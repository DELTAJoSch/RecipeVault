import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ApplicationOption, ApplicationOptionType } from 'src/app/dtos/option';
import { ErrorService } from 'src/app/services/error.service';
import { OptionsService } from 'src/app/services/options.service';

@Component({
  selector: 'app-global-options',
  templateUrl: './global-options.component.html',
  styleUrls: ['./global-options.component.scss']
})
export class GlobalOptionsComponent implements OnInit {
  options: ApplicationOption[] = [];
  maxElements = 0;
  maxPageNum = 0;
  page = 0;
  size = 20;
  searchName = '';

  constructor(
    private notification: ToastrService,
    private optionService: OptionsService,
    private errorService: ErrorService,
  ) { }

  /**
   * Initializes this component
   */
  ngOnInit() {
    this.load();
  }

  /**
   * Loads application options
   */
  load() {
    this.optionService.getApplicationOptions(this.searchName, this.page, this.size).subscribe({
      next: resp => {
        this.maxElements = Number(resp.headers.get('X-Total-Count')) ?? Number.MAX_VALUE;
        this.options = resp.body;
        this.maxPageNum = Math.ceil(this.maxElements / this.size) - 1;
      },
      error: error => {
        this.errorService.handleError(error, `Globale Einstellungen konnten nicht geladen werden`);
      }
    });
  }

  /**
   * Advances the view to the next page of options
   */
  pageNext(){
    if(this.page < this.maxPageNum){
      this.page++;
    }
  }

  /**
   * Advances tho the previous page
   */
  pagePrev(){
    if(this.page > 0){
      this.page--;
    }
  }

  /**
   * Get the upper bound of the currently displayed elements.
   *
   * @returns Returns the upper element bound
   */
  getElementUpperBound(): number {
    if (this.page < this.maxPageNum) {
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
    if (this.maxElements !== 0) {
      return this.page * this.size + 1;
    } else {
      return 0;
    }
  }
}
