import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'recipeWineCategoryLinking'
})
export class RecipeWineCategoryLinkingPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    switch(value) {
      case 'SPARKLING': return 'sparkling';
      case 'LIGHT_WHITE': return 'lightWhite';
      case 'FULL_WHITE': return 'fullWhite';
      case 'AROMATIC_WHITE': return 'aromaticWhite';
      case 'ROSE': return 'rose';
      case 'LIGHT_RED': return 'lightRed';
      case 'MIDDLE_RED': return 'middleRed';
      case 'FULL_RED': return 'fullRed';
      case 'DESSERT': return 'dessert';
      default: return 'unknown';
    }
  }

}
