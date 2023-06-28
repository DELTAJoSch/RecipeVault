import { Component} from '@angular/core';
import { User } from 'src/app/dtos/user';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent {

  users: User[];
  page = 0;
  size = 25;
  maxPageNum = 0;
  maxElements = 0;

  constructor(
  ) {
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
    if(this.page < this.maxPageNum){
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
    if(this.maxElements !== 0){
      return this.page*this.size + 1;
    } else {
      return 0;
    }
  }
}
