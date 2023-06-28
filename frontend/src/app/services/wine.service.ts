import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import { Wine } from '../dtos/wine';
import { WineSearch } from '../dtos/wine-search';

@Injectable({
  providedIn: 'root'
})
export class WineService {

  private wineBaseUri: string = this.globals.backendUri + '/wine';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) { }

  /**
   * Loads the wines at the specified page from the backend
   */
  getWines(page: number = 0, size: number = 20): Observable<HttpResponse<Wine[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);

    return this.httpClient.get<Wine[]>(this.wineBaseUri, {params: pageParams, observe: 'response'});
  }

  /**
   * Search for wines
   *
   * @param search The wines to search for
   * @returns Returns a list of wines matching the search parameters
   */
  getSearchResults(page: number = 0, size: number = 20, search: WineSearch): Observable<HttpResponse<Wine[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);

    if(search.category !== null){
      pageParams = pageParams.append('category', search.category);
    }

    if(search.name !== null){
      pageParams = pageParams.append('name', search.name);
    }

    if(search.vinyard !== null){
      pageParams = pageParams.append('vinyard', search.vinyard);
    }

    if(search.country !== null){
      pageParams = pageParams.append('country', search.country);
    }

    return this.httpClient.get<Wine[]>(this.wineBaseUri + '/search', {params: pageParams, observe: 'response'});
  }

  /**
   * Loads specific wine from the backend
   *
   * @param id of wine to load
   */
  getWineById(id: number): Observable<Wine> {
    return this.httpClient.get<Wine>(this.wineBaseUri + '/' + id);
  }

  /**
   * Persists wine to the backend
   *
   * @param wine to persist
   */
  createWine(wine: Wine): Observable<void> {
    return this.httpClient.post<void>(this.wineBaseUri, wine);
  }

  /**
   * Updates a wine in the database
   *
   * @param wine The new data of the wine
   * @param id The id of the wine to update
   * @returns Returns the updated wine
   */
  updateWine(wine: Wine, id: number): Observable<Wine> {
    return this.httpClient.post<Wine>(this.wineBaseUri + '/' + id, wine);
  }

  /**
   * Delete the specified wine
   *
   * @param id The id of the wine to delete
   */
  deleteWine(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.wineBaseUri + '/' + id);
  }
}
