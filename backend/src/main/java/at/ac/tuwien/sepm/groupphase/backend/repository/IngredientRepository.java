package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
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
 * Repository for ingredients.
 */
@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    /**
     * Find specific ingredient by its name.
     *
     * @param name the name of the ingredient to get
     * @return the ingredient with the specified name
     */
    Optional<Ingredient> findByName(String name);

    /**
     * Find specific ingredient by its id.
     *
     * @param id the id of the ingredient to get
     * @return the ingredient with the specified id
     */
    Optional<Ingredient> findById(Long id);

    /**
     * Find at most ten ingredients which names contain a specific string.
     *
     * @param name the (part of the) name of the ingredients to get
     * @return at most ten ingredients that match the name
     */
    List<Ingredient> findTop10ByNameContaining(String name);

    /**
     * Find an ingredient which name contains a specific string.
     *
     * @param name the (part of the) name of the ingredient to get
     * @return ingredient that matches the name
     */
    List<Ingredient> findByNameContaining(String name);

    /**
     * Find all ingredients with matching attributes. Null values are ignored.
     *
     * @param name     The name of the ingredient
     * @param category The category of the ingredient
     * @param pageable The page requested
     * @return Returns a list of ingredients matching the requested data
     */
    @Query("SELECT i FROM Ingredient i WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null) "
        + "AND (i.category = :category OR :category IS null) ")
    Page<Ingredient> find(
        @Param("name") String name,
        @Param("category") IngredientMatchingCategory category,
        Pageable pageable
    );

    /**
     * Find the number of ingredients with matching attributes. Null values are ignored.
     *
     * @param name     The name of the ingredient
     * @param category The category of the ingredient
     * @return Returns a long value of the number of elements available for the specified search parameters
     */
    @Query("SELECT COUNT(i) FROM Ingredient i WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', :name, '%')) OR :name IS null) "
        + "AND (i.category = :category OR :category IS null) ")
    long countOfFind(
        @Param("name") String name,
        @Param("category") IngredientMatchingCategory category);

    /**
     * Deletes the ingredient with a specific id.
     *
     * @param id the id of the ingredient to delete
     */
    void deleteById(Long id);
}

