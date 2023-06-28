package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeListCompositeKey;

import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ListRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DefaultListService implements ListService {
    @Autowired
    private ListRepository listRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public List<Recipe> getRecipesOfList(String email, String name, Pageable pageable) {
        LOGGER.trace("getRecipesOfList({}, {}, {})", email, name, pageable);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail-Adresse " + email + " konnte nicht gefunden werden.");
        }
        
        if (!listRepository.existsById(new RecipeListCompositeKey(user, name))) {
            throw new NotFoundException("Liste konnte nicht gefunden werden.");
        }

        return listRepository.getRecipesByNameAndUserId(name, user.getId(), pageable);
    }


    @Override
    public void createList(String email, String name) throws ObjectAlreadyExistsException, ValidationException {
        LOGGER.trace("createList({}, {})", email, name);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail-Adresse " + email + " konnte nicht gefunden werden.");
        }

        if (listRepository.existsById(new RecipeListCompositeKey(user, name))) {
            throw new ObjectAlreadyExistsException("Eine Liste mit diesem Namen existiert bereits.");
        }

        listRepository.saveAndFlush(new RecipeList(user, new ArrayList<>(), name));
    }

    @Override
    public void addToList(String email, Long recipeId, String name) throws ObjectAlreadyExistsException {
        LOGGER.trace("addToList({}, {}, {})", email, recipeId, name);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail-Adresse " + email + " konnte nicht gefunden werden.");
        }

        if (!listRepository.existsById(new RecipeListCompositeKey(user, name))) {
            throw new NotFoundException("Liste konnte nicht gefunden werden.");
        }

        List<Recipe> recipes = listRepository.getRecipesByNameAndUserId(name, user.getId());
        Optional<Recipe> rec = recipeRepository.findById(recipeId);
        if (rec.isEmpty()) {
            throw new NotFoundException("Rezept konnte nicht gefunden werden.");
        }

        if (recipes.contains(rec.get())) {
            throw new ObjectAlreadyExistsException("Dieses Rezept ist bereits in dieser Liste vorhanden.");
        }

        recipes.add(rec.get());
        listRepository.saveAndFlush(new RecipeList(user, recipes, name));
    }

    @Override
    public List<RecipeList> getListsOfUser(String email) {
        LOGGER.trace("getListsOfUser({})", email);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail-Adresse " + email + " konnte nicht gefunden werden.");
        }

        return listRepository.getAllByUserId(user.getId());
    }

    @Override
    public void deleteRecipeFromList(String email, Long recipeId, String name) {
        LOGGER.trace("deleteRecipeFromList({}, {}, {})", email, recipeId, name);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail-Adresse " + email + " konnte nicht gefunden werden.");
        }

        if (!listRepository.existsById(new RecipeListCompositeKey(user, name))) {
            throw new NotFoundException("Liste konnte nicht gefunden werden.");
        }

        Optional<RecipeList> list = listRepository.findByUserIdAndName(user.getId(), name);
        RecipeList l = list.get();
        Optional<Recipe> rec = recipeRepository.findById(recipeId);
        if (rec.isEmpty()) {
            throw new NotFoundException("Rezept konnte nicht gefunden werden.");
        }
        l.getRecipe().remove(rec.get());

        listRepository.saveAndFlush(new RecipeList(user, l.getRecipe(), name));
    }

    @Override
    public void deleteList(String email, String name) {
        LOGGER.trace("deleteList({}, {})", email, name);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail-Adresse " + email + " konnte nicht gefunden werden.");
        }

        if (!listRepository.existsById(new RecipeListCompositeKey(user, name))) {
            throw new NotFoundException("Liste konnte nicht gefunden werden.");
        }

        Optional<RecipeList> list = listRepository.findByUserIdAndName(user.getId(), name);
        RecipeList l = list.get();

        listRepository.delete(l);
    }

    @Override
    public long countRecipesOfList(String email, String name) {
        LOGGER.trace("countRecipesOfList({}, {})", email, name);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail-Adresse " + email + " konnte nicht gefunden werden.");
        }

        if (!listRepository.existsById(new RecipeListCompositeKey(user, name))) {
            throw new NotFoundException("Liste konnte nicht gefunden werden.");
        }
        
        Optional<RecipeList> list = listRepository.findByUserIdAndName(user.getId(), name);
        return list.get().getRecipe().size();
    }
}
