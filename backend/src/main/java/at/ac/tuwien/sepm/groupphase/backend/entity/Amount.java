package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;

import java.util.Objects;

@Entity
@IdClass(AmountKey.class)
public class Amount {
    @Id
    @JoinColumn(name = "recipe", nullable = false)
    private Recipe recipe;

    @Id
    @JoinColumn(name = "ingredient", nullable = false)
    private Ingredient ingredient;

    @Column(name = "amount")
    private Double amount;

    @Enumerated(EnumType.STRING)
    private AmountUnit unit;

    public Amount() {
    }

    public Amount(Recipe recipe, Ingredient ingredient, double amount, AmountUnit unit) {
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.amount = amount;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Amount amount1 = (Amount) o;
        return Objects.equals(getRecipe(), amount1.getRecipe()) && Objects.equals(getIngredient(), amount1.getIngredient()) && Objects.equals(getAmount(), amount1.getAmount()) && getUnit() == amount1.getUnit();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getIngredient());
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AmountUnit getUnit() {
        return unit;
    }

    public void setUnit(AmountUnit unit) {
        this.unit = unit;
    }

    public static final class AmountBuilder {
        private Recipe recipe;
        private Ingredient ingredient;
        private Double amount;
        private AmountUnit unit;


        public AmountBuilder setRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public AmountBuilder setIngredient(Ingredient ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public AmountBuilder setAmount(Double amount) {
            this.amount = amount;
            return this;
        }

        public AmountBuilder setUnit(AmountUnit unit) {
            this.unit = unit;
            return this;
        }

        public Amount build() {
            Amount am = new Amount();
            am.recipe = this.recipe;
            am.ingredient = this.ingredient;
            am.amount = this.amount;
            am.unit = this.unit;
            return am;
        }
    }
}
