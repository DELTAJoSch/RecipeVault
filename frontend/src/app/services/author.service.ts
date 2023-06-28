import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Author} from '../dtos/author';

@Injectable({
  providedIn: 'root'
})
export class AuthorService {

  private authorBaseUri: string = this.globals.backendUri + '/author';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) {}

  /**
   * Loads the authors at the specified page from the backend
   */
  getAuthors(page: number = 0, size: number = 20): Observable<HttpResponse<Author[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);
    return this.httpClient.get<Author[]>(this.authorBaseUri, {params: pageParams, observe: 'response'});
  }


  /**
   * Loads specific author from the backend
   *
   * @param id of author to load
   */
  getAuthorById(id: number): Observable<Author> {
    return this.httpClient.get<Author>(this.authorBaseUri + '/' + id);
  }

  /**
   * Persists author to the backend
   *
   * @param author to persist
   */
  createAuthor(author: Author): Observable<void> {
    return this.httpClient.post<void>(this.authorBaseUri, author);
  }

  /**
   * Updates an author in the database
   *
   * @param author  The new data of the author
   * @returns Returns the updated author
   */
  updateAuthor(author: Author): Observable<Author> {
    return this.httpClient.post<Author>(this.authorBaseUri + '/' + author.id, author);
  }

  /**
   * Delete the specified author
   *
   * @param id The id of the author to delete
   */
  deleteAuthor(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.authorBaseUri + '/' + id);
  }

  /**
   * Get authors stored in the system with the given name
   *
   * @param name of the author to get
   * @return observable of found authors.
   */
  searchByName(name: string): Observable<Author[]> {
    const params = new HttpParams()
      .set('name', name);
    return this.httpClient.get<Author[]>(this.authorBaseUri, { params });
  }
}
