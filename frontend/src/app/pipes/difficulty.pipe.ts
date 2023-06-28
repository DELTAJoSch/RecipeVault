import { Pipe, PipeTransform } from '@angular/core';
import { Difficulty } from '../dtos/recipe-details';

@Pipe({
  name: 'difficulty'
})
export class DifficultyPipe implements PipeTransform {

  transform(value: Difficulty, ...args: unknown[]): string {
    switch(value){
      case Difficulty.easy: return 'Einfach';
      case Difficulty.medium: return 'Mittel';
      case Difficulty.hard: return ' Schwer';
      default: return 'Unbekannt';
    }
  }

}
