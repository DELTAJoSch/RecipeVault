package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationOptionMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ApplicationOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Endpoint for handling application level options like feature toggles, etc.
 */
@RestController
@RequestMapping(value = "/api/v1/options")
@Validated
public class ApplicationOptionEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ApplicationOptionService applicationOptionService;
    private final ApplicationOptionMapper applicationOptionMapper;

    @Autowired
    public ApplicationOptionEndpoint(ApplicationOptionService applicationOptionService, ApplicationOptionMapper applicationOptionMapper) {
        this.applicationOptionService = applicationOptionService;
        this.applicationOptionMapper = applicationOptionMapper;
    }

    /**
     * Update an option.
     *
     * @param applicationOptionDto The updated option
     * @param id                   The id of the option to update
     * @return Returns the updated option
     * @throws ValidationException Thrown if the option is invalid
     * @throws NotFoundException   Thrown if the option is not found
     */
    @Secured("ROLE_ADMIN")
    @PostMapping("/{id}")
    @Operation(summary = "Update an application option by its id", security = @SecurityRequirement(name = "apiKey"))
    public ApplicationOptionDto update(@Valid @RequestBody ApplicationOptionDto applicationOptionDto, @PathVariable Long id)
        throws ValidationException,
        NotFoundException {
        var result = applicationOptionService.update(applicationOptionDto, id);
        return applicationOptionMapper.applicationOptionToApplicationOptionDto(result);
    }

    /**
     * Find all application options withing the page limit.
     *
     * @param page The page to look for. Default = 0
     * @param size The size of the page. Default = 25
     * @return Returns a list of application options at the requested page
     */
    @Secured("ROLE_ADMIN")
    @GetMapping
    @Operation(summary = "Get all application options by page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<ApplicationOptionDto>> findLike(
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") 
        @Positive(message = "Seitengröße muss positiv sein!") int size,
        @RequestParam(required = false) @Size(max = 255, message = "Suchstring darf nicht länger als 255 Zeichen sein") String name
    ) throws ValidationException {
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: " + page + " Size: " + size);

        if (name != null && name.length() > 255) {
            throw new ValidationException("Name zu lang.");
        }

        var count = applicationOptionService.countAll(name);
        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(applicationOptionMapper.applicationOptionToApplicationOptionDto(applicationOptionService.findLike(name, pageable)));
    }
}
