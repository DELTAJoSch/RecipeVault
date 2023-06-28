import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Ingredient} from '../dtos/ingredient';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {IngredientSearch} from '../dtos/ingredient-search';

@Injectable({
  providedIn: 'root'
})
export class IngredientService {

  private ingredientBaseUri: string = this.globals.backendUri + '/ingredient';
  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) { }

  /**
   * Persists ingredient to the backend
   *
   * @param ingredient to persist
   */
  createIngredient(ingredient: Ingredient): Observable<void> {
    return this.httpClient.post<void>(this.ingredientBaseUri, ingredient);
  }

  /**
   * Updates an ingredient in the database
   *
   * @param ingredient the new data of the ingredient
   * @param id the id of the ingredient to update
   * @returns Returns the updated ingredient
   */
  updateIngredient(ingredient: Ingredient, id: number): Observable<Ingredient> {
    return this.httpClient.post<Ingredient>(this.ingredientBaseUri + '/' + id, ingredient);
  }

  /**
   * Delete the specified ingredient
   *
   * @param id The id of the ingredient to delete
   */
  deleteIngredient(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.ingredientBaseUri + '/' + id);
  }

  /**
   * Get ingredients stored in the system with the given name
   *
   * @param name of the ingredients to get
   * @return observable of found ingredients.
   */
  searchByName(name: string): Observable<Ingredient[]> {
    const params = new HttpParams()
      .set('name', name);

    return this.httpClient.get<Ingredient[]>(this.ingredientBaseUri +'/' +name, { params });
  }

  /**
   * Search for ingredients
   *
   * @param search the ingredients to search for
   * @returns a list of ingredients matching the search parameters
   */
  getSearchResults(page: number = 0, size: number = 20, search: IngredientSearch): Observable<HttpResponse<Ingredient[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);

    if(search.category !== null){
      pageParams = pageParams.append('category', search.category);
    }

    if(search.name !== null){
      pageParams = pageParams.append('name', search.name);
    }

    return this.httpClient.get<Ingredient[]>(this.ingredientBaseUri + '/search', {params: pageParams, observe: 'response'});
  }

}
