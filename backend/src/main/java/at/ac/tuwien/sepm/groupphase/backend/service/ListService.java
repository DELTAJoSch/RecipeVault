package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Pageable;


import java.util.List;

/**
 * This service contains everything related to lists.
 */
public interface ListService {

    /**
     * Get all recipes in a specific list of a user.
     *
     * @param email    of the user, who the list belongs to
     * @param name     of list
     * @param pageable the requested page
     * @return recipes in that list on the given page
     */
    List<Recipe> getRecipesOfList(String email, String name, Pageable pageable);

    /**
     * create a new list for a user.
     *
     * @param email of the user who wants to create the list
     * @param name  of list
     * @throws ObjectAlreadyExistsException if a list with this name already exists
     * @throws ValidationException          if the list name is empty
     */
    void createList(String email, String name) throws ObjectAlreadyExistsException, ValidationException;


    /**
     * Add a recipe to a user's list.
     *
     * @param recipeId id of the recipe
     * @param name     of the list
     * @param email    of the user to whom belongs the list
     * @throws ObjectAlreadyExistsException if the recipe is already in that list
     */
    void addToList(String email, Long recipeId, String name) throws ObjectAlreadyExistsException;

    /**
     * Get all lists of a user.
     *
     * @param email of user
     * @return list of lists
     */
    List<RecipeList> getListsOfUser(String email);

    /**
     * Get the number of recipes in the list.
     *
     * @param email of user who the list belongs to
     * @param name  of list
     */
    long countRecipesOfList(String email, String name);

    /**
     * Remove a recipe from a list.
     *
     * @param email    of user
     * @param recipeId or recipe
     * @param name     of list
     */
    void deleteRecipeFromList(String email, Long recipeId, String name);

    /**
     * Delete a list.
     *
     * @param email of the issuing user
     * @param name  of the list
     */
    void deleteList(String email, String name);

}
