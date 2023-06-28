package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.WineMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.WineService;
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
 * Endpoint for Wine Management.
 */
@RestController
@RequestMapping(value = "/api/v1/wine")
@Validated
public class WineManagementEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final WineService wineService;
    private final WineMapper wineMapper;

    @Autowired
    public WineManagementEndpoint(WineService wineService, WineMapper wineMapper) {
        this.wineService = wineService;
        this.wineMapper = wineMapper;
    }

    /**
     * Find a specific wine by its id.
     *
     * @param id The id to look for
     * @return Returns the wine with the specified id
     */
    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    @Operation(summary = "Get a wine by its id", security = @SecurityRequirement(name = "apiKey"))
    public WineDetailsDto find(@PathVariable Long id) {
        LOGGER.trace("find({})", id);
        return wineMapper.wineToWineDetailsDto(wineService.getWineById(id));
    }

    /**
     * Find all wines matching the search parameters within the page limit.
     *
     * @param page     The page to look for. Default = 0
     * @param size     The size of the page. Default = 25
     * @param category The category to search for
     * @param name     The name to search for
     * @param country  The country to search for
     * @param vinyard  The vinyard to search for
     * @return Returns a list of wines matching the search porameters at the requested page
     */
    @Secured("ROLE_USER")
    @GetMapping("/search")
    @Operation(summary = "Get wines matching search parameters by page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<WineDetailsDto>> findBy(
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) WineCategory category,
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String vinyard)
        throws ValidationException {
        LOGGER.trace("findBy({}, {}, {}, {}, {}, {})", page, size, name, category, country, vinyard);

        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: " + page + " Size: " + size);

        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setVinyard(vinyard)
            .setName(name)
            .setCategory(category)
            .setCountry(country)
            .build();

        var count = wineService.countFindBy(searchDto);

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(wineMapper.wineToWineDetailsDto(wineService.findWines(searchDto, pageable)));
    }

    /**
     * Update a wine.
     *
     * @param wineDetailsDto The new wine details
     * @param authentication The logged-in user
     * @return Returns the updated wine if successful
     * @throws UserPermissionException      thrown, if the user triggering the request does not have the correct permissions
     * @throws ValidationException          Thrown if the updated data is invalid
     * @throws ObjectAlreadyExistsException Thrown if the object already exists
     */
    @Secured("ROLE_USER")
    @PostMapping("/{id}")
    @Operation(summary = "Update a wine by its id", security = @SecurityRequirement(name = "apiKey"))
    public WineDetailsDto update(@Valid @RequestBody WineDetailsDto wineDetailsDto, Authentication authentication)
        throws UserPermissionException,
        ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.trace("update({}, {})", wineDetailsDto, authentication);
        var result = wineService.updateWine(wineDetailsDto, authentication.getName());
        return wineMapper.wineToWineDetailsDto(result);
    }

    /**
     * Create a new wine.
     *
     * @param wineCreateDto  The new wine's data
     * @param authentication The user triggering the request
     * @throws ValidationException          Thrown if the data is invalid
     * @throws ObjectAlreadyExistsException Thrown if the object already exists.
     */
    @Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new wine", security = @SecurityRequirement(name = "apiKey"))
    public void create(@Valid @RequestBody WineCreateDto wineCreateDto, Authentication authentication)
        throws ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.trace("create({}, {})", wineCreateDto, authentication);
        wineService.createWine(wineCreateDto, authentication.getName());
    }

    /**
     * Find all wines withing the page limit.
     *
     * @param page The page to look for. Default = 0
     * @param size The size of the page. Default = 25
     * @return Returns a list of wines at the requested page
     */
    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get all wines by page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<WineDetailsDto>> findAll(
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size) {
        LOGGER.trace("findAll({}, {}, {}, {}, {}, {})", page, size);        
        Pageable pageable = PageRequest.of(page, size);

        var count = wineService.countAll();

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(wineMapper.wineToWineDetailsDto(wineService.getWines(pageable)));
    }

    /**
     * Delete the specified wine.
     *
     * @param id             The id of the wine to delete
     * @param authentication The user triggering the request
     * @throws UserPermissionException Thrown if the user does not have the correct permissions for the wine
     */
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete the wine specified by the id", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable Long id, Authentication authentication) throws UserPermissionException {
        LOGGER.trace("delete({}, {})", id, authentication);
        wineService.delete(id, authentication.getName());
    }
}
