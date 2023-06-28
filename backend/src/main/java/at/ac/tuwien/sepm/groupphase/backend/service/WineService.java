package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This service contains everything related to the management (CRUD) of wines.
 */
public interface WineService {

    /**
     * Fetch a wine by its id.
     *
     * @param id The id of the wine to look for.
     * @return Returns the wine
     * @throws NotFoundException If the wine could not be found
     */
    Wine getWineById(Long id) throws NotFoundException;

    /**
     * Count all wines in the table.
     *
     * @return Returns the number of wines in the database
     */
    long countAll();


    /**
     * Count all wines matching the search parameters.
     *
     * @param searchDto The search parameters to look for
     * @return Returns the number of wines matching the search query.
     */
    long countFindBy(WineSearchDto searchDto) throws ValidationException;

    /**
     * Fetch all wines for the specified page and search.
     *
     * @param wineSearchDto The search parameters to look for
     * @param pageable      The pages to get
     * @return Returns the wines
     */
    List<Wine> findWines(WineSearchDto wineSearchDto, Pageable pageable) throws ValidationException;

    /**
     * Fetch all wines for the specified page.
     *
     * @param pageable The pages to get
     * @return Returns the wines
     */
    List<Wine> getWines(Pageable pageable);

    /**
     * Create a new wine with the calling user as the owner.
     *
     * @param wine     The wine to create
     * @param username The owner of the wine
     * @throws ValidationException          Thrown if the validation of the createDto failed
     * @throws ObjectAlreadyExistsException Thrown if a similar wine already exists.
     */
    void createWine(WineCreateDto wine, String username) throws ValidationException, ObjectAlreadyExistsException;

    /**
     * Updates the wine based on the specified information.
     *
     * @param updated  The update wine details
     * @param username The username of the user that called this action
     * @return Returns the updated wine
     * @throws NotFoundException            Thrown if the dto to update is not found
     * @throws UserPermissionException      Thrown if the user that executed this query is not the owner or an admin
     * @throws ValidationException          Thrown if the Details dto failed validation
     * @throws ObjectAlreadyExistsException Thrown if a similar wine already exists.
     */
    Wine updateWine(WineDetailsDto updated, String username) throws NotFoundException,
        UserPermissionException,
        ValidationException,
        ObjectAlreadyExistsException;

    /**
     * Deletes the wine.
     *
     * @param id       The id of the Wine
     * @param username The user who called this query
     * @throws NotFoundException       Thrown if the requested wine could not be found
     * @throws UserPermissionException Thrown if the user is not the owner or an admin
     */
    void delete(Long id, String username) throws NotFoundException, UserPermissionException;
}
