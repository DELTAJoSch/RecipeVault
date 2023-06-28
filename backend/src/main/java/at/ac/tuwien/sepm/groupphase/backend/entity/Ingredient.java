package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    /**
     * The ID of the ingredient.
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * The name of the ingredient.
     */
    @Column(name = "name", unique = true)
    private String name;

    /**
     * The amounts to which this ingredient is linked.
     */
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private List<Amount> amounts;

    /**
     * The category of the ingredient.
     */
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private IngredientMatchingCategory category;


    public Ingredient() {
    }

    public Ingredient(String name) {
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
        Ingredient that = (Ingredient) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && getCategory() == that.getCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCategory());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Amount> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<Amount> amounts) {
        this.amounts = amounts;
    }

    public IngredientMatchingCategory getCategory() {
        return category;
    }

    public void setCategory(IngredientMatchingCategory category) {
        this.category = category;
    }

    public static final class IngredientBuilder {
        private String name;
        private IngredientMatchingCategory category;

        private Long id;

        public Ingredient.IngredientBuilder setId(Long id) {
            this.id = id;
            return this;
        }


        public Ingredient.IngredientBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Ingredient.IngredientBuilder setCategory(IngredientMatchingCategory category) {
            this.category = category;
            return this;
        }

        public Ingredient build() {
            Ingredient ingredient = new Ingredient();
            ingredient.name = this.name;
            ingredient.category = this.category;
            ingredient.id = this.id;
            return ingredient;
        }
    }
}
