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
export class ListService {

  private listBaseUri: string = this.globals.backendUri + '/list';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) {}

  /**
   * Loads all lists from specific user from the backend.
   */
  getLists(): Observable<List[]> {
    return this.httpClient.get<List[]>(this.listBaseUri);
  }

  /**
   * Get the recipes of a list of a user.
   *
   * @param id of list
   * @param page to load
   * @param size of page
   */
  getRecipesOfList(name: string, page: number = 0, size: number = 20): Observable<HttpResponse<RecipeListElement[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);
    return this.httpClient.get<RecipeListElement[]>(this.listBaseUri + '/' + name, {params: pageParams, observe: 'response'});
}

  /**
   * Loads the number of recipes in the list.
   *
   * @param name of list.
   */
  getCount(name: string): Observable<number> {
    const httpParams = new HttpParams().set('name', name);
    const options = { params: httpParams };
    return this.httpClient.get<number>(this.listBaseUri + '/number', options);
  }

  /**
   * Remove a recipe from a list.
   *
   * @param listName name of list
   * @param recipeId id of recipe
   */
  removeFromList(listName: string, recipeId: number): Observable<object> {
    const httpParams = new HttpParams().set('recipeId', recipeId);
    const options = { params: httpParams };
    return this.httpClient.delete(this.listBaseUri + '/' + listName, options);
  }

  /**
   * Create a new list.
   *
   * @param name of list
   */
  createList(name: string): Observable<object> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('name', name);
    return this.httpClient.post(this.listBaseUri, null, {params: pageParams, observe: 'response'});
  }

  /**
   * Delete a list.
   *
   * @param name of list
   */
  deleteList(name: string): Observable<object> {
    const httpParams = new HttpParams().set('name', name);
    const options = { params: httpParams };
    return this.httpClient.delete(this.listBaseUri, options);
  }

  /**
   * Add a recipe to a specific list.
   *
   * @param name of list
   * @param recipeId id of recipe
   */
  addToList(name: string, recipeId: number): Observable<object> {
    const httpParams = new HttpParams().set('recipeId', recipeId);
    const options = {params: httpParams};
    return this.httpClient.post(this.listBaseUri + '/' + name, null, options);
  }

}
