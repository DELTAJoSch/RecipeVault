import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {Wine, WineCategory} from 'src/app/dtos/wine';
import {WineService} from 'src/app/services/wine.service';
import {WineSearch} from 'src/app/dtos/wine-search';
import {ToastrService} from 'ngx-toastr';
import {Subscription, timer} from 'rxjs';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {ErrorService} from 'src/app/services/error.service';

@Component({
  selector: 'app-wine',
  templateUrl: './wine.component.html',
  styleUrls: ['./wine.component.scss'],
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
      transition('* => visible', animate('500ms ease-in-out')),
      transition('* => hidden, * => void', animate('500ms ease-in-out'))
    ])
  ]
})
export class WineComponent implements OnInit, OnChanges, OnDestroy {
  @Input() page = 0;
  @Input() size = 25;
  @Input() delay = 250;

  // This is neccessary so onChange works, since it doesn't detect object value changes.
  // Since the other alternative (check change) is a performance killer, this is the best option.
  @Input() searchName: string | null = null;
  @Input() searchVinyard: string | null = null;
  @Input() searchCountry: string | null = null;
  @Input() searchCategory: WineCategory | null = null;

  @Output() searchCountChanged: EventEmitter<number> = new EventEmitter<number>();

  currentTimer: Subscription = null;

  startupDelayCompleted = false;

  wines: Wine[] = [];

  maxElements = 0;

  constructor(
    private wineService: WineService,
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
   * Initialize this component
   */
  ngOnInit(): void {
    this.onParameterChange();
  }


  /**
   * Loads the wines
   */
  loadWines() {
    const searchParameters = new WineSearch();

    searchParameters.category = this.searchCategory;
    searchParameters.country = this.searchCountry;
    searchParameters.name = this.searchName;
    searchParameters.vinyard = this.searchVinyard;

    this.wineService.getSearchResults(this.page, this.size, searchParameters).subscribe({
      next: resp => {
        this.wines = resp.body;

        this.maxElements = Number(resp.headers.get('X-Total-Count')) ?? Number.MAX_VALUE;
        this.searchCountChanged.emit(this.maxElements);
      },
      error: error => {
        this.errorService.handleError(error, `Weine konnten nicht geladen werden`);
      }
    });
  }

  /**
   * Handle changes of input parameters like size, page and search parameters
   *
   * @param changes The changes that occured
   */
  ngOnChanges(changes: SimpleChanges) {
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case 'page':
          case'size': {
            if (this.startupDelayCompleted === true) {
              this.loadWines(); //load instantly
            }

            break;
          }
          case 'searchName':
          case 'searchVinyard':
          case 'searchCategory':
          case 'searchCountry': {
            this.onParameterChange(); // delays the load a bit
            break;
          }
        }
      }
    }
  }


  /**
   * Add a debounce delay to parameter changes for a better search experience.
   * Since the form is not part of this component, wait for a bit of time (set by this.delay)
   */
  onParameterChange() {
    if (this.currentTimer !== null) {
      this.currentTimer.unsubscribe();
    }
    this.currentTimer = timer(this.delay).subscribe(
      _ => {
        this.loadWines();
      }
    );
  }

  /**
   * Removes an entry from the array without reloading the wines.
   *
   * @param entry The wine to remove
   */
  onEntryDelete(entry: Wine) {
    const index = this.wines.findIndex(d => d.id === entry.id);
    this.wines.splice(index, 1);
    this.maxElements--;
    if (this.maxElements < this.size * (this.page + 1)) {
      this.searchCountChanged.emit(this.maxElements);
    } else {
      this.loadWines();
    }
  }
}
