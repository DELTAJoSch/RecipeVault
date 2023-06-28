import {Component, OnInit, Output, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { List } from 'src/app/dtos/list';
import { ErrorService } from 'src/app/services/error.service';
import { ListService } from 'src/app/services/list.service';

@Component({
  selector: 'app-list-overview',
  templateUrl: './list-overview.component.html',
  styleUrls: ['./list-overview.component.scss']
})
export class ListOverviewComponent implements OnInit {
  @ViewChild('nameInput') nameInput;
  @Output() listName;

  page = 0;
  size = 12;
  maxPageNum = 0;
  maxElements = 0;

  lists: List[] = [];


  constructor(
    private service: ListService,
    private notification: ToastrService,
    private errorService: ErrorService,
  ) {
  }

  /**
   * loads lists of user on init.
   */
  ngOnInit(): void {
    this.loadLists();
  }

  /**
   * loads all lists of user from backend.
   */
  loadLists() {
    this.service.getLists().subscribe({
      next: data => {
        this.lists = data;
        this.maxPageNum = Math.ceil(this.lists.length/this.size) - 1;
        this.maxElements = this.lists.length;
      },
      error: error => {
        this.errorService.handleError(error, `Listen konnten nicht geladen werden`);
      }
    });
  }

  /**
   * decreases the pagenumber if the corresponding button is clicked.
   */
  onPreviousClick() {
    if (this.page > 0) {
      this.page--;
    }
  }

  /**
   * increases the pagenumber if the corresponding button is clicked.
   */
  onNextClick() {
    if (this.page < this.maxPageNum) {
      this.page++;
    }
  }

  /**
   * If the element count has changed, this is called.
   *
   * @param count of removed recipes
   */
  onCountChanged(count: number) {
    this.maxElements--;
    if(this.maxElements % this.size === 0) {
      if(this.page > 0) {
        this.page--;
      }
    }
    this.maxPageNum = Math.ceil(this.maxElements / this.size) - 1;
  }

  /**
   * Get the upper bound of the currently displayed elements.
   *
   * @returns Returns the upper element bound
   */
  getElementUpperBound(): number {
    if(this.maxElements === 0) {
      return 0;
    }
    if(this.page === this.maxPageNum) {
      return this.maxElements;
    } else if(this.maxElements !== 0){
      return this.page*this.size + this.size;
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

  /**
   * When a new list is selected, the number of recipes in this list is loaded.
   *
   * @param value selected option
   */
  onOptionsSelected(value: string){
    this.listName = value;
    this.page = 0;
    if(this.listName !== 'Liste auswählen') {
      this.service.getCount(value).subscribe({
        next: num => {
          this.maxPageNum = Math.ceil(num / this.size) - 1;
          this.maxElements = num;
        },
        error: error => {
          this.errorService.handleError(error, `Anzahl der Rezepte konnte nicht geladen werden`);
        }
      });
    }
  }


  /**
   * clears the input field of the modal for the creation of new lists.
   */
  clearInput() {
    this.nameInput.nativeElement.value = '';
  }

  /**
   * Creates a new list with the given name.
   *
   * @param name of list to create
   */
  createList(name: string) {
    this.clearInput();
    this.service.createList(name).subscribe({
      next: _ => {
        this.notification.success(`Liste wurde erstellt!`);
        this.lists = [];
        this.loadLists();
      },
      error: error => {
        this.errorService.handleError(error, `Liste ${name} konnte nicht erstellt werden`);
      }
    });
  }

  /**
   * deletes a list.
   */
  deleteList() {
    this.service.deleteList(this.listName).subscribe({
      next: _ => {
        this.notification.success(`${this.listName} gelöscht!`);
        this.listName = 'Liste auswählen';
        this.loadLists();
      },
      error: error => {
        this.errorService.handleError(error, `Liste ${this.listName} konnte nicht gelöscht werden`);
      }
    });
  }
}
