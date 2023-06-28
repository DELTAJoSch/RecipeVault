import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {RecipeDetails} from '../dtos/recipe-details';
import {RecipeSearch} from '../dtos/recipe-search';
import { RecipeListElement } from '../dtos/recipe-list-element';

@Injectable({
  providedIn: 'root'
})
export class RecipeService {

  private recipeBaseUri: string = this.globals.backendUri + '/recipe';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) { }

  /**
   * Loads the recipes at the specified page from the backend
   */
  getRecipes(page: number = 0, size: number = 20): Observable<HttpResponse<RecipeListElement[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);

    return this.httpClient.get<RecipeListElement[]>(this.recipeBaseUri, {params: pageParams, observe: 'response'});
  }

  /**
   * Search for recipes
   *
   * @param search The recipes to search for
   * @returns Returns a list of recipes matching the search parameters
   */
  getSearchResults(page: number = 0, size: number = 20, search: RecipeSearch): Observable<HttpResponse<RecipeListElement[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);

    if(search.difficulty !== null){
      pageParams = pageParams.append('difficulty', search.difficulty);
    }

    if(search.name !== null){
      pageParams = pageParams.append('name', search.name);
    }

    return this.httpClient.get<RecipeListElement[]>(this.recipeBaseUri + '/search', {params: pageParams, observe: 'response'});
  }

  /**
   * Loads specific recipe from the backend
   *
   * @param id of recipe to load
   */
  getRecipeById(id: number): Observable<RecipeDetails> {
    return this.httpClient.get<RecipeDetails>(this.recipeBaseUri + '/' + id);
  }

  /**
   * Persists recipe to the backend
   *
   * @param recipe to persist
   */
  createRecipe(recipe: RecipeDetails): Observable<void> {
    return this.httpClient.post<void>(this.recipeBaseUri, recipe);
  }

  /**
   * Updates a recipe in the database
   *
   * @param recipe The new data of the wine
   * @param id The id of the wine to update
   * @returns Returns the updated wine
   */
  updateRecipe(recipe: RecipeDetails, id: number): Observable<RecipeDetails> {
    return this.httpClient.post<RecipeDetails>(this.recipeBaseUri + '/' + id, recipe);
  }

  /**
   * Delete the specified wine
   *
   * @param id The id of the wine to delete
   */
  deleteRecipe(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.recipeBaseUri + '/' + id);
  }
}
