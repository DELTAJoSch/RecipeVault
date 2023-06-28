import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {Subscription, timer} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {ErrorService} from '../../../services/error.service';
import {AuthorService} from '../../../services/author.service';
import {Author} from '../../../dtos/author';

@Component({
  selector: 'app-author-list',
  templateUrl: './author-list.component.html',
  styleUrls: ['./author-list.component.scss']
})
export class AuthorListComponent implements OnInit, OnChanges, OnDestroy {
  @Input() page = 0;
  @Input() size = 25;
  @Input() delay = 250;

  @Output() searchCountChanged: EventEmitter<number> = new EventEmitter<number>();

  currentTimer: Subscription = null;

  startupDelayCompleted = false;

  authors: Author[] = [];
  maxElements = 0;

  constructor(
    private authorService: AuthorService,
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
   * Loads the authors
   */
  loadAuthors() {
    this.authorService.getAuthors(this.page, this.size).subscribe({
      next: resp => {
        this.authors = resp.body;

        this.maxElements = Number(resp.headers.get('X-Total-Count')) ?? Number.MAX_VALUE;
        this.searchCountChanged.emit(this.maxElements);
      },
      error: error => {
        this.errorService.handleError(error, 'Autoren konnten nicht geladen werden');
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
              this.loadAuthors(); //load instantly
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
        this.loadAuthors();
      }
    );
  }

  /**
   * Removes an entry from the array without reloading the authors.
   *
   * @param entry The author to remove
   */
  onEntryDelete(entry: Author) {
    const index = this.authors.findIndex(d => d.id === entry.id);
    this.authors.splice(index, 1);
    this.maxElements--;
    if (this.maxElements < this.size * (this.page + 1)) {
      this.searchCountChanged.emit(this.maxElements);
    } else {
      this.loadAuthors();
    }
  }
}
