package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;

public interface WineRecommendationEngineTestData {
    Ingredient INGREDIENT_MEAT_WHITE = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.MEAT_WHITE)
        .setName("WHITE MEAT")
        .build();

    Ingredient INGREDIENT_MEAT_RED = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.MEAT_RED)
        .setName("RED MEAT")
        .build();

    Ingredient INGREDIENT_UNDEFINED = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.UNDEFINED)
        .setName("UNDEFINED")
        .build();

    Ingredient INGREDIENT_HERB_AROMATIC = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.HERBS_AROMATIC)
        .setName("HERBS AROMATIC")
        .build();

    Ingredient INGREDIENT_FISH = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.FISH)
        .setName("FISH")
        .build();

    Ingredient INGREDIENT_MARINADE = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.MARINADE_INTENSE)
        .setName("MARINADE")
        .build();

    Ingredient INGREDIENT_NIGHT_SHADOW = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.VEGETABLE_NIGHT_SHADOW)
        .setName("NIGHT SHADOW")
        .build();

    Amount AM_100G = new Amount.AmountBuilder()
        .setAmount(100.0)
        .setUnit(AmountUnit.G)
        .build();

    Amount AM_1CUP = new Amount.AmountBuilder()
        .setAmount(1.0)
        .setUnit(AmountUnit.CUP)
        .build();

    Amount AM_1KG = new Amount.AmountBuilder()
        .setAmount(1.0)
        .setUnit(AmountUnit.KG)
        .build();

    Amount AM_10TSP = new Amount.AmountBuilder()
        .setAmount(10.0)
        .setUnit(AmountUnit.TSP)
        .build();

    Amount AM_2LB = new Amount.AmountBuilder()
        .setAmount(1.0)
        .setUnit(AmountUnit.LB)
        .build();
}
