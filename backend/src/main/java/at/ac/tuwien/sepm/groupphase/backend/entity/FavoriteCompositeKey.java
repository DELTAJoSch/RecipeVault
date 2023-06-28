package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Key for the table "Favorites", it includes a user id and a recipe id.
 */
public class FavoriteCompositeKey implements Serializable {
    private ApplicationUser user;
    private Recipe recipe;

    public FavoriteCompositeKey() {
    }

    public FavoriteCompositeKey(ApplicationUser user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteCompositeKey that = (FavoriteCompositeKey) o;
        return Objects.equals(user, that.user) && Objects.equals(recipe, that.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, recipe);
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
