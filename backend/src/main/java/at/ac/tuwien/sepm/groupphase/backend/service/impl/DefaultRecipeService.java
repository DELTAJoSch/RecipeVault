package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.engines.WineRecommendationEngine;
import at.ac.tuwien.sepm.groupphase.backend.engines.containers.WineRecommendationResult;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.AuthorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.AmountRepository;

import at.ac.tuwien.sepm.groupphase.backend.service.ListService;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Default implementation of RecipeService.
 */
@Service
public class DefaultRecipeService implements RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private AmountRepository amountRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private WineRecommendationEngine wineRecommendationEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListService listService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public Recipe getRecipeById(Long id) throws NotFoundException {
        LOGGER.trace("getRecipeById({})", id);
        var result = this.recipeRepository.findById(id);

        if (result.isEmpty()) {
            throw new NotFoundException("Recipe with id " + id + " could not be found!");
        }

        return result.get();
    }

    @Override
    public List<Recipe> getRecipes(Pageable pageable) {
        LOGGER.trace("getRecipes({})", pageable);
        return recipeRepository.findAll(pageable).getContent();
    }

    @Override
    @Transactional
    public void createRecipe(RecipeCreateDto recipe, String username) throws ObjectAlreadyExistsException {
        LOGGER.trace("createRecipe({})", recipe);
        var user = this.userRepository.findByEmail(username);
        if (user == null) {
            throw new InternalServerException("User could not be found in the database.");
        }

        var amounts = new LinkedList<Amount>();
        if (recipe.getIngredients() != null) {
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                var amount = recipe.getIngredients().get(i);
                var ingredient = ingredientRepository.findByName(amount.getIngredient().getName());

                if (ingredient.isPresent()) {
                    if (amounts.stream().anyMatch(o -> Objects.equals(o.getIngredient().getName(), ingredient.get().getName()))) {
                        throw new ObjectAlreadyExistsException("Zutat " + ingredient.get().getName() + " wurde mehr als einmal hinzugefügt. Bitte zusammenfassen.");
                    }

                    Amount newAmount = new Amount.AmountBuilder()
                        .setAmount(amount.getAmount())
                        .setUnit(amount.getUnit())
                        .setIngredient(ingredient.get())
                        .build();


                    amounts.add(newAmount);
                } else {
                    throw new NotFoundException("Ingredient not found: " + amount.getIngredient().getName());
                }
            }
        }

        var recommendation = wineRecommendationEngine.generateRecommendation(amounts);

        try {
            Recipe entity = new Recipe();
            entity.setName(recipe.getName());
            entity.setShortDescription(recipe.getShortDescription());
            entity.setDescription(recipe.getDescription());
            entity.setDifficulty(recipe.getDifficulty());
            entity.setOwner(user);
            entity.setIngredients(amounts);
            entity.setImageId(recipe.getImageId());

            if (recipe.getAuthor() != null) {
                var authorDbEntry = authorRepository.findById(recipe.getAuthor().getId());
                if (authorDbEntry.isEmpty()) {
                    throw new NotFoundException("Author could not be found in the database");
                } else {
                    Author author = authorDbEntry.get();
                    author.setFirstname(recipe.getAuthor().getFirstname());
                    author.setLastname(recipe.getAuthor().getLastname());
                    author.setDescription(recipe.getAuthor().getDescription());
                    entity.setAuthor(author);
                }
            }

            if (recommendation != null) {
                entity.setRecommendationConfidence(recommendation.getConfidence());
                entity.setRecommendedCategory(recommendation.getCategory());
            }

            for (int i = 0; i < amounts.size(); i++) {
                amounts.get(i).setRecipe(entity);
            }

            this.recipeRepository.save(entity);
            for (int i = 0; i < amounts.size(); i++) {
                this.amountRepository.saveAndFlush(amounts.get(i));
            }
            this.recipeRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("A similar recipe already exists. Try searching for it!", e);
        }
    }

    @Override
    public Recipe updateRecipe(RecipeDetailsDto updated, String username) throws NotFoundException,
        UserPermissionException,
        ObjectAlreadyExistsException {

        LOGGER.trace("updateRecipe({})", updated);
        var issuer = this.userRepository.findByEmail(username);
        if (issuer == null) {
            throw new InternalServerException("User could not be found in the database.");
        }

        var dbEntry = this.recipeRepository.findById(updated.getId());
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Recipe could not be found in the database.");
        }

        Recipe entity = dbEntry.get();

        if (issuer.getAdmin()) { // if issuer is admin, update all including owner
            var owner = this.userRepository.findByEmail(updated.getOwner().getEmail());
            if (owner == null) {
                throw new InternalServerException("Owner could not be found in the database.");
            }

            entity.setOwner(owner);
        } else if (!Objects.equals(issuer.getId(), entity.getOwner().getId())) { //otherwise compare owner and issuer
            throw new UserPermissionException("Issuing user is not Owner or Admin!");
        }

        // get the old amounts to compare against
        var oldAmounts = entity.getIngredients();
        var amounts = new ArrayList<Amount>();
        if (updated.getIngredients() != null) {
            for (var amount : updated.getIngredients()) { // all updated ingredients
                var ingredient = ingredientRepository.findByName(amount.getIngredient().getName());

                if (ingredient.isPresent()) {
                    //get the first occurence of an amount with the same ingredient
                    var old = oldAmounts.stream().filter(amt -> Objects.equals(amt.getIngredient().getId(), ingredient.get().getId())).findFirst();

                    if (old.isPresent()) { // if an old amount for the same ingredient exists, update the old amount
                        var updateAmount = old.get();
                        updateAmount.setAmount(amount.getAmount());
                        updateAmount.setUnit(amount.getUnit());
                        amounts.add(updateAmount);
                    } else { // otherwise, create a new amount
                        Amount newAmount = new Amount.AmountBuilder()
                            .setAmount(amount.getAmount())
                            .setUnit(amount.getUnit())
                            .setIngredient(ingredient.get())
                            .setRecipe(entity)
                            .build();

                        amounts.add(newAmount);
                    }
                }
            }
        }

        WineRecommendationResult recommendation = wineRecommendationEngine.generateRecommendation(amounts);

        try {
            entity.setName(updated.getName());
            entity.setDescription(updated.getDescription());
            entity.setShortDescription(updated.getShortDescription());
            entity.setDifficulty(updated.getDifficulty());
            entity.setIngredients(amounts);
            entity.setImageId(updated.getImageId());
            if (updated.getAuthor() != null) {
                var authorDbEntry = authorRepository.findById(updated.getAuthor().getId());
                if (authorDbEntry.isEmpty()) {
                    throw new NotFoundException("Author could not be found in the database.");
                } else {
                    if (entity.getAuthor() == null || !Objects.equals(entity.getAuthor().getId(), updated.getAuthor().getId())) {
                        Author author = authorDbEntry.get();
                        author.setFirstname(updated.getAuthor().getFirstname());
                        author.setLastname(updated.getAuthor().getLastname());
                        author.setDescription(updated.getAuthor().getDescription());
                        entity.setAuthor(author);
                    }
                }
            } else {
                entity.setAuthor(null);
            }

            if (recommendation != null) {
                entity.setRecommendationConfidence(recommendation.getConfidence());
                entity.setRecommendedCategory(recommendation.getCategory());
            } else {
                entity.setRecommendedCategory(null);
                entity.setRecommendationConfidence(null);
            }

            this.recipeRepository.saveAndFlush(entity);

        } catch (DataIntegrityViolationException e) {
            throw new ObjectAlreadyExistsException("A similar recipe already exists. Try searching for it!", e);
        }

        return entity;
    }

    @Override
    public void delete(Long id, String username) throws NotFoundException, UserPermissionException {
        LOGGER.trace("deleteRecipe({})", id);
        var issuer = this.userRepository.findByEmail(username);
        if (issuer == null) {
            throw new InternalServerException("User could not be found in the database.");
        }

        var dbEntry = this.recipeRepository.findById(id);
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Recipe could not be found in the database.");
        }

        Recipe entity = dbEntry.get();

        if (issuer.getId() != entity.getOwner().getId() && !issuer.getAdmin()) {
            throw new UserPermissionException("Issuing user is not Owner or Admin!");
        }

        removeFromLists(entity, username);
        Long imageId = entity.getImageId();

        this.recipeRepository.delete(entity);

        if (imageId != null) {
            imageService.deleteImage(entity.getImageId());
        }
    }

    @Override
    public long countAll() {
        return this.recipeRepository.count();
    }

    @Override
    public long countFindBy(RecipeSearchDto searchDto) {
        return this.recipeRepository.countOfFind(
            searchDto.getName(),
            searchDto.getDifficulty()
        );
    }

    @Override
    public List<Recipe> findRecipes(RecipeSearchDto recipeSearchDto, Pageable pageable) {
        LOGGER.trace("findRecipes({})", recipeSearchDto);
        return this.recipeRepository.find(
                recipeSearchDto.getName(),
                recipeSearchDto.getDifficulty(),
                pageable)
            .getContent();
    }

    /**
     * Removes a recipe from all lists it is in.
     *
     * @param recipe to remove
     * @param email of issuing user
     */
    private void removeFromLists(Recipe recipe, String email) {
        List<RecipeList> lists = recipe.getRecipeLists();
        for (RecipeList list : lists) {
            listService.deleteRecipeFromList(list.getUser().getEmail(), recipe.getId(), list.getName());
        }
    }

}

