import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';
import {User} from '../dtos/user';
import {UserSignup} from '../dtos/user-signup';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userBaseUri: string = this.globals.backendUri + '/user';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals,
  ) { }

  /**
   * Loads the users at the specified page from the backend
   */
  getUsers(page: number = 0, size: number = 20): Observable<HttpResponse<User[]>> {
    let pageParams = new HttpParams();
    pageParams = pageParams.append('size', size);
    pageParams = pageParams.append('page', page);
    return this.httpClient.get<User[]>(this.userBaseUri, {params: pageParams, observe: 'response'});
  }

  /**
   * Loads specific user from the backend
   *
   * @param id of user to load
   */
  getUserById(id: number): Observable<User> {
    let idParam = new HttpParams();
    idParam = idParam.append('id', id);
    return this.httpClient.get<User>(this.userBaseUri, {params: idParam});
  }

  /**
   * Loads specific user form the backend
   *
   * @param email E-Mail Address of the user to be fetched
   */
  getUserByEmail(email: string): Observable<User> {
    let mailParam = new HttpParams();
    mailParam = mailParam.append('email', email);
    return this.httpClient.get<User>(this.userBaseUri, {params: mailParam});
  }

  /**
   * Persists user to the backend
   *
   * @param user to persist
   */
  createUser(user: User): Observable<void> {
    return this.httpClient.post<void>(this.userBaseUri, user);
  }

  /**
   * Updates a user in the database
   *
   * @param user The new data of the user
   * @param id The id of the user to update
   * @returns Returns the updated user
   */
  updateUser(user: User, id: number): Observable<User> {
    return this.httpClient.post<User>(this.userBaseUri + '/' + id, user);
  }

  /**
   * Delete the specified user
   *
   * @param id The id of the user to delete
   */
  deleteUser(id: number): Observable<void> {
    return this.httpClient.delete<void>(this.userBaseUri + '/' + id);
  }

  /**
   * Sign up as a new User.
   *
   * @param user Parameters for the new User
   */
  signUpUser(user: UserSignup): Observable<void> {
    return this.httpClient.post<void>(this.globals.backendUri + '/signup', user);
  }

  /**
   * Get Details of currently logged-in User.
   */
  getCurrentUser(): Observable<User> {
    return this.httpClient.get<User>(this.userBaseUri + '/me');
  }

  /**
   * Update currently logged-in User.
   *
   * @param user The data of the new user
   */
  updateCurrentUser(user: User): Observable<User> {
    return this.httpClient.post<User>(this.userBaseUri + '/me', user);
  }

  /**
   * Delete currently logged-in User.
   */
  deleteCurrentUser(): Observable<void> {
    return this.httpClient.delete<void>(this.userBaseUri + '/me');
  }
}
