package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for wine types.
 */
@Repository
public interface WineRepository extends JpaRepository<Wine, Long> {
    Optional<Wine> findById(Long id);

    List<Wine> findByCategory(WineCategory category);

    List<Wine> findByVinyardContaining(String vinyard);

    List<Wine> findByNameContaining(String name);

    /**
     * Find all wines with matching attributes. Null values are ignored.
     *
     * @param name     The name of the wine
     * @param vinyard  The vinyard the wine is from
     * @param category The category of the wine
     * @param country  The country the wine is from
     * @param pageable The page requested
     * @return Returns a list of wines matching the requested data
     */
    @Query("SELECT w FROM Wine w WHERE (UPPER(w.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null) "
        + "AND (UPPER(w.vinyard) LIKE UPPER(CONCAT('%', :vinyard, '%')) OR :vinyard IS null) "
        + "AND (w.category = :category OR :category IS null) "
        + "AND (UPPER(w.country) = UPPER(:country) OR :country IS null)")
    Page<Wine> find(
        @Param("name") String name,
        @Param("vinyard") String vinyard,
        @Param("category") WineCategory category,
        @Param("country") String country,
        Pageable pageable
    );

    /**
     * Find the number of wines with matching attributes. Null values are ignored.
     *
     * @param name     The name of the wine
     * @param vinyard  The vinyard the wine is from
     * @param category The category of the wine
     * @param country  The country the wine is from
     * @return Returns a long value of the number of elements available for the specified search parameters
     */
    @Query("SELECT COUNT(w) FROM Wine w WHERE (UPPER(w.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null) "
        + "AND (UPPER(w.vinyard) LIKE UPPER(CONCAT('%', :vinyard, '%')) OR :vinyard IS null) "
        + "AND (w.category = :category OR :category IS null) "
        + "AND (UPPER(w.country) = UPPER(:country) OR :country IS null)")
    long countOfFind(
        @Param("name") String name,
        @Param("vinyard") String vinyard,
        @Param("category") WineCategory category,
        @Param("country") String country
    );

    List<Wine> findAll();

    Page<Wine> findAll(Pageable pageable);

    void deleteById(Long id);
}
