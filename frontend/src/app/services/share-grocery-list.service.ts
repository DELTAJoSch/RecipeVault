import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Amount } from '../dtos/amount';

@Injectable()
export class ShareGroceryListService {

  groceryList = new BehaviorSubject([]);
  currentGroceryList = this.groceryList.asObservable();

  constructor() { }

  /**
   * sets the grocery list, that is stored.
   *
   * @param list to set groceryList to
   */
  setGroceryList(list: Amount[]) {
    this.groceryList.next(list);
  }
}
