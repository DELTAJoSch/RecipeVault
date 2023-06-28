import { Pipe, PipeTransform } from '@angular/core';
import { AmountUnit } from '../dtos/amount';

@Pipe({
  name: 'unit'
})
export class UnitPipe implements PipeTransform {

  transform(value: AmountUnit, ...args: unknown[]): string {
    switch(value){
      case AmountUnit.g: return 'g';
      case AmountUnit.piece: return 'Stück(e)';
      case AmountUnit.lb: return 'lb';
      case AmountUnit.tsp: return 'TL';
      case AmountUnit.tbsp: return 'EL';
      case AmountUnit.kg: return 'kg';
      case AmountUnit.fl: return 'fl. oz.';
      case AmountUnit.oz: return 'oz.';
      case AmountUnit.ml: return 'ml';
      case AmountUnit.cup: return 'cup(s)';
      default: return '';
    }
  }

}
