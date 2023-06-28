package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Pageable;


import java.util.List;

/**
 * This service contains everything related to the management (CRUD) of recipes.
 */
public interface RecipeService {

    /**
     * Fetch a recipe by its id.
     *
     * @param id The id of the wine to look for.
     * @return Returns the recipe
     * @throws NotFoundException If the recipe could not be found
     */
    Recipe getRecipeById(Long id) throws NotFoundException;

    /**
     * Fetch all recipes for the specified page.
     *
     * @param pageable The pages to get
     * @return Returns the recipes
     */
    List<Recipe> getRecipes(Pageable pageable);

    /**
     * Create a new recipe with the calling user as the owner.
     *
     * @param wine     The wine to create
     * @param username The owner of the recipe
     * @throws ValidationException          Thrown if the validation of the createDto failed
     * @throws ObjectAlreadyExistsException Thrown if a similar wine already exists.
     */
    void createRecipe(RecipeCreateDto wine, String username) throws ValidationException, ObjectAlreadyExistsException;

    /**
     * Updates the recipe based on the specified information.
     *
     * @param updated  The update recipe details
     * @param username The username of the user that called this action
     * @return Returns the updated recipe
     * @throws NotFoundException            Thrown if the dto to update is not found
     * @throws UserPermissionException      Thrown if the user that executed this query is not the owner or an admin
     * @throws ValidationException          Thrown if the Details dto failed validation
     * @throws ObjectAlreadyExistsException Thrown if a similar recipe already exists.
     */
    Recipe updateRecipe(RecipeDetailsDto updated, String username) throws NotFoundException,
        UserPermissionException,
        ValidationException,
        ObjectAlreadyExistsException;

    /**
     * Deletes the recipe.
     *
     * @param id       The id of the recipe
     * @param username The user who called this query
     * @throws NotFoundException       Thrown if the requested recipe could not be found
     * @throws UserPermissionException Thrown if the user is not the owner or an admin
     */
    void delete(Long id, String username) throws NotFoundException, UserPermissionException;

    /**
     * Count all recipes in the table.
     *
     * @return Returns the number of wines in the database
     */
    long countAll();


    /**
     * Count all recipes matching the search parameters.
     *
     * @param searchDto The search parameters to look for
     * @return Returns the number of wines matching the search query.
     */
    long countFindBy(RecipeSearchDto searchDto) throws ValidationException;

    /**
     * Fetch all recipes for the specified page and search.
     *
     * @param recipeSearchDto The search parameters to look for
     * @param pageable        The pages to get
     * @return Returns the wines
     */
    List<Recipe> findRecipes(RecipeSearchDto recipeSearchDto, Pageable pageable) throws ValidationException;
}
