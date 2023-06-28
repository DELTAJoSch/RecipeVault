package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

/**
 * This mapper maps ingredients to ingredient entities.
 */
@Mapper
public interface IngredientMapper {

    @Named("ingredientDetails")
    IngredientCreateDto ingredientToIngredientCreateDto(Ingredient ingredient);

    @IterableMapping(qualifiedByName = "ingredientDetails")
    List<IngredientCreateDto> ingredientToIngredientCreateDto(List<Ingredient> ingredients);

    @Named("ingredientDetails")
    IngredientDetailsDto ingredientToIngredientDetailsDto(Ingredient ingredient);

    @IterableMapping(qualifiedByName = "ingredientDetails")
    List<IngredientDetailsDto> ingredientToIngredientDetailsDto(List<Ingredient> ingredients);
}
