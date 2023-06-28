import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationOption } from '../dtos/option';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class OptionsService {
  private optionsBaseUri: string = this.globals.backendUri + '/options';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) { }

  /**
   * Gets the options for the current search
   *
   * @param name The name to search for
   * @param page The page to look for
   * @param size The size of the page
   * @returns Returns the http response
   */
  getApplicationOptions(name: string, page = 0, size = 25): Observable<HttpResponse<ApplicationOption[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);


    if(name !== null){
      pageParams = pageParams.append('name', name);
    }

    return this.httpClient.get<ApplicationOption[]>(this.optionsBaseUri, {params: pageParams, observe: 'response'});
  }

  /**
   * Updates an option
   *
   * @param option The new data
   * @param id The id of the option
   * @returns Returns the updated data
   */
  update(option: ApplicationOption, id: number): Observable<ApplicationOption> {
    return this.httpClient.post<ApplicationOption>(this.optionsBaseUri + `/${id}`, option);

  }
}
