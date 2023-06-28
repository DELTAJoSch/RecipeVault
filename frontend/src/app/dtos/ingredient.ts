/**
 * This class represents an ingredient dto
 */
export class Ingredient {

  name: string;
  category: IngredientMatchingCategory;
  id: number;
}

/**
 * This enum contains all ingredient matching categories available to the application
 */
export enum IngredientMatchingCategory {
  cheeseCrumbly = 'CHEESE_CRUMBLY',
  cheeseIntense = 'CHEESE_INTENSE',
  cheeseCream = 'CHEESE_CREAM',
  cheeseDelicate = 'CHEESE_DELICATE',
  cheeseNutty = 'CHEESE_NUTTY',
  cheeseFruity = 'CHEESE_FRUITY',
  cheeseDrySalty = 'CHEESE_DRY_SALTY',
  crustaceans = 'CRUSTACEANS',
  shellfish = 'SHELLFISH',
  fish = 'FISH',
  meatWhite = 'MEAT_WHITE',
  meatRed = 'MEAT_RED',
  meatCured = 'MEAT_CURED',
  marinadeIntense = 'MARINADE_INTENSE',
  vegetableCruciferous = 'VEGETABLE_CRUCIFEROUS', //Kreuzblütler for Germans!
  vegetableGreen = 'VEGETABLE_GREEN',
  vegetableRoots = 'VEGETABLE_ROOTS',
  vegetableBulbous = 'VEGETABLE_BULBOUS', //Zwiebelgewächse for Germans!
  vegetableNightShadow = 'VEGETABLE_NIGHT_SHADOW',
  vegetableLegumes = 'VEGETABLE_LEGUMES',
  vegetableMushrooms = 'VEGETABLE_MUSHROOMS',
  herbsAromatic = 'HERBS_AROMATIC',
  herbsDry = 'HERBS_DRY',
  herbsResinous = 'HERBS_RESINOUS',
  herbsExotic = 'HERBS_EXOTIC',
  herbsBaking = 'HERBS_BAKING',
  herbsUmami = 'HERBS_UMAMI',
  peppersChillis = 'PEPPERS_CHILLIS',
  undefined = 'UNDEFINED'
}
