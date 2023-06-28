import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { countries } from 'src/app/data/country-codes';
import {Difficulty} from '../../../dtos/recipe-details';
import {RecipeService} from '../../../services/recipe.service';

@Component({
  selector: 'app-recipe-overview',
  templateUrl: './recipe-overview.component.html',
  styleUrls: ['./recipe-overview.component.scss'],
  animations: [
    trigger('visibleHidden', [
      state(
        'search-visible',
        style({
          opacity: 1,
          scale: 1
        })
      ),
      state(
        'search-hidden, void',
        style({
          opacity: 0,
          scale: 0
        })
      ),
      transition('* => search-visible', animate('1s ease-in-out')),
      transition('* => search-hidden, * => void', animate('500ms ease-in-out'))
    ])
  ]
})
export class RecipeOverviewComponent {
  countryList = countries;

  searchName: string | null = null;
  searchDifficulty: Difficulty | null = null;

  page = 0;
  size = 12;
  maxPageNum = 0;
  maxElements = 0;

  searchIsVisible = false;
  difficulty = Difficulty;
  values: any;

  constructor(
      private recipeService: RecipeService,
      private notification: ToastrService
    ){
      this.values = Object.values;
      this.difficulty = Difficulty;
    }

  /**
   * Toggle Search visibility
   */
  toggleSearch() {
    this.searchIsVisible = !this.searchIsVisible;
  }

  /**
   * Show previous page
   */
  onPreviousClick() {
    if(this.page > 0){
      this.page--;
    }
  }

  /**
   * Show next page
   */
  onNextClick() {
    if(this.page < this.maxPageNum){
      this.page++;
    }
  }

  /**
   * If the element count has changed, this is called.
   *
   * @param count The new number of maximum elements
   */
  elementCountChanged(count: number){
    this.maxElements = count;
    this.maxPageNum = Math.ceil(count/this.size) - 1;
    if(this.page > this.maxPageNum){
      this.page = this.maxPageNum;
    }
    if(this.page < 0 ){
      this.page = 0;
    }
  }

  /**
   * Get the upper bound of the currently displayed elements.
   *
   * @returns Returns the upper element bound
   */
  getElementUpperBound(): number {
    if (this.maxElements === 0) {
      return 0;
    }
    if (this.page === this.maxPageNum) {
      return this.maxElements;
    } else if (this.maxElements !== 0) {
      return this.page * this.size + this.size;
    } else {
      return 0;
    }
  }

  /**
   * Get the lower bound of the currently displayed elements.
   *
   * @returns Returns the lower element bound
   */
  getElementLowerBound(): number {
    if(this.maxElements === 0) {
      return 0;
    }
    if(this.page === 0) {
      return 1;
    } else if(this.page <= this.maxPageNum){
      return this.page * this.size + 1;
    }
  }
}
