import {Injectable} from '@angular/core';
import {AuthRequest} from '../dtos/auth-request';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs/operators';
// @ts-ignore
import jwt_decode from 'jwt-decode';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authBaseUri: string = this.globals.backendUri + '/authentication';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) {}

  /**
   * Login in the user. If it was successful, a valid JWT token will be stored
   *
   * @param authRequest User data
   */
  loginUser(authRequest: AuthRequest): Observable<string> {
    return this.httpClient.post(this.authBaseUri, authRequest, {responseType: 'text'})
      .pipe(
        tap((authResponse: string) => this.setToken(authResponse))
      );
  }


  /**
   * Check if a valid JWT token is saved in the localStorage
   */
  isLoggedIn() {
    return !!this.getToken() && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf());
  }

  /**
   * Returns true if the user is an admin and logged in.
   *
   * @returns True if the user is an admin.
   */
  isAdmin(): boolean {
    const token = this.getToken();

    //if logged in, check roles:
    if(!!token && (this.getTokenExpirationDate(this.getToken()).valueOf() > new Date().valueOf())){
      const decoded: any = jwt_decode(token);
      const roles = decoded.rol;
      if(roles === undefined || roles === null){
        return false;
      }

      return roles.includes('ROLE_ADMIN');
    }else{
      return false;
    }
  }

  /**
   * Log the user out
   */
  logoutUser() {
    localStorage.removeItem('authToken');
  }

  /**
   * Get the AuthToken
   *
   * @returns The AuthToken
   */
  getToken() {
    return localStorage.getItem('authToken');
  }

  /**
   * Returns the user role based on the current token
   */
  getUserRole() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authInfo: string[] = decoded.rol;
      if (authInfo.includes('ROLE_ADMIN')) {
        return 'ADMIN';
      } else if (authInfo.includes('ROLE_USER')) {
        return 'USER';
      }
    }
    return 'UNDEFINED';
  }

  /**
   * Return the e-mail address of the user based on the current token
   */
  getUserEmail() {
    if (this.getToken() != null) {
      const decoded: any = jwt_decode(this.getToken());
      const authEmail: string = decoded.sub;
      if(authEmail !== null || 0 !== authEmail.trim().length){
        return authEmail;
      }
    }
    return 'UNDEFINED';
  }

  /**
   * Save the auth token
   *
   * @param authResponse The token to save
   */
  private setToken(authResponse: string) {
    localStorage.setItem('authToken', authResponse);
  }

  /**
   * Get a jwt token's expiration date
   *
   * @param token The token to get the date of
   * @returns Returns the expiration date of the token
   */
  private getTokenExpirationDate(token: string): Date {

    const decoded: any = jwt_decode(token);
    if (decoded.exp === undefined) {
      return null;
    }

    const date = new Date(0);
    date.setUTCSeconds(decoded.exp);
    return date;
  }

}
