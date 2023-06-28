package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.RecipeTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
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
public class RecipeMappingTest implements RecipeTestData {

    private final Recipe RECIPE = new Recipe.RecipeBuilder()
        .setDifficulty(DIFFICULTY)
        .setName(NAME)
        .setDescription(DESCRIPTION)
        .setId(ID)
        .setIngredients(INGREDIENTS)
        .setOwner(USER)
        .setConfidence(CONFIDENCE)
        .setRecommendedCategory(WINE_CATEGORY)
        .build();
    @Autowired
    private RecipeMapper recipeMapper;

    @Test
    public void givenNothing_whenMappingRecipeToRecipeDetailsDto_thenEntityHasAllProperties() {
        RecipeDetailsDto recipeDetailsDto = this.recipeMapper.recipeToRecipeDetailsDto(RECIPE);

        UserInfoDto userInfoDto = new UserInfoDto.UserInfoDtoBuilder()
            .withId(USER.getId())
            .withName(USER.getEmail())
            .build();

        assertAll(
            () -> assertEquals(ID, recipeDetailsDto.getId()),
            () -> assertEquals(NAME, recipeDetailsDto.getName()),
            () -> assertEquals(DESCRIPTION, recipeDetailsDto.getDescription()),
            () -> assertEquals(AMOUNT_DTOS, recipeDetailsDto.getIngredients()),
            () -> assertEquals(userInfoDto, recipeDetailsDto.getOwner()),
            () -> assertEquals(DIFFICULTY, recipeDetailsDto.getDifficulty()),
            () -> assertEquals(CONFIDENCE, recipeDetailsDto.getRecommendationConfidence()),
            () -> assertEquals(WINE_CATEGORY, recipeDetailsDto.getRecommendedCategory())
        );
    }

    @Test
    public void givenNothing_whenMapListWithTwoWineEntitiesToDetailsDto_thenGetListWithSizeTwoAndAllProperties() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(RECIPE);
        recipes.add(RECIPE);

        UserInfoDto userInfoDto = new UserInfoDto.UserInfoDtoBuilder()
            .withId(USER.getId())
            .withName(USER.getEmail())
            .build();

        List<RecipeDetailsDto> recipeDetailsDtos = this.recipeMapper.recipeToRecipeDetailsDto(recipes);
        assertEquals(2, recipeDetailsDtos.size());

        RecipeDetailsDto recipeDetailsDto = recipeDetailsDtos.get(0);
        assertAll(
            () -> assertEquals(ID, recipeDetailsDto.getId()),
            () -> assertEquals(NAME, recipeDetailsDto.getName()),
            () -> assertEquals(DESCRIPTION, recipeDetailsDto.getDescription()),
            () -> assertEquals(AMOUNT_DTOS, recipeDetailsDto.getIngredients()),
            () -> assertEquals(userInfoDto, recipeDetailsDto.getOwner()),
            () -> assertEquals(DIFFICULTY, recipeDetailsDto.getDifficulty()),
            () -> assertEquals(CONFIDENCE, recipeDetailsDto.getRecommendationConfidence()),
            () -> assertEquals(WINE_CATEGORY, recipeDetailsDto.getRecommendedCategory())
        );
    }

}

