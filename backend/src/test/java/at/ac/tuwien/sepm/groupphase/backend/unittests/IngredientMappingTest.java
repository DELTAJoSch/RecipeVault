package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.IngredientTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.IngredientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class IngredientMappingTest implements IngredientTestData {

    private final Ingredient INGREDIENT = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.CHEESE_CREAM)
        .setName(NAME)
        .build();
    @Autowired
    private IngredientMapper ingredientMapper;

    @Test
    public void givenNothing_mappingIngredientToIngredientDto_entityHasAllProperties() {
        IngredientCreateDto ingredientCreateDto = this.ingredientMapper.ingredientToIngredientCreateDto(INGREDIENT);

        assertAll(
            () -> assertEquals(NAME, ingredientCreateDto.getName()),
            () -> assertEquals(IngredientMatchingCategory.CHEESE_CREAM, ingredientCreateDto.getCategory())
        );
    }

    @Test
    public void givenNothing_mapListWithTwoIngredientEntitiesToDto_getListWithSizeTwoAndAllProperties() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(INGREDIENT);
        ingredients.add(INGREDIENT);

        List<IngredientCreateDto> ingredientCreateDtos = this.ingredientMapper.ingredientToIngredientCreateDto(ingredients);
        assertEquals(2, ingredientCreateDtos.size());

        IngredientCreateDto ingredientCreateDto = ingredientCreateDtos.get(0);
        assertAll(
            () -> assertEquals(NAME, ingredientCreateDto.getName()),
            () -> assertEquals(IngredientMatchingCategory.CHEESE_CREAM, ingredientCreateDto.getCategory())
        );
    }

}

