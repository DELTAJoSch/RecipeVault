package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface to use with ApplicationOptions.
 */
@Repository
public interface ApplicationOptionRepository extends JpaRepository<ApplicationOption, Long> {
    /**
     * Find an application option by the name of the option.
     *
     * @param name The name of the option.
     * @return Returns an Optional of the application option.
     */
    Optional<ApplicationOption> findByName(String name);

    /**
     * Returns all options within a given page.
     *
     * @param pageable The page to look for.
     * @return Returns a list of options at the specified page.
     */
    Page<ApplicationOption> findAll(Pageable pageable);

    /**
     * Returns all options within a given page that match the name.
     *
     * @param name     The name to look for.
     * @param pageable The page to look for.
     * @return Returns a list of options at the specified page.
     */
    @Query("SELECT o FROM ApplicationOption o WHERE (UPPER(o.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null)")
    Page<ApplicationOption> findByNameContaining(@Param("name") String name, Pageable pageable);

    /**
     * Returns the number of options within a given page that match the name.
     *
     * @param name The name to look for.
     * @return Returns a number of options at the specified page.
     */
    @Query("SELECT COUNT(o) FROM ApplicationOption o WHERE (UPPER(o.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null)")
    long countByNameContaining(@Param("name") String name);
}
