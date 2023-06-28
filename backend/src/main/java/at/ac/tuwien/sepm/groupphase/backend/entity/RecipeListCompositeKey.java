package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite Key for the table "RecipeList", it includes a user id, a recipe id and a name.
 */
public class RecipeListCompositeKey implements Serializable {
    private ApplicationUser user;
    private String name;

    public RecipeListCompositeKey() {
    }

    public RecipeListCompositeKey(ApplicationUser user, String name) {
        this.user = user;
        this.name = name;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeListCompositeKey that = (RecipeListCompositeKey) o;
        return Objects.equals(user, that.user) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, name);
    }
}
