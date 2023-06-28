import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {RecipeListElement} from '../dtos/recipe-list-element';
import {Favorite} from '../dtos/favorite';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {

  private favoriteBaseUri: string = this.globals.backendUri + '/favorites';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) {}

  /**
   * Loads favorites from specific user from the backend.
   */
  getFavorites(page: number = 0, size: number = 20): Observable<HttpResponse<RecipeListElement[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);
    return this.httpClient.get<RecipeListElement[]>(this.favoriteBaseUri, {params: pageParams, observe: 'response'});
  }

  /**
   * Adds a recipe to a user's favorites.
   *
   * @param recipeId recipe to add to favorites of user
   */
  addFavorite(recipeId: number): Observable<Favorite> {
    const httpParams = new HttpParams().set('id', recipeId);
    return this.httpClient.post<Favorite>(this.favoriteBaseUri, httpParams);
  }

  /**
   * Remove a recipe from a user's favorites.
   *
   * @param recipeId id of recipe
   */
  remove(recipeId: number): Observable<object>{
    const httpParams = new HttpParams().set('recipeId', recipeId);
    const options = { params: httpParams };
    return this.httpClient.delete(this.favoriteBaseUri, options);
  }

  /**
   * Loads the number of favorites of the user from the backend
   */
  getCount(): Observable<number> {
    return this.httpClient.get<number>(this.favoriteBaseUri + '/number');
  }

  /**
   * returns true if the recipe is included in the user's favorites
   *
   * @param recipeId id of recipe
   */
  isFavorite(recipeId: number): Observable<boolean> {
    return this.httpClient.get<boolean>(this.favoriteBaseUri + '/status/' + recipeId);
  }

}
