import { Pipe, PipeTransform } from '@angular/core';
import { IngredientMatchingCategory } from '../dtos/ingredient';

/**
 * This pipe produces examples for the matching categories.
 */
@Pipe({
  name: 'ingredientCategoryExample'
})
export class IngredientCategoryExamplePipe implements PipeTransform {

  transform(value: IngredientMatchingCategory, ...args: unknown[]): string {
    switch(value){
      case IngredientMatchingCategory.cheeseCrumbly: return 'z.B.: Feta, Halloumi';
      case IngredientMatchingCategory.cheeseIntense: return 'z.B.: Stilton, Roquefort, Gorgonzola';
      case IngredientMatchingCategory.cheeseCream: return 'z.b.: Ricotta, Quark, Sour Cream';
      case IngredientMatchingCategory.cheeseDelicate: return 'z.B.: Brie, Camembert, Burrata';
      case IngredientMatchingCategory.cheeseNutty: return 'z.B.: Gruyère, Edamer, Emmentaler';
      case IngredientMatchingCategory.cheeseFruity: return 'z.B.: Cheddar, Gouda, Munster';
      case IngredientMatchingCategory.cheeseDrySalty: return 'z.B.: Parmiggiano, Grana Padano, Pecorino';
      case IngredientMatchingCategory.crustaceans: return 'z.B.: Hummer, Krebs, Garnele';
      case IngredientMatchingCategory.shellfish: return 'z.B.: Auster, Jakobsmuschel';
      case IngredientMatchingCategory.fish: return 'z.B.: Heilbutt, Lachs, Forelle';
      case IngredientMatchingCategory.meatWhite: return 'z.B.: Huhn, Schweinekotelett, Tofu, Seitan';
      case IngredientMatchingCategory.meatRed: return 'z.B.: Rind, Lamm, Wild';
      case IngredientMatchingCategory.meatCured: return 'z.B.: Salumi, Speck, Schinken';
      case IngredientMatchingCategory.marinadeIntense: return 'z.B.: Teryiakisauce, BBQ-Sauce, Essig-Marinaden';
      case IngredientMatchingCategory.vegetableCruciferous: return 'z.B.: Weißkohl, Brokkoli, Rucola';
      case IngredientMatchingCategory.vegetableGreen: return 'z.B.: Grüne Bohne, Erbse, Grühnkohl, Avocado, grüner Paprika';
      case IngredientMatchingCategory.vegetableRoots: return 'z.B.: Yams, Karotte, Kürbis, Zucchini';
      case IngredientMatchingCategory.vegetableBulbous: return 'z.B.: Zwiebel, Knoblauch, Schalotte';
      case IngredientMatchingCategory.vegetableNightShadow: return 'z.B.: Chili, Tomate, Aubergine, Paprika';
      case IngredientMatchingCategory.vegetableLegumes: return 'z.B.: weiße Bohne, Linse, Wachtelbohne';
      case IngredientMatchingCategory.vegetableMushrooms: return 'z.B.: Champignon, Steinpilz, Shiitake';
      case IngredientMatchingCategory.herbsAromatic: return 'z.B.: Minze, Basilikum, Kerbel';
      case IngredientMatchingCategory.herbsDry: return 'z.B.: Oregan, Thymian, Dill, Petersilie';
      case IngredientMatchingCategory.herbsResinous: return 'z.B.: Rosmarin, Salbei, Fichte, Lavendel';
      case IngredientMatchingCategory.herbsExotic: return 'z.B.: Anis, Kardamom, Ingwer';
      case IngredientMatchingCategory.herbsBaking: return 'z.B.: Zimt, Piment, Vanille, Nelke, Muskat';
      case IngredientMatchingCategory.herbsUmami: return 'z.B.: Koriander, Kreuzkümmel, MSG/MNG, Kurkuma';
      case IngredientMatchingCategory.peppersChillis: return 'z.B.: Cayenne-Pfeffer, Pul Biber, Chipotle';
      case IngredientMatchingCategory.undefined: return 'z.B.: Reis, Brot, neutrales';
      default: return '';
    }
  }

}
