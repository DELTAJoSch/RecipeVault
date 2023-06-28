package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import jakarta.validation.Valid;

import java.util.List;

/**
 * This service contains everything relating to comments of recipes.
 */
public interface CommentService {

    /**
     * Get all comments for the desired recipe.
     *
     * @param recipeId the recipe to get comments of
     * @return a list of all comments for the specified recipe
     * @throws NotFoundException if the recipe is not in the database
     */
    List<Comment> getComments(Long recipeId)
        throws NotFoundException;

    /**
     * Delete a comment from a recipe.
     *
     * @param commentDto the comment to delete
     * @param issuerEmail the email address of the user that wants to delete the comment
     * @throws NotFoundException if the comment or user are not found
     * @throws UserPermissionException if the user is not allowed to delete the comment
     */
    void deleteComment(@Valid CommentDto commentDto, String issuerEmail)
        throws NotFoundException, UserPermissionException;

    /**
     * Creates a comment for a recipe.
     *
     * @param commentDto the comment to create
     * @param issuerEmail the email address of the user that wants to create the comment
     * @return the created comment
     * @throws ValidationException if the comment is not valid
     */
    Comment createComment(@Valid CommentDto commentDto, String issuerEmail) throws ValidationException;

}