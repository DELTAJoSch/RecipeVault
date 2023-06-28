package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * This dto contains information about the results of an ocr execution.
 */
public class OcrRecipeDto {

    @NotNull(message = "Recipe description must not be null")
    private String description;

    private List<AmountDto> ingredients;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AmountDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<AmountDto> ingredients) {
        this.ingredients = ingredients;
    }

    public static final class OcrRecipeDtoBuilder {
        private String description;
        private List<AmountDto> ingredients;

        public OcrRecipeDtoBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public OcrRecipeDtoBuilder setIngredients(List<AmountDto> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public OcrRecipeDto build() {
            OcrRecipeDto recipe = new OcrRecipeDto();
            recipe.description = this.description;
            recipe.ingredients = this.ingredients;
            return recipe;
        }
    }

}
