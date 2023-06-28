import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'recipeWineCategory'
})
export class RecipeWineCategoryPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    switch(value) {
      case 'SPARKLING': return 'Sekt und Prosecco';
      case 'LIGHT_WHITE': return 'Leichter Weißwein';
      case 'FULL_WHITE': return 'Voller Weißwein';
      case 'AROMATIC_WHITE': return 'Aromatischer Weißwein';
      case 'ROSE': return 'Rosé';
      case 'LIGHT_RED': return 'Leichter Rotwein';
      case 'MIDDLE_RED': return 'Mittelschwerer Rotwein';
      case 'FULL_RED': return 'Voller Rotwein';
      case 'DESSERT': return 'Süßwein';
      default: return 'Unbekannt';
    }
  }

}
