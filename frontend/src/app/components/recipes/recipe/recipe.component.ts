import {Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {Subscription, timer} from 'rxjs';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {Difficulty, RecipeDetails} from '../../../dtos/recipe-details';
import {RecipeSearch} from '../../../dtos/recipe-search';
import {RecipeService} from '../../../services/recipe.service';
import {ErrorService} from 'src/app/services/error.service';
import {RecipeListElement} from 'src/app/dtos/recipe-list-element';

@Component({
  selector: 'app-recipe',
  templateUrl: './recipe.component.html',
  styleUrls: ['./recipe.component.scss'],
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
      transition('* => hidden, * => void', animate('200ms ease-in-out'))
    ])
  ]
})
export class RecipeComponent implements OnInit, OnChanges, OnDestroy {
  @Input() page = 0;
  @Input() size = 12;
  @Input() delay = 250;

  // This is neccessary so onChange works, since it doesn't detect object value changes.
  // Since the other alternative (check change) is a performance killer, this is the best option.
  @Input() searchName: string | null = null;
  @Input() searchDifficulty: Difficulty | null = null;

  @Output() searchCountChanged: EventEmitter<number> = new EventEmitter<number>();

  currentTimer: Subscription = null;
  recipes: RecipeListElement[] = [];

  startupDelayCompleted = false;

  maxElements = 0;

  constructor(
    private recipeService: RecipeService,
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
   * handles cleanup on destroy
   */
  ngOnDestroy(): void {
    if (this.currentTimer !== null) { //unsubscribe to ensure no errors occur
      this.currentTimer.unsubscribe();
    }
  }

  /**
   * load recipes on init
   */
  ngOnInit(): void {
    if (this.currentTimer !== null) {
      this.currentTimer.unsubscribe();
    }
    this.currentTimer = timer(this.delay).subscribe(
      _ => {
        this.loadRecipes();
      }
    );
  }


  /**
   * Loads the recipes
   */
  loadRecipes() {
    const searchParameters = new RecipeSearch();

    searchParameters.difficulty = this.searchDifficulty;
    searchParameters.name = this.searchName;

    this.recipeService.getSearchResults(this.page, this.size, searchParameters).subscribe({
      next: resp => {
        this.recipes = resp.body;

        this.maxElements = Number(resp.headers.get('X-Total-Count')) ?? Number.MAX_VALUE;
        this.searchCountChanged.emit(this.maxElements);
      },
      error: error => {
        this.errorService.handleError(error, `Rezept konnte nicht geladen werden`);
      }
    });
  }

  /**
   * Handle changes of input parameters like size, page and search parameters
   *
   * @param changes The changes that occured
   */
  ngOnChanges(changes: SimpleChanges) {
    this.recipes = [];
    this.onParameterChange();
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
        this.loadRecipes();
      }
    );
  }

  /**
   * Removes an entry from the array without reloading the recipes.
   *
   * @param entry The recipe to remove
   */
  onEntryDelete(entry: RecipeDetails) {
    const index = this.recipes.findIndex(d => d.id === entry.id);
    this.recipes.splice(index, 1);
    this.maxElements--;
    if (this.maxElements < this.size * (this.page + 1)) {
      this.searchCountChanged.emit(this.maxElements);
    } else {
      this.loadRecipes();
    }
  }
}
