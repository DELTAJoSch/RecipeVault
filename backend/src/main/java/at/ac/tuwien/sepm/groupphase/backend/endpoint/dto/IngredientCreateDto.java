package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This DTO contains all information to create a new ingredient.
 */
public class IngredientCreateDto {

    @NotNull(message = "darf nicht null sein")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    @NotBlank(message = "darf nicht leer sein")
    private String name;

    @NotNull(message = "darf nicht null sein")
    private IngredientMatchingCategory category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IngredientMatchingCategory getCategory() {
        return category;
    }

    public void setCategory(IngredientMatchingCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "IngredientDto: {"
            + "name: " + name
            + "category: " + category
            + "}";
    }

    public static final class IngredientCreateDtoBuilder {
        private String name;
        private IngredientMatchingCategory category;

        public IngredientCreateDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public IngredientCreateDtoBuilder withCategory(IngredientMatchingCategory category) {
            this.category = category;
            return this;
        }

        /**
         * Builds the IngredientCreateDto with the specified information.
         *
         * @return Returns a new IngredientCreateDto
         */
        public IngredientCreateDto build() {
            IngredientCreateDto ingredient = new IngredientCreateDto();
            ingredient.name = this.name;
            ingredient.category = this.category;
            return ingredient;
        }
    }

}

