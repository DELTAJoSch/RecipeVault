package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.exception.DeletionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This service contains everything related to the management of ingredients.
 */
public interface IngredientService {

    /**
     * Fetches first 10 ingredients by their name.
     *
     * @param searchParameters The name of the ingredients to look for.
     * @return Returns the ingredients
     * @throws NotFoundException if the ingredient could not be found
     */
    List<Ingredient> search(IngredientSearchDto searchParameters);

    /**
     * Count all ingredients matching the search parameters.
     *
     * @param searchDto The search parameters to look for
     * @return Returns the number of ingredients matching the search query.
     */
    long countFindBy(IngredientSearchDto searchDto);

    /**
     * Fetch all ingredients for the specified page and search.
     *
     * @param ingredientSearchDto The search parameters to look for
     * @param pageable      The pages to get
     * @return Returns the ingredients
     */
    List<Ingredient> findIngredients(IngredientSearchDto ingredientSearchDto, Pageable pageable);

    /**
     * Creates a new ingredient.
     *
     * @param ingredient The ingredient to create
     * @throws ObjectAlreadyExistsException if a similar ingredient already exists.
     */
    void createIngredient(IngredientCreateDto ingredient) throws ObjectAlreadyExistsException;

    /**
     * Updates the ingredient based on the specified information.
     *
     * @param updated  The update ingredient details
     * @param username The username of the user that called this action
     * @return Returns the updated ingredient
     * @throws NotFoundException            if the dto to update is not found
     * @throws UserPermissionException      if the user that executed this query is not an admin
     * @throws ObjectAlreadyExistsException if a similar ingredient already exists.
     */
    Ingredient updateIngredient(IngredientDetailsDto updated, String username) throws NotFoundException,
        UserPermissionException,
        ObjectAlreadyExistsException;

    /**
     * Deletes the ingredient.
     *
     * @param id       The id of the ingredient to delete
     * @param username The user who called this
     * @throws NotFoundException       if the requested ingredient was not found
     * @throws UserPermissionException if the user is not an admin
     * @throws InternalServerException if the issuing user can't be found in the database
     * @throws DeletionException if the ingredient to delete is currently in use
     */
    void delete(Long id, String username) throws NotFoundException, UserPermissionException, InternalServerException, DeletionException;

}

