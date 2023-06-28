package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Favorite;
import at.ac.tuwien.sepm.groupphase.backend.entity.FavoriteCompositeKey;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteCompositeKey> {
    /**
     * Get the favorite of a user and recipe.
     *
     * @param userId   id of user
     * @param recipeId id of recipe
     * @return favorite recipe with the given id that belongs to the given user
     */
    Optional<Favorite> findByUserIdAndRecipeId(Long userId, Long recipeId);

    /**
     * Get the number of favorites of a user.
     *
     * @param id of user
     * @return number of favorites
     */
    Long countByUserId(Long id);

    /**
     * get the favorites of a user.
     *
     * @param id       of user
     * @param pageable the page requested
     * @return favorites of user for the page
     */
    @Query("SELECT r FROM Recipe r, Favorite f WHERE (f.recipe.id = r.id) AND (f.user.id = :id)")
    List<Recipe> getFavorites(@Param("id") Long id, Pageable pageable);


}
