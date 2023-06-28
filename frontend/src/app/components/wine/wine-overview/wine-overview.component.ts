import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { countries } from 'src/app/data/country-codes';
import { WineCategory } from 'src/app/dtos/wine';
import { WineService } from 'src/app/services/wine.service';

@Component({
  selector: 'app-wine-overview',
  templateUrl: './wine-overview.component.html',
  styleUrls: ['./wine-overview.component.scss'],
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
export class WineOverviewComponent {
  countryList = countries;

  searchName: string | null = null;
  searchVinyard: string | null = null;
  searchCountry: string | null = null;
  searchCategory: WineCategory | null = null;

  page = 0;
  size = 25;
  maxPageNum = 0;
  maxElements = 0;

  searchIsVisible = false;
  categories: typeof WineCategory;
  values: any;

  constructor(
    private wineService: WineService,
    private notification: ToastrService
  ) {
    this.values = Object.values;
    this.categories = WineCategory;
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
    if (this.page > 0) {
      this.page--;
    }
  }

  /**
   * Show next page
   */
  onNextClick() {
    if (this.page < this.maxPageNum) {
      this.page++;
    }
  }

  /**
   * If the element count has changed, this is called.
   *
   * @param count The new number of maximum elements
   */
  elementCountChanged(count: number) {
    this.maxElements = count;
    this.maxPageNum = Math.ceil(count / this.size) - 1;
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
    if (this.page < this.maxPageNum) {
      return (this.page + 1) * this.size;
    } else {
      return this.maxElements;
    }
  }

  /**
   * Get the lower bound of the currently displayed elements.
   *
   * @returns Returns the lower element bound
   */
  getElementLowerBound(): number {
    if (this.maxElements !== 0) {
      return this.page * this.size + 1;
    } else {
      return 0;
    }
  }
}
