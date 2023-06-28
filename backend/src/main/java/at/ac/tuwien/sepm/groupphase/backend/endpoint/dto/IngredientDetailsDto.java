package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * This dto contains information about an ingredient.
 */
public class IngredientDetailsDto {

    @NotNull(message = "darf nicht null sein")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    @NotBlank(message = "darf nicht leer sein")
    private String name;

    @NotNull(message = "darf nicht null sein")
    private IngredientMatchingCategory category;

    @NotNull(message = "darf nicht null sein")
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IngredientDetailsDto that = (IngredientDetailsDto) o;
        return Objects.equals(name, that.name) && category == that.category && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, id);
    }

    public static final class IngredientDetailsDtoBuilder {
        private Long id;
        private String name;
        private IngredientMatchingCategory category;

        public IngredientDetailsDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public IngredientDetailsDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public IngredientDetailsDtoBuilder withCategory(IngredientMatchingCategory category) {
            this.category = category;
            return this;
        }

        public IngredientDetailsDto build() {
            var dto = new IngredientDetailsDto();
            dto.category = this.category;
            dto.id = this.id;
            dto.name = this.name;

            return dto;
        }
    }
}
