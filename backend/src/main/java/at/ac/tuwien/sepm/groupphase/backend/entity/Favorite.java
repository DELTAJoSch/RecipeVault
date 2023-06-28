package at.ac.tuwien.sepm.groupphase.backend.entity;



import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
@IdClass(FavoriteCompositeKey.class)
@Table(name = "favorites", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "recipeId"})})
public class Favorite {
    /**
     * The ID of the user.
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private ApplicationUser user;

    /**
     * The ID of the as favorite marked recipe.
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    public Favorite() {

    }

    public Favorite(ApplicationUser user, Recipe recipe) {
        this.user = user;
        this.recipe = recipe;
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

    public static final class FavoriteBuilder {
        private Recipe recipe;
        private ApplicationUser user;

        public FavoriteBuilder() {
        }

        public FavoriteBuilder withRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public FavoriteBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }


        public Favorite build() {
            Favorite favorite = new Favorite();
            favorite.user = this.user;
            favorite.recipe = this.recipe;
            return favorite;
        }

    }

}
