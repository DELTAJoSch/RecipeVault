import {AfterViewInit, Component, OnInit, Output } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import {FavoriteService} from '../../../services/favorite.service';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-favorite-overview',
  templateUrl: './favorite-overview.component.html',
  styleUrls: ['./favorite-overview.component.scss']
})
export class FavoriteOverviewComponent implements OnInit, AfterViewInit{
  @Output() page = 0;
  @Output() size = 12;
  maxPageNum = 0;
  maxElements = 0;


  constructor(
    private service: FavoriteService,
    private notification: ToastrService,
    private errorService: ErrorService,
  ){}

  /**
   * loads the count of favorites on init, and sets the maximum pagenumber.
   */
  ngOnInit(): void {
    this.service.getCount().subscribe({
      next: num => {
        this.maxPageNum = Math.ceil(num/this.size) - 1;
        this.maxElements = num;
      },
      error: error => {
        this.errorService.handleError(error, `Anzahl der Favoriten konnte nicht geladen werden`);
      }
    });
  }

  /**
   * scrolls to top after view has loaded.
   */
  ngAfterViewInit() {
    (document.getElementById('headerBar') as HTMLElement).scrollIntoView({behavior: 'smooth', block: 'start', inline: 'nearest'});
  }

  /**
   * decreases pagenumber when corresponding button is clicked.
   */
  onPreviousClick() {
    if(this.page > 0){
      this.page--;
    }
  }

  /**
   * increases pagenumber when corresponding button is clicked.
   */
  onNextClick() {
    if(this.page < this.maxPageNum){
      this.page++;
    }
  }

  /**
   * If the element count has changed, this is called.
   */
  onCountChanged(){
    this.maxElements--;
    if(this.maxElements % this.size === 0) {
      if(this.page > 0) {
        this.page--;
      }
    }
    this.maxPageNum = Math.ceil(this.maxElements/this.size) - 1;

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
}
