package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for recipes.
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findById(Long id);

    List<Recipe> findByDifficulty(Difficulty difficulty);

    List<Recipe> findByNameContaining(String name);

    List<Recipe> findByAuthorId(Long id);

    Optional<Recipe> findByName(String name);


    /**
     * Find all recipes with matching attributes. Null values are ignored.
     *
     * @param name       The name of the recipe
     * @param difficulty The recipe of the recipe
     * @param pageable   The page requested
     * @return Returns a list of recipes matching the requested data
     */
    @Query("SELECT r FROM Recipe r WHERE (UPPER(r.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null) "
        + "AND (r.difficulty = :difficulty OR :difficulty IS null) ")
    Page<Recipe> find(
        @Param("name") String name,
        @Param("difficulty") Difficulty difficulty,
        Pageable pageable
    );

    /**
     * Find the number of recipes with matching attributes. Null values are ignored.
     *
     * @param name       The name of the recipe
     * @param difficulty The recipe of the recipe
     * @return Returns a long value of the number of elements available for the specified search parameters
     */
    @Query("SELECT COUNT(r) FROM Recipe r WHERE (UPPER(r.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null) "
        + "AND (r.difficulty = :difficulty OR :difficulty IS null) ")
    long countOfFind(
        @Param("name") String name,
        @Param("difficulty") Difficulty difficulty
    );


    List<Recipe> findAll();

    Page<Recipe> findAll(Pageable pageable);

    void deleteById(Long id);

    void deleteAll();

}
