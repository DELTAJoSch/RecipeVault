package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for authors.
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Find specific author by its id.
     *
     * @param id the id of the author to get
     * @return the author with the specified id
     */
    Optional<Author> findById(Long id);

    /**
     * Find specific author by its first- and lastname.
     *
     * @param firstname the firstname of the author to get
     * @param lastname  the lastname of the author to get
     * @return the author with the specified name
     */
    Optional<Author> findByFirstnameAndLastname(String firstname, String lastname);

    /**
     * Find all authors.
     *
     * @return a list of all authors
     */
    List<Author> findAll();

    /**
     * Find all authors with matching attributes.
     *
     * @param pageable The requested page.
     * @return Returns a list of authors matching the requested page
     */
    Page<Author> findAll(Pageable pageable);

    /**
     * Finds at most ten authors which names (first-and lastname) contain a specific string.
     *
     * @param name (part of the) name of the author to find
     * @return at most ten authors that match the name
     */
    @Query(nativeQuery = true, value = "SELECT * FROM authors a "
        + "WHERE (CONCAT(UPPER(a.firstname), ' ', UPPER(a.lastname)) LIKE UPPER(CONCAT('%', :name, '%'))) "
        + "ORDER BY a.firstname DESC LIMIT 10")
    List<Author> findTop10ByName(@Param("name") String name);

}
