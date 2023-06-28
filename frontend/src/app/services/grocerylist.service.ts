import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {RecipeListElement} from '../dtos/recipe-list-element';
import { List } from '../dtos/list';
import { Amount } from '../dtos/amount';

@Injectable({
  providedIn: 'root'
})
export class GroceryListService {

  private grocerylistBaseUri: string = this.globals.backendUri + '/grocerylist';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Generate a grocery list from the number of portions per recipe.
   *
   * @param portions portions per recipe
   */
  generateGroceryList(portions: number[][]): Observable<Amount[]> {
    return this.httpClient.post<Amount[]>(this.grocerylistBaseUri, portions);
  }
}
