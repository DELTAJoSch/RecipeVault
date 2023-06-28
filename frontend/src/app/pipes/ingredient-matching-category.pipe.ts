import { Pipe, PipeTransform } from '@angular/core';
import {IngredientMatchingCategory} from '../dtos/ingredient';

@Pipe({
  name: 'ingredientMatchingCategory'
})
export class IngredientMatchingCategoryPipe implements PipeTransform {

  transform(value: IngredientMatchingCategory, ...args: unknown[]): string {
    switch(value) {
      case IngredientMatchingCategory.cheeseCrumbly: return 'Krümeliger Käse';
      case IngredientMatchingCategory.cheeseIntense: return 'Intensiver Käse';
      case IngredientMatchingCategory.cheeseCream: return 'Frischkäse';
      case IngredientMatchingCategory.cheeseDelicate: return 'Delikater Käse';
      case IngredientMatchingCategory.cheeseNutty: return 'Nussiger Käse';
      case IngredientMatchingCategory.cheeseFruity: return 'Fruchtiger Käse';
      case IngredientMatchingCategory.cheeseDrySalty: return 'Trockener, salziger Käse';
      case IngredientMatchingCategory.crustaceans: return 'Krustentier';
      case IngredientMatchingCategory.shellfish: return 'Schalentier';
      case IngredientMatchingCategory.fish: return 'Fisch';
      case IngredientMatchingCategory.meatWhite: return 'Helles Fleisch';
      case IngredientMatchingCategory.meatRed: return 'Rotes Fleisch';
      case IngredientMatchingCategory.meatCured: return 'Geräuchertes Fleisch';
      case IngredientMatchingCategory.marinadeIntense: return 'Intensive Marinade';
      case IngredientMatchingCategory.vegetableCruciferous: return 'Kreuzblütler';
      case IngredientMatchingCategory.vegetableGreen: return 'Grünes Gemüse';
      case IngredientMatchingCategory.vegetableRoots: return 'Wurzelgemüse';
      case IngredientMatchingCategory.vegetableBulbous: return 'Zwiebelgewächs';
      case IngredientMatchingCategory.vegetableNightShadow: return 'Nachtschattengewächs';
      case IngredientMatchingCategory.vegetableLegumes: return 'Hülsenfrüchte';
      case IngredientMatchingCategory.vegetableMushrooms: return 'Pilz';
      case IngredientMatchingCategory.herbsAromatic: return 'Aromatische Kräuter';
      case IngredientMatchingCategory.herbsDry: return 'Getrocknete Kräuter';
      case IngredientMatchingCategory.herbsResinous: return 'Harzige Kräuter';
      case IngredientMatchingCategory.herbsExotic: return 'Exotische Kräuter';
      case IngredientMatchingCategory.herbsBaking: return 'Back-Kräuter';
      case IngredientMatchingCategory.herbsUmami: return 'Umami-Kräuter';
      case IngredientMatchingCategory.peppersChillis: return 'Paprikagewächse';
      case IngredientMatchingCategory.undefined: return 'Keine Kategorie';
      default: return 'Unbekannt';
    }
  }

}
