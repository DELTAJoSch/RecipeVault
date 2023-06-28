import {animate, state, style, transition, trigger } from '@angular/animations';
import { Component, EventEmitter, Input, OnChanges, OnDestroy, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription, timer } from 'rxjs';
import { RecipeListElement } from 'src/app/dtos/recipe-list-element';
import { ErrorService } from 'src/app/services/error.service';
import { ListService } from 'src/app/services/list.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
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
export class ListComponent implements OnDestroy, OnChanges{
  @Input() page;
  @Input() size;
  @Input() listName;
  @Input() delay = 250;

  @Output() countChanged= new EventEmitter();

  errorMessage = '';
  recipes: RecipeListElement[] = [];
  maxPageNum = 0;

  currentTimer: Subscription = null;

  startupDelayCompleted = false;

  constructor(
    private service: ListService,
    private notification: ToastrService,
    private router: Router,
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
    if(this.currentTimer !== null){ //unsubscribe to ensure no errors occur
      this.currentTimer.unsubscribe();
    }
  }

  /**
   * load recipes of selected list.
   */
  loadRecipes() {
    if(this.listName !== 'Liste auswählen' && this.listName !== undefined) {
      this.service.getRecipesOfList(this.listName, this.page, this.size)
        .subscribe({
          next: data => {
            this.recipes = data.body;
            const count = Number(data.headers.get('X-Total-Count')) ?? Number.MAX_VALUE;
          },
          error: error => {
            this.errorService.handleError(error,`Rezepte konnten nicht geladen werden`, '/list');
            this.recipes = [];
          }
        });
    }
  }

  /**
   * Remove a recipe from the array.
   *
   * @param rec Recipe to remove
   */
  onEntryDelete(rec) {
    const index = this.recipes.findIndex(d => d.id === rec.id);
    this.recipes.splice(index, 1);
    this.countChanged.emit(1);
    this.loadRecipes();
  }

  /**
   * Reload recipes on changes (another list selected).
   */
  ngOnChanges() {
    this.recipes = [];
    if(this.currentTimer !== null){
      this.currentTimer.unsubscribe();
    }
    this.currentTimer = timer(this.delay).subscribe(
      _ => {
        this.loadRecipes();
      }
    );
  }

}
