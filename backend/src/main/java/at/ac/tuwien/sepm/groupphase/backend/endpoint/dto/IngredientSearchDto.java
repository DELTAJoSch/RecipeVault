package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import jakarta.validation.constraints.Size;

/**
 * DTO to encapsulate parameters for Ingredient search.
 */
public class IngredientSearchDto {

    @NotNull(message = "Ingredient name darf nicht null sein")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String name;
    private IngredientMatchingCategory category;

    public IngredientSearchDto() {}

    public IngredientSearchDto(String name, IngredientMatchingCategory category) {
        this.name = name;
        this.category = category;
    }

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

    public static final class IngredientSearchDtoBuilder {

        private String name;
        private IngredientMatchingCategory category;

        public IngredientSearchDtoBuilder() {}

        public IngredientSearchDto.IngredientSearchDtoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public IngredientSearchDto.IngredientSearchDtoBuilder setCategory(IngredientMatchingCategory category) {
            this.category = category;
            return this;
        }

        public IngredientSearchDto build() {
            var dto = new IngredientSearchDto();
            dto.name = name;
            dto.category = category;

            return dto;
        }
    }
}
