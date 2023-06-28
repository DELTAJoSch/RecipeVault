package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NoteDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Note;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import jakarta.validation.Valid;

/**
 * This interface represents a service for managing notes related to recipes.
 */
public interface NoteService {

    /**
     * Finds a note by the owner's email, recipe ID.
     *
     * @param ownerEmail  The email of the note owner.
     * @param recipeId    The ID of the recipe associated with the note.
     * @param issuerEmail The email of the request issuer.
     * @return The found Note object.
     * @throws NotFoundException If the note is not found.
     */
    Note findNote(String ownerEmail, Long recipeId, String issuerEmail) 
        throws NotFoundException;

    /**
     * Deletes a note associated with a recipe.
     *
     * @param recipeId    The ID of the recipe associated with the note.
     * @param email       The email of the note owner.
     * @param issuerEmail The email of the request issuer.
     * @throws NotFoundException       If the note or recipe is not found.
     * @throws UserPermissionException If the user does not have permission to
     *                                 delete the note.
     */
    void deleteNote(Long recipeId, String email, String issuerEmail)
            throws NotFoundException, UserPermissionException;

    /**
     * Updates or creates a note based on the provided NoteDto object.
     *
     * @param noteDto     The NoteDto object containing the note information.
     * @param issuerEmail The email of the note issuer.
     * @return The updated or newly created Note object.
     * @throws UserPermissionException If the user does not have permission to
     *                                 update or create the note.
     * @throws ValidationException     If the noteDto object fails validation.
     */
    Note updateOrCreateNote(@Valid NoteDto noteDto, String issuerEmail) throws UserPermissionException, 
            ValidationException;
}