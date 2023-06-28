package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import java.util.Objects;

/**
 * Key for Amount entity.
 */
@Embeddable
public class AmountKey implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "recipe", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ingredient", nullable = false)
    private Ingredient ingredient;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AmountKey amountKey = (AmountKey) o;
        return Objects.equals(getRecipe(), amountKey.getRecipe()) && Objects.equals(getIngredient(), amountKey.getIngredient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipe(), getIngredient());
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public static final class AmountKeyBuilder {
        private Recipe recipe;
        private Ingredient ingredient;

        public AmountKeyBuilder withRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public AmountKeyBuilder withIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public AmountKey build() {
            AmountKey key = new AmountKey();
            key.ingredient = this.ingredient;
            key.recipe = this.recipe;
            return key;
        }
    }
}