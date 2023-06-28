import {Component, Input, OnInit, Output, EventEmitter, OnChanges, OnDestroy} from '@angular/core';
import {FavoriteService} from '../../../services/favorite.service';
import {RecipeListElement} from '../../../dtos/recipe-list-element';
import {ToastrService} from 'ngx-toastr';
import { ErrorService } from 'src/app/services/error.service';
import { Subscription, timer } from 'rxjs';
import {animate, state, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-favorites',
  templateUrl: './favorite.component.html',
  styleUrls: ['./favorite.component.scss'],
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
export class FavoriteComponent implements OnInit, OnChanges, OnDestroy {
  @Input() page = 0;
  @Input() size = 12;
  @Input() delay = 250;

  @Output() countChanged= new EventEmitter();

  errorMessage = '';
  favorites: RecipeListElement[] = [];
  maxPageNum = 0;

  currentTimer: Subscription = null;

  startupDelayCompleted = false;

  constructor(
    private service: FavoriteService,
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
   * Get favorites of user on init.
   */
  ngOnInit(): void {
    if(this.currentTimer !== null){
      this.currentTimer.unsubscribe();
    }
    this.currentTimer = timer(this.delay).subscribe(
      _ => {
        this.loadFavorites();
      }
    );
  }

  /**
   * Handle cleanup on destroy
   */
  ngOnDestroy(): void {
    if(this.currentTimer !== null){ //unsubscribe to ensure no errors occur
      this.currentTimer.unsubscribe();
    }
  }

  /**
   * load favorites of user from backend.
   */
  loadFavorites(): void {
    this.service.getFavorites(this.page, this.size)
      .subscribe({
        next: data => {
          this.favorites = data.body;
        },
        error: error => {
          this.errorService.handleError(error, `Favoriten konnten nicht geladen werden`);
        }
      });
  }

  /**
   * Removes an entry from the array without reloading the favorites.
   *
   * @param entry The favorite to remove
   */
  onEntryDelete(entry: RecipeListElement) {
      const index = this.favorites.findIndex(d => d.id === entry.id);
      this.favorites.splice(index, 1);
      const count = this.favorites.length;
      this.countChanged.emit();
      this.loadFavorites();
  }

  /**
   * Reloads favorites on changes (this is for pagination).
   */
  ngOnChanges() {
    this.favorites = [];
    if(this.currentTimer !== null){
      this.currentTimer.unsubscribe();
    }
    this.currentTimer = timer(this.delay).subscribe(
      _ => {
        this.loadFavorites();
      }
    );
  }

}
