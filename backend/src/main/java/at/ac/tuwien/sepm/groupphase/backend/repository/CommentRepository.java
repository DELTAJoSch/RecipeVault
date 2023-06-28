package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import at.ac.tuwien.sepm.groupphase.backend.entity.CommentPrimaryKey;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for comments of recipes.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, CommentPrimaryKey> {

    /**
     * Get all comments of a recipe.
     *
     * @param recipe the recipe to get the comments of
     * @return list of comments
     */
    public List<Comment> findByRecipe(Recipe recipe);

}
