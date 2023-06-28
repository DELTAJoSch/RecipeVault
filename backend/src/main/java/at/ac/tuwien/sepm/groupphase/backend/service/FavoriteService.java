package at.ac.tuwien.sepm.groupphase.backend.service;


import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;

import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This service contains everything related to favorites.
 */
public interface FavoriteService {
    /**
     * Get all favorite recipes of a user.
     *
     * @param email of user, whose recipes are retrieved
     * @return favorite recipes of user
     */
    List<Recipe> getFavorites(String email, Pageable pageable) throws ValidationException;

    /**
     * add a recipe to favorites of a user.
     *
     * @param email    of user
     * @param recipeId id of recipe
     * @throws ValidationException          if user or recipe is null
     * @throws ObjectAlreadyExistsException if the favorite is already stored
     */
    void addFavorite(String email, Long recipeId) throws ValidationException, ObjectAlreadyExistsException;

    /**
     * Delete a recipe from a user's favorites.
     *
     * @param email    of user from which to remove the recipe
     * @param recipeId recipe to remove
     */
    void delete(String email, Long recipeId);

    /**
     * Get the number of favorites of a user with the provided id.
     *
     * @param email of user
     * @return number of favorites
     */
    Long countFavoritesOfUser(String email);

    /**
     * Get if the recipe is included in the user's favorites.
     *
     * @param email    of user
     * @param recipeId id of recipe
     * @return if the recipe is a favorite
     */
    Boolean getStatus(String email, Long recipeId);
}
