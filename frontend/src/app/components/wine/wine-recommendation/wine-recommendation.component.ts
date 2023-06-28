import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { WineCategory } from 'src/app/dtos/wine';

@Component({
  selector: 'app-wine-recommendation',
  templateUrl: './wine-recommendation.component.html',
  styleUrls: ['./wine-recommendation.component.scss']
})
export class WineRecommendationComponent implements OnInit{
  recommendedCategory: WineCategory = WineCategory.aromaticWhite;

  page = 0;
  size = 10;
  maxPageNum = 0;
  maxElements = 0;

  currentWidth = 0;
  mobileExpanded = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ){}

  @HostListener('window:resize')
  onResize() {
    this.currentWidth = window.innerWidth;
  }

  /**
   * Initialize component based on selected mode
   */
  ngOnInit(): void {
    this.route.params.subscribe(param => {
      const val = param['category'];
      const cat = WineCategory[val];

      if(cat === undefined || cat === null){
        this.router.navigateByUrl('/wine');
      }

      this.recommendedCategory = cat;
    });


    this.currentWidth = window.innerWidth;
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
  }

  /**
   * Get the upper bound of the currently displayed elements.
   *
   * @returns Returns the upper element bound
   */
  getElementUpperBound(): number {
    if(this.page < this.maxPageNum){
      return this.page * this.size;
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
    if(this.maxElements !== 0){
      return this.page*this.size + 1;
    } else {
      return 0;
    }
  }

  /**
   * Checks whether mobile view needs to be displayed for this component
   *
   * @returns True, if mobile view is necessary
   */
  isMobile(): boolean {
    return this.currentWidth < 1000;
  }

  /**
   * Toggle the state of the mobile expansion for the category description
   */
  expandToggle(){
    this.mobileExpanded = !this.mobileExpanded;
  }
}
