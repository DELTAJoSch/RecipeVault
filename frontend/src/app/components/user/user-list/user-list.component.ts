import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {Subscription, timer} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {ErrorService} from '../../../services/error.service';
import {User} from '../../../dtos/user';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit, OnChanges, OnDestroy {
  @Input() page = 0;
  @Input() size = 25;
  @Input() delay = 250;

  @Output() searchCountChanged: EventEmitter<number> = new EventEmitter<number>();

  currentTimer: Subscription = null;

  startupDelayCompleted = false;

  users: User[] = [];
  maxElements = 0;

  constructor(
    private userService: UserService,
    private notification: ToastrService,
    private errorService: ErrorService,
  ) {
    setTimeout(() => {
        this.startupDelayCompleted = true;
      },
      this.delay
    );
  }

  /**
   * Handle cleanup on destroy
   */
  ngOnDestroy(): void {
    if (this.currentTimer !== null) { //unsubscribe to ensure no errors occur
      this.currentTimer.unsubscribe();
    }
  }

  /**
   * Calls debounce delay on init.
   */
  ngOnInit(): void {
    this.onParameterChange();
  }


  /**
   * Loads the users
   */
  loadUsers() {
    this.userService.getUsers(this.page, this.size).subscribe({
      next: resp => {
        this.users = resp.body;

        this.maxElements = Number(resp.headers.get('X-Total-Count')) ?? Number.MAX_VALUE;
        this.searchCountChanged.emit(this.maxElements);
      },
      error: error => {
        this.errorService.handleError(error, 'Benutzer konnten nicht geladen werden');
      }
    });
  }

  /**
   * Handle changes of input parameters like size, page and search parameters
   *
   * @param changes The changes that occurred
   */
  ngOnChanges(changes: SimpleChanges) {
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case 'page':
          case 'size': {
            if (this.startupDelayCompleted === true) {
              this.loadUsers(); //load instantly
            }
          }
        }
      }
    }
  }


  /**
   * Add debounce delay to parameter changes for a better search experience.
   * Since the form is not part of this component, wait for a bit of time (set by this.delay)
   */
  onParameterChange() {
    if (this.currentTimer !== null) {
      this.currentTimer.unsubscribe();
    }

    this.currentTimer = timer(this.delay).subscribe(
      _ => {
        this.loadUsers();
      }
    );
  }

  /**
   * Removes an entry from the array without reloading the users.
   *
   * @param entry The user to remove
   */
  onEntryDelete(entry: User) {
    const index = this.users.findIndex(d => d.id === entry.id);
    this.users.splice(index, 1);
    this.maxElements--;
    if (this.maxElements < this.size * (this.page + 1)) {
      this.searchCountChanged.emit(this.maxElements);
    } else {
      this.loadUsers();
    }
  }
}
