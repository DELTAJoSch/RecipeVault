import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Subscription, timer} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {Ingredient, IngredientMatchingCategory} from '../../../dtos/ingredient';
import {IngredientService} from '../../../services/ingredient.service';
import {IngredientSearch} from '../../../dtos/ingredient-search';
import {ErrorService} from 'src/app/services/error.service';

@Component({
  selector: 'app-ingredient-list',
  templateUrl: './ingredient-list.component.html',
  styleUrls: ['./ingredient-list.component.scss'],
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
export class IngredientListComponent implements OnInit, OnDestroy, OnChanges {
  @Input() page = 0;
  @Input() size = 25;
  @Input() delay = 250;

  // This is necessary so onChange works, since it doesn't detect object value changes.
  // Since the other alternative (check change) is a performance killer, this is the best option.
  @Input() searchName: string | null = null;
  @Input() searchCategory: IngredientMatchingCategory | null = null;

  @Output() searchCountChanged: EventEmitter<number> = new EventEmitter<number>();

  currentTimer: Subscription = null;

  startupDelayCompleted = false;

  ingredients: Ingredient[] = [];
  maxElements = 0;

  constructor(
    private ingredientService: IngredientService,
    private notification: ToastrService,
    private errorService: ErrorService,
  ) {
    setTimeout(() => {
        this.loadIngredients();
        this.startupDelayCompleted = true;
      },
      this.delay
    );
  }

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
   * Loads the ingredients
   */
  loadIngredients() {
    const searchParameters = new IngredientSearch();

    searchParameters.category = this.searchCategory;
    searchParameters.name = this.searchName;

    this.ingredientService.getSearchResults(this.page, this.size, searchParameters).subscribe({
      next: resp => {
        this.ingredients = resp.body;

        this.maxElements = Number(resp.headers.get('X-Total-Count')) ?? Number.MAX_VALUE;
        this.searchCountChanged.emit(this.maxElements);
      },
      error: error => {
        this.errorService.handleError(error, 'Zutaten konneten nicht geladen werden');
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
          case'size': {
            if (this.startupDelayCompleted === true) {
              this.loadIngredients(); //load instantly
            }

            break;
          }
          case 'searchName':
          case 'searchCategory': {
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
        this.loadIngredients();
      }
    );
  }

  /**
   * Removes an entry from the array without reloading the ingredients.
   *
   * @param entry The ingredient to remove
   */
  onEntryDelete(entry: Ingredient) {
    const index = this.ingredients.findIndex(d => d.id === entry.id);
    this.ingredients.splice(index, 1);
    this.maxElements--;
    if (this.maxElements < this.size * (this.page + 1)) {
      this.searchCountChanged.emit(this.maxElements);
    } else {
      this.loadIngredients();
    }
  }
}
