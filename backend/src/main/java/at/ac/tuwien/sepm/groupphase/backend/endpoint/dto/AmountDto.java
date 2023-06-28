package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Dto to transfer an Amount of an Ingredient.
 */
public class AmountDto {

    @NotNull(message = "darf nicht null sein")
    private IngredientDetailsDto ingredient;

    @NotNull(message = "darf nicht null sein")
    private Double amount;

    @NotNull(message = "darf nicht null sein")
    private AmountUnit unit;

    public IngredientDetailsDto getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientDetailsDto ingredient) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AmountDto amountDto = (AmountDto) o;
        return Objects.equals(ingredient, amountDto.ingredient) && Objects.equals(amount, amountDto.amount) && unit == amountDto.unit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredient, amount, unit);
    }

    public static final class AmountDtoBuilder {
        private IngredientDetailsDto ingredient;

        private Double amount;

        private AmountUnit unit;

        public AmountDtoBuilder withIngredient(IngredientDetailsDto ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public AmountDtoBuilder withAmount(Double amount) {
            this.amount = amount;
            return this;
        }

        public AmountDtoBuilder withUnit(AmountUnit unit) {
            this.unit = unit;
            return this;
        }

        public AmountDto build() {
            var dto = new AmountDto();
            dto.amount = this.amount;
            dto.ingredient = this.ingredient;
            dto.unit = this.unit;

            return dto;
        }
    }
}
