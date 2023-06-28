package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Favorite;
import at.ac.tuwien.sepm.groupphase.backend.entity.FavoriteCompositeKey;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.FavoriteRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.FavoriteService;
import at.ac.tuwien.sepm.groupphase.backend.validators.FavoriteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultFavoriteService implements FavoriteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public List<Recipe> getFavorites(String email, Pageable pageable) {
        LOGGER.trace("getFavorites({})", email);
        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail " + email + " konnte nicht gefunden werden");
        }
        return favoriteRepository.getFavorites(user.getId(), pageable);

    }

    @Override
    public void addFavorite(String email, Long recipeId) throws ValidationException, ObjectAlreadyExistsException {
        LOGGER.trace("addFavorite({}, {})", email, recipeId);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail " + email + " konnte nicht gefunden werden");
        }

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new NotFoundException("Rezept mit der ID " + recipeId + " konnte nicht gefunden werden");
        }

        if (this.favoriteRepository.existsById(new FavoriteCompositeKey(user, recipe.get()))) {
            throw new ObjectAlreadyExistsException("Dieses Rezept ist bereits als Favorit markiert");
        }

        Favorite favorite = new Favorite(user, recipe.get());
        FavoriteValidator.validateForAddition(favorite);
        this.favoriteRepository.saveAndFlush(favorite);
    }

    @Override
    public void delete(String email, Long recipeId) {
        LOGGER.trace("delete({}, {})", email, recipeId);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail " + email + " konnte nicht gefunden werden");
        }

        var dbEntry = this.favoriteRepository.findByUserIdAndRecipeId(user.getId(), recipeId);
        if (dbEntry.isEmpty()) {
            throw new NotFoundException("Favorit konnte nicht in der Datenbank gefunden werden");
        }

        Favorite entity = dbEntry.get();
        this.favoriteRepository.delete(entity);
    }

    @Override
    public Long countFavoritesOfUser(String email) {
        LOGGER.trace("countFavoritesOfUser({})", email);

        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail " + email + " konnte nicht gefunden werden");
        }

        return this.favoriteRepository.countByUserId(user.getId());
    }

    @Override
    public Boolean getStatus(String email, Long recipeId) {
        LOGGER.trace("getStatus({}, {})", email, recipeId);
        ApplicationUser user = userRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException("Benutzer mit E-Mail " + email + " konnte nicht gefunden werden");
        }

        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new NotFoundException("Rezept mit der ID " + recipeId + " konnte nicht gefunden werden");
        }

        return this.favoriteRepository.existsById(new FavoriteCompositeKey(user, recipe.get()));
    }
}
