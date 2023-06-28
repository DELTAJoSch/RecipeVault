package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.CommentMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Endpoint for comments of recipes.
 */
@RestController
@RequestMapping(value = "/api/v1/comments")
public class CommentEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentEndpoint(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    /**
     * Get all comments of a recipe.
     *
     * @param id of the recipe to get comments of
     * @return list of comment dtos for the specific recipe
     */
    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    @Transactional
    @Operation(summary = "Get comments for specific recipe", security = @SecurityRequirement(name = "apiKey"))
    public List<CommentDto> find(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/comments/{}", id);
        var comments = commentService.getComments(id);
        return commentMapper.commentToCommentDto(comments);
    }

    /**
     * Create a new comment for a recipe.
     *
     * @param commentDto the comment to create
     * @param authentication the authentication of the issuer
     * @return the newly created comment
     * @throws NotFoundException when the recipe or user are not found
     * @throws ValidationException when the comment to create is invalid
     */
    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "Create comment", security = @SecurityRequirement(name = "apiKey"))
    public CommentDto create(@Valid @RequestBody CommentDto commentDto, Authentication authentication)
        throws NotFoundException,
        ValidationException {
        LOGGER.info("POST /api/v1/comments");
        var result = commentService.createComment(commentDto, authentication.getName());
        return commentMapper.commentToCommentDto(result);
    }

    /**
     * Delete comment from recipe.
     *
     * @param commentDto the comment to delete
     * @param authentication the authentication of the issuing user
     * @throws UserPermissionException if user is not allowed to delete the comment
     * @throws NotFoundException if comment or user are not found
     */
    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "Delete comment", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@Valid @RequestBody CommentDto commentDto, Authentication authentication)
        throws UserPermissionException, NotFoundException {
        LOGGER.info("DELETE /api/v1/comments");
        commentService.deleteComment(commentDto, authentication.getName());
    }

}
