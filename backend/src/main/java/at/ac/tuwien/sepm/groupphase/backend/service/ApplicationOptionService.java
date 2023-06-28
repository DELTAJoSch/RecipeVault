package at.ac.tuwien.sepm.groupphase.backend.service;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This service contains everything relating to global application options that are changeable in the ui.
 */
public interface ApplicationOptionService {
    /**
     * Get all options at the specified page.
     *
     * @param pageable The page to look for
     * @return Returns a list of options at the specified page
     */
    List<ApplicationOption> findAll(Pageable pageable);

    /**
     * Get all options at the specified page.
     *
     * @param name     The name to look for
     * @param pageable The page to look for
     * @return Returns a list of options at the specified page
     * @throws ValidationException Thrown if the name is invalid
     */
    List<ApplicationOption> findLike(String name, Pageable pageable) throws ValidationException;

    /**
     * Finds a specific application option by its name.
     *
     * @param name The name to look for
     * @return Returns the applicationOption
     * @throws NotFoundException   Thrown if the ApplicationOption couldn't be found.
     * @throws ValidationException Thrown if the name is invalid.
     */
    ApplicationOption findByName(String name) throws NotFoundException, ValidationException;

    /**
     * Update a given applicationOption.
     *
     * @param option The new data of the option.
     * @param id     The id of the option to update.
     * @return Returns the updated option.
     * @throws ValidationException Thrown if the update is invalid.
     * @throws NotFoundException   Thrown if the option to update could not be found.
     */
    ApplicationOption update(ApplicationOptionDto option, Long id) throws ValidationException, NotFoundException;

    /**
     * Get the count of matching applicationOptions.
     *
     * @param name The name to look for
     * @return Returns the number of available application options matching the search parameter
     * @throws ValidationException Throws a ValidationException if the name is too long.
     */
    long countAll(String name) throws ValidationException;
}
