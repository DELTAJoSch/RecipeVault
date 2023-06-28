package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.AuthorMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.DeletionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Endpoint for Author-Requests.
 */
@RestController
@RequestMapping(value = "/api/v1/author")
@Validated
public class AuthorManagementEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorManagementEndpoint(AuthorService authorService, AuthorMapper authorMapper) {
        this.authorService = authorService;
        this.authorMapper = authorMapper;
    }

    /**
     * Find all authors.
     *
     * @return List of all Authors in persistent datastore.
     */
    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "get all authors according to page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<AuthorDetailsDto>> getAuthors(
        @Valid
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size) {

        LOGGER.info("GET /api/v1/author");
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: {} Size: {}", page, size);

        var count = authorService.countAll();

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(authorMapper.authorToAuthorDetailsDto(authorService.getAuthors(pageable)));

    }

    /**
     * Find author based on its id.
     *
     * @param id id of the author to be found
     * @return Returns the author with the specified id.
     */
    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    @Operation(summary = "Get author by its id", security = @SecurityRequirement(name = "apiKey"))
    public AuthorDetailsDto findById(@PathVariable Long id) {
        LOGGER.info("GET /api/v1/author/{}", id);

        return authorMapper.authorToAuthorDetailsDto(authorService.getAuthorById(id));
    }

    /**
     * Create new author.
     *
     * @param authorDto The new authors data
     * @throws ValidationException          Thrown if the data is invalid
     * @throws ObjectAlreadyExistsException Thrown if the object already exists.
     */
    @Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create an author", security = @SecurityRequirement(name = "apiKey"))
    public void create(@Valid @RequestBody AuthorCreateDto authorDto) throws
        ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/author");

        authorService.createAuthor(authorDto);
    }

    /**
     * Update Author.
     *
     * @param updateDto Details for updated author
     * @return Returns the updated Author if successful
     * @throws ValidationException          Thrown if validation of given data fails.
     * @throws ObjectAlreadyExistsException Thrown if author with same first- and lastname already exist.
     */
    @Secured("ROLE_USER")
    @PostMapping("/{id}")
    @Operation(summary = "update an author", security = @SecurityRequirement(name = "apiKey"))
    public AuthorDetailsDto update(@PathVariable Long id, @Valid @RequestBody AuthorDetailsDto updateDto) throws
        ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/author/{}", id);

        var result = authorService.updateAuthor(updateDto);
        return authorMapper.authorToAuthorDetailsDto(result);
    }

    /**
     * Delete the specified wine.
     *
     * @param id             The id of the author to delete.
     * @param authentication The user triggering the request.
     * @throws UserPermissionException Thrown if the user does not have the permission to delete author.
     * @throws DeletionException       if the author can't be deleted because it is in use
     */
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the author specified by the id", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable Long id, Authentication authentication) throws UserPermissionException, DeletionException {
        LOGGER.info("DELETE /api/v1/author/{}", id);

        authorService.deleteAuthor(id, authentication.getName());
    }

    /**
     * Search for Author with a given Name.
     *
     * @param name Name the author should have.
     * @return A list of authors, who's names match the given name
     */
    @Secured("ROLE_USER")
    @GetMapping(params = {"name"})
    @Operation(summary = "Get author by their name", security = @SecurityRequirement(name = "apiKey"))
    public List<AuthorDetailsDto> search(@RequestParam String name) {
        LOGGER.info("GET /api/v1/author?name={}", name);

        return authorMapper.authorToAuthorDetailsDto(authorService.search(name));
    }


}
