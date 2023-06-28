package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NoteDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NoteMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

@RestController
@RequestMapping(value = "/api/v1/notes")
public class NoteEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @Autowired
    public NoteEndpoint(NoteService noteService, NoteMapper noteMapper) {
        this.noteService = noteService;
        this.noteMapper = noteMapper;
    }

    /**
     * Retrieves detailed information about a specific note.
     *
     * @param id              The ID of the note to retrieve.
     * @param authentication  The authentication object containing user information.
     * @return                A NoteDto object containing the details of the note.
     */
    @Secured("ROLE_USER") // Requires ROLE_USER role for access
    @GetMapping("/{id}") // Handles GET requests with an ID parameter
    @Transactional // Enables transactional behavior for the method
    @Operation(summary = "Get detailed information about a specific note", security = @SecurityRequirement(name = "apiKey"))
    public NoteDto find(@PathVariable Long id, Authentication authentication) {
        LOGGER.info("GET /api/v1/notes/{}", id); // Logs the request information
        var note = noteService.findNote(authentication.getName(), id, authentication.getName());
        if (Objects.equals(note.getContent(), "")) {
            return new NoteDto.NoteDtoBuilder()
                    .setOwnerId(null)
                    .setRecipeId(id)
                    .setContent("")
                    .build();
        }
        return noteMapper.noteToNoteDto(note);
    }

    /**
     * Updates or creates a note.
     *
     * @param noteDto         The NoteDto object containing the note information.
     * @param authentication  The authentication object containing user information.
     * @return                A NoteDto object containing the updated or created note details.
     * @throws UserPermissionException  If the user does not have permission to perform the operation.
     * @throws ValidationException      If the noteDto fails validation.
     */
    @Secured("ROLE_USER") // Requires ROLE_USER role for access
    @PostMapping // Handles POST requests
    @Operation(summary = "Update note", security = @SecurityRequirement(name = "apiKey"))
    public NoteDto updateOrCreate(@Valid @RequestBody NoteDto noteDto, Authentication authentication)
            throws UserPermissionException, ValidationException {
        LOGGER.info("POST /api/v1/notes"); // Logs the request information
        var result = noteService.updateOrCreateNote(noteDto, authentication.getName());
        return noteMapper.noteToNoteDto(result);
    }

    /**
     * Deletes a note.
     *
     * @param id              The ID of the note to delete.
     * @param authentication  The authentication object containing user information.
     * @throws UserPermissionException  If the user does not have permission to delete the note.
     * @throws NotFoundException       If the note is not found.
     */
    @Secured("ROLE_USER") // Requires ROLE_USER role for access
    @DeleteMapping("/{id}") // Handles DELETE requests with an ID parameter
    @Operation(summary = "Delete the User specified by the id", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable Long id, Authentication authentication)
            throws UserPermissionException, NotFoundException {
        noteService.deleteNote(id, authentication.getName(), authentication.getName());
    }

}
