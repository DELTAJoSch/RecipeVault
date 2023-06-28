package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeListCompositeKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for recipe lists of users.
 */

@Repository
public interface ListRepository extends JpaRepository<RecipeList, RecipeListCompositeKey> {
    Optional<RecipeList> findByUserIdAndName(Long userId, String name);

    List<RecipeList> getAllByUserId(Long userId);


    /**
     * Get all recipes of a certain list.
     *
     * @param name     of list
     * @param userId   id of user.
     * @param pageable the requested page
     * @return list of recipes
     */
    @Query(value = "SELECT l.recipes FROM RecipeList l WHERE (l.name = :name) AND (l.user.id = :userId)")
    List<Recipe> getRecipesByNameAndUserId(@Param("name") String name, @Param("userId") Long userId, Pageable pageable);

    /**
     * Get all recipes of a certain list.
     *
     * @param name   of list
     * @param userId id of user.
     * @return list of recipes
     */
    @Query(value = "SELECT l.recipes FROM RecipeList l WHERE (l.name = :name) AND (l.user.id = :userId)")
    List<Recipe> getRecipesByNameAndUserId(@Param("name") String name, @Param("userId") Long userId);
    

}
