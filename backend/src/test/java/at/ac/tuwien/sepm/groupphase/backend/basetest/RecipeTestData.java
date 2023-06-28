package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty.EASY;

/**
 * Contains Data for Recipe tests.
 */
public interface RecipeTestData {
    Long ID = 1L;
    String NAME = "TestRECIPE";
    String DESCRIPTION = "description XYZ";
    Difficulty DIFFICULTY = EASY;
    WineCategory WINE_CATEGORY = WineCategory.ROSE;
    Double CONFIDENCE = 1.5;


    Ingredient TEST_INGREDIENT = new Ingredient.IngredientBuilder()
        .setName("test ingredient one")
        .setId(-1L)
        .setCategory(IngredientMatchingCategory.CHEESE_CREAM)
        .build();

    IngredientDetailsDto TEST_INGREDIENT_DTO = new IngredientDetailsDto.IngredientDetailsDtoBuilder()
        .withName("test ingredient one")
        .withId(-1L)
        .withCategory(IngredientMatchingCategory.CHEESE_CREAM)
        .build();

    ApplicationUser OWNER = new ApplicationUser.ApplicationUserBuilder()
        .setId(-42L)
        .setAdmin(true)
        .setEmail("test@12122392.xyz")
        .setPassword("unused")
        .build();

    Amount AMOUNT = new Amount.AmountBuilder()
        .setRecipe(null)
        .setIngredient(TEST_INGREDIENT)
        .setAmount(1.0)
        .setUnit(AmountUnit.KG)
        .build();
    List<Amount> INGREDIENTS = new ArrayList<Amount>(Collections.singleton(AMOUNT));
    Amount COMPLETE_AMOUNT = new Amount.AmountBuilder()
        .setRecipe(null)
        .setIngredient(TEST_INGREDIENT)
        .setAmount(1.0)
        .setUnit(AmountUnit.KG)
        .build();
    List<Amount> COMPLETE_INGREDIENT = new ArrayList<Amount>(Collections.singleton(COMPLETE_AMOUNT));
    AmountDto COMPLETE_AMOUNT_DTO = new AmountDto.AmountDtoBuilder()
        .withIngredient(TEST_INGREDIENT_DTO)
        .withAmount(1.0)
        .withUnit(AmountUnit.KG)
        .build();
    List<AmountDto> AMOUNT_DTOS = new ArrayList<AmountDto>(Collections.singleton(COMPLETE_AMOUNT_DTO));
    Amount NO_ID_AMOUNT = new Amount.AmountBuilder()
        .setRecipe(null)
        .setIngredient(TEST_INGREDIENT)
        .setUnit(AmountUnit.KG)
        .setAmount(1.0)
        .build();
    List<Amount> NO_ID_INGREDIENT = new ArrayList<Amount>(Collections.singleton(NO_ID_AMOUNT));


    String BASE_URI = "/api/v1";
    String RECIPE_MANAGEMENT_BASE_URI = BASE_URI + "/recipe";


    ApplicationUser USER = new ApplicationUser.ApplicationUserBuilder()
        .setEmail("test@12122392.xyz")
        .setAdmin(true)
        .setPassword("test")
        .setId(-42L)
        .build();


    String ADMIN_USER = "test@12122392.xyz";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "test.three@12122392.xyz";
    List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

}
