package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.IngredientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import at.ac.tuwien.sepm.groupphase.backend.exception.DeletionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.IngredientService;
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

@RestController
@RequestMapping(value = "/api/v1/ingredient")
@Validated
public class IngredientManagementEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final IngredientService ingredientService;
    private final IngredientMapper ingredientMapper;

    @Autowired
    public IngredientManagementEndpoint(IngredientService ingredientService, IngredientMapper ingredientMapper) {
        this.ingredientService = ingredientService;
        this.ingredientMapper = ingredientMapper;
    }

    /**
     * Search for a specific ingredient.
     *
     * @param searchParameters parameters to filter ingredients
     * @return the matching ingredients
     */
    @Secured("ROLE_USER")
    @GetMapping("/{name}")
    @Operation(summary = "Get ingredients by their name", security = @SecurityRequirement(name = "apiKey"))
    public List<IngredientDetailsDto> search(@PathVariable String name, @Valid IngredientSearchDto searchParameters) {
        LOGGER.info("GET /api/v1/ingredient/{}", name);

        return ingredientMapper.ingredientToIngredientDetailsDto(ingredientService.search(searchParameters));
    }

    /**
     * Find all ingredients matching the search parameters within the page limit.
     *
     * @param page     The page to look for. Default = 0
     * @param size     The size of the page. Default = 25
     * @param category The category to search for
     * @param name     The name to search for
     * @return Returns a list of ingredients matching the search parameters at the requested page
     * @throws ValidationException if the search data is invalid
     */
    @Secured("ROLE_USER")
    @GetMapping("/search")
    @Operation(summary = "Get ingredients matching search parameters by page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<IngredientDetailsDto>> findBy(
        @Valid
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) IngredientMatchingCategory category)
        throws ValidationException {
        LOGGER.info("GET /api/v1/ingredient/search");

        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: " + page + " Size: " + size);

        IngredientSearchDto searchDto = new IngredientSearchDto.IngredientSearchDtoBuilder()
            .setName(name)
            .setCategory(category)
            .build();

        var count = ingredientService.countFindBy(searchDto);

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(ingredientMapper.ingredientToIngredientDetailsDto(ingredientService.findIngredients(searchDto, pageable)));
    }


    /**
     * Create a new ingredient.
     *
     * @param ingredientCreateDto The new ingredient's data
     * @throws ValidationException Thrown if the data is invalid
     */
    @Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new ingredient", security = @SecurityRequirement(name = "apiKey"))
    public void create(@Valid @RequestBody IngredientCreateDto ingredientCreateDto)
        throws ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/ingredient");

        ingredientService.createIngredient(ingredientCreateDto);
    }

    /**
     * Update an ingredient.
     *
     * @param ingredientDetailsDto The new ingredients details
     * @param authentication       The logged-in user
     * @return Returns the updated ingredient if successful
     * @throws UserPermissionException      if the user triggering the request does not have the necessary permissions
     * @throws ValidationException          if the updated data is invalid
     * @throws ObjectAlreadyExistsException if the object already exists
     */
    @Secured("ROLE_USER")
    @PostMapping("/{id}")
    @Operation(summary = "Update an ingredient by its id", security = @SecurityRequirement(name = "apiKey"))
    public IngredientDetailsDto update(@PathVariable Long id, @Valid @RequestBody IngredientDetailsDto ingredientDetailsDto, Authentication authentication)
        throws UserPermissionException,
        ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/ingredient/{}", id);

        var result = ingredientService.updateIngredient(ingredientDetailsDto, authentication.getName());
        return ingredientMapper.ingredientToIngredientDetailsDto(result);
    }

    /**
     * Delete the specified ingredient.
     *
     * @param id             The id of the ingredient to delete
     * @param authentication The user triggering the request
     * @throws UserPermissionException if the user does not have the necessary permissions
     * @throws DeletionException if the ingredient can't be deleted because it is in use
     */
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the ingredient specified by the id", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable Long id, Authentication authentication) throws UserPermissionException, DeletionException {
        LOGGER.info("DELETE /api/v1/ingredient/{}", id);

        ingredientService.delete(id, authentication.getName());
    }
}
