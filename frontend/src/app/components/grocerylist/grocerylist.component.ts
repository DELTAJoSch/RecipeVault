import {animate, state, style, transition, trigger } from '@angular/animations';
import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription, timer } from 'rxjs';
import { Amount } from 'src/app/dtos/amount';
import { List } from 'src/app/dtos/list';
import { RecipeListElement } from 'src/app/dtos/recipe-list-element';
import { GroceryListService } from 'src/app/services/grocerylist.service';
import { ListService } from 'src/app/services/list.service';
import { ShareGroceryListService } from 'src/app/services/share-grocery-list.service';

@Component({
  selector: 'app-grocerylist',
  templateUrl: './grocerylist.component.html',
  styleUrls: ['./grocerylist.component.scss'],
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
export class GrocerylistComponent implements OnInit, OnDestroy{
  @Input() delay = 250;

  @Output() groceryList: Amount[];
  listName: string;
  lists: List[] = [];
  recipes: RecipeListElement[] = [];
  localPortion: number[] = [];
  globalPortions: number = null;

  currentTimer: Subscription = null;

  startupDelayCompleted = false;

  constructor(
    private listService: ListService,
    private notification: ToastrService,
    private router: Router,
    private shareService: ShareGroceryListService,
    private groceryListService: GroceryListService,
  ) {
    setTimeout(() => {
        this.startupDelayCompleted = true;
      },
      this.delay
    );
  }

  /**
   * load lists from backend on init
   */
  ngOnInit(): void {
    this.loadLists();
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
   * load all lists of the user
   */
  loadLists() {
    this.listService.getLists().subscribe({
      next: data => {
        this.lists = data;
      },
      error: error => {
        let msg = '';
        if (typeof error.error === 'object') {
          if(error.error.error !== null) {
            msg = error.error.error;
          }
        } else {
          if(error.error !== null) {
            msg = error.error;
          }
        }
        msg = (msg)? msg : 'Konnte Listen nicht laden';
        this.notification.error(msg);
      }
    });
  }

  /**
   * sets the listname to the selected value
   *
   * @param value selected listname
   */
  onOptionsSelected(value: string){
    this.listName = value;
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

  /**
   * load recipes of list from backend
   */
  loadRecipes() {
      if(this.listName !== 'Liste auswählen' && this.listName !== undefined) {
        this.listService.getRecipesOfList(this.listName)
          .subscribe({
            next: data => {
              this.recipes = data.body;
              for (let i = 0; i < this.recipes.length; i++) {
                this.localPortion[i] = undefined;
              }
              this.globalPortions = undefined;
            },
            error: error => {
              this.recipes = [];
              let msg = '';
              if (typeof error.error === 'object') {
                if(error.error.error !== null) {
                  msg = error.error.error;
                }
              } else {
                if(error.error !== null) {
                  msg = error.error;
                }
              }
              msg = (msg)? msg : 'Konnte Rezepte nicht laden';
              this.notification.error(msg);
            }
          });
      } else {
        this.recipes = [];
      }
    }

  /**
   * generate the grocery list
   */
  generateGroceryList(): void {
    let portionMissing = false;
    this.localPortion.forEach(num => {
      if (num === undefined) {
        portionMissing = true;
      }
    });
    if (portionMissing) {
      this.notification.error('Bitte eine Portionenanzahl für alle Rezepte setzen.');
      return;
    }
    const portionsPerRecipe = [];
    for (let i = 0; i < this.recipes.length; i++) {
      const rec = this.recipes[i];
      portionsPerRecipe[i] = [rec.id, this.localPortion[i]];
    }
    this.groceryListService.generateGroceryList(portionsPerRecipe).subscribe({
      next: data => {
        this.groceryList = data;
        this.shareService.setGroceryList(this.groceryList);
        this.router.navigateByUrl('/grocerylist/print');
      },
      error: error => {
        let msg = '';
        if (typeof error.error === 'object') {
          if(error.error.error !== null) {
            msg = error.error.error;
          }
        } else {
          if(error.error !== null) {
            msg = error.error;
          }
        }
        msg = (msg)? msg : 'Fehler beim Listen generieren';
        this.notification.error(msg);
      }
    });
  }

  /**
   * Globally sets all portions to the given value.
   *
   * @param portions to set to
   */
  onGlobalPortionsSet(portions: any) {
    const isNumber = !isNaN(parseInt(portions, 10));
    if (portions % 1 === 0 && portions >= 0 && !isNaN(Number(portions)) && isNumber) {
      for (let i = 0; i < this.recipes.length; i++) {
        this.localPortion[i] = portions;
      }
    } else {
      this.notification.error('Bitte ganze nicht negative Zahlen eingeben.');
    }
  }

  /**
   * Sets the portion for a recipe in the localPortion array.
   *
   * @param portions new value for portion
   * @param index of recipe in array
   */
  onLocalPortionSet(portions: any, index: number) {
    const isNumber = !isNaN(parseInt(portions, 10));
    if (portions % 1 === 0 && portions >= 0 && isNumber) {
      this.localPortion[index] = portions;
    } else {
      this.localPortion[index] = undefined;
      this.notification.error('Bitte ganze nicht negative Zahlen eingeben.');
    }
  }
}
