import { Injectable } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor(
    private router: Router,
    private notification: ToastrService,

  ) { }

  /**
   * handleError handles errors send back by the backend, logging, notificaitons, and routing
   */
  public handleError(
    error,
    msgPrefix: string = 'etwas ist falschgelaufen',
    route = undefined,
    sendNotificaiton = true,
    logToConsole = true): void {

    // get message form error
    let errorString = '';
    if (typeof error.error === 'object') {
      if (typeof error.error.error === 'string' || error.error instanceof String) {
        errorString = `: ${error.error.error}`;
      }
    } else {
      if (typeof error.error === 'string' || error.error instanceof String) {
        // check if it is in the format of validation errors (jakata.validation)
        if (error.error.startsWith('{Validation errors=[')) {
          errorString = ': Validierung fehlgeschlagen da ' + error.error.substring('{Validation errors=['.length, error.error.length - 2);
        } else {
          errorString = `: ${error.error}`;
        }


      }
    }

    // assemble full error message
    const msg = msgPrefix + errorString;

    if (logToConsole) {
      console.error(msg);
      console.error(error);
    }
    if (sendNotificaiton) {
      this.notification.error(msg);
    }
    if (route !== undefined && route !== null) {
      this.router.navigateByUrl(route);
    }

  }
}
