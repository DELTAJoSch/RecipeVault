import {Component, HostListener, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Subject } from 'rxjs';
import { ErrorService } from 'src/app/services/error.service';

// Depending on the amount of elements in the header, this value might have to be adjusted if the header is changed.
const CHANGE_SIZE = 1400;

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  animations: [
    trigger('visibleHidden', [
      state(
        'visible',
        style({
          opacity: 1
        })
      ),
      state(
        'hidden, void',
        style({
          opacity: 0
        })
      ),
      transition('* => visible', animate('400ms ease-in-out')),
      transition('* => hidden, * => void', animate('400ms ease-in-out'))
    ])
  ]
})
export class HeaderComponent implements OnInit {
  isCollapsed = true;
  isMobile = true;
  dropdownChangeSubject: Subject<string> = new Subject<string>();

  constructor(
    public authService: AuthService,
    private userService: UserService,
    private router: Router,
    private notification: ToastrService,
    private errorService: ErrorService,
  ) { }

  /**
   * React to window size changes.
   */
  @HostListener('window:resize')
  onResize() {
    this.isMobile = window.innerWidth <= CHANGE_SIZE;
  }

  /**
   * Initializes this component.
   */
  ngOnInit() {
    this.isMobile = window.innerWidth <= CHANGE_SIZE;
  }

  /**
   * React to account details press
   */
  onClickAccountDetails() {
    this.router.navigateByUrl('user/me/details');
  }

  /**
   * Change the collapse state for the mobile view
   */
  collapsedToggle() {
    this.isCollapsed = !this.isCollapsed;
  }

  /**
   * React to changes in one of the dropdown components. Alert other components that the state has changed.
   *
   * @param event The tag-value of the raising component
   */
  dropdownChanged(event: string) {
    this.dropdownChangeSubject.next(event);
  }
}
