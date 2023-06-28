import { Pipe, PipeTransform } from '@angular/core';
import { WineCategory } from '../dtos/wine';

@Pipe({
  name: 'wineCategory'
})
export class WineCategoryPipe implements PipeTransform {

  transform(value: WineCategory, ...args: unknown[]): string {
    switch(value) {
      case WineCategory.sparkling: return 'Sekt und Prosecco';
      case WineCategory.lightWhite: return 'Leichter Weißwein';
      case WineCategory.fullWhite: return 'Voller Weißwein';
      case WineCategory.aromaticWhite: return ' Aromatischer Weißwein';
      case WineCategory.rose: return 'Rosé';
      case WineCategory.lightRed: return 'Leichter Rotwein';
      case WineCategory.middleRed: return 'Mittelschwerer Rotwein';
      case WineCategory.fullRed: return 'Voller Rotwein';
      case WineCategory.dessert: return 'Süßwein';
      default: return 'Unbekannt';
    }
  }

}
