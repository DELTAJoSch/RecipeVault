package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import at.ac.tuwien.sepm.groupphase.backend.exception.DeletionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * This service contains everthing related to the management of authors.
 */
public interface AuthorService {
    /**
     * Fetch an author by its id.
     *
     * @param id The id of the author to look for.
     * @return Returns the Author
     * @throws NotFoundException If the author could not be found
     */
    Author getAuthorById(Long id) throws NotFoundException;

    /**
     * Fetch all authors for the specified page.
     *
     * @param pageable The page to get
     * @return Returns a list of all authors.
     */
    List<Author> getAuthors(Pageable pageable);

    /**
     * Count all authors in the table.
     *
     * @return Returns the number of authors in the database
     */
    long countAll();

    /**
     * Create a new author with the given data.
     *
     * @param author The author to create.
     * @throws ValidationException          Thrown if the validation of the createDto fails.
     * @throws ObjectAlreadyExistsException Thrown if an author with same first- and lastname already exists.
     */
    void createAuthor(AuthorCreateDto author) throws ValidationException, ObjectAlreadyExistsException;

    /**
     * Updates author with the given data (based on id).
     *
     * @param author The author to update
     * @throws ValidationException          Thrown if the validation of the data fails.
     * @throws ObjectAlreadyExistsException Thrown if an author with same first- and lastname already exists.
     */
    Author updateAuthor(AuthorDetailsDto author) throws ValidationException, ObjectAlreadyExistsException;

    /**
     * Deletes author with given id when called by an admin user.
     *
     * @param id       ID of the author to be deleted.
     * @param username Username of the issuing user.
     * @throws NotFoundException       Thrown if the admin with given id could not be found.
     * @throws UserPermissionException Thrown if issuing user could not be found.
     * @throws DeletionException       Thrown if author to delete is currently in use.
     */
    void deleteAuthor(Long id, String username) throws NotFoundException, UserPermissionException, DeletionException;

    /**
     * Fetch first 10 authors by their name.
     *
     * @param searchParameters The name of the author to look for.
     * @return Returns the authors
     * @throws NotFoundException If the author could not be found
     */
    List<Author> search(String searchParameters);

}
