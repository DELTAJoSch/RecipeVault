import {Component, EventEmitter, HostListener, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Ingredient, IngredientMatchingCategory} from '../../../dtos/ingredient';
import {IngredientService} from '../../../services/ingredient.service';
import {IngredientComponent} from '../ingredient.component';
import {MatDialog} from '@angular/material/dialog';
import {AuthService} from '../../../services/auth.service';
import {ErrorService} from '../../../services/error.service';

@Component({
  selector: 'app-ingredient-item',
  templateUrl: './ingredient-item.component.html',
  styleUrls: ['./ingredient-item.component.scss']
})
export class IngredientItemComponent implements OnInit{
  @Input() ingredient: Ingredient = {
    id: 0,
    name: '',
    category: IngredientMatchingCategory.undefined
  };

  @Output() delete: EventEmitter<void> = new EventEmitter<void>();

  mobile = false;

  constructor(
    private router: Router,
    private ingredientService: IngredientService,
    private authService: AuthService,
    private notification: ToastrService,
    private dialog: MatDialog,
    private errorService: ErrorService,
  ){}

  @HostListener('window:resize')
  onResize() {
    this.mobile = window.innerWidth <= 900;
  }

  ngOnInit() {
    this.mobile = window.innerWidth <= 900;
  }

  /**
   * Gets called when an ingredient should be updated, opens update-dialog.
   */
  onUpdateClicked() {
    const dialogRef = this.dialog.open(IngredientComponent, {
      data: this.ingredient,
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result.data !== undefined) {
        this.ingredient = result.data;
      }
    });
  }

  /**
   * Gets called when an ingredient should be deleted.
   */
  onDeleteClicked() {
    this.ingredientService.deleteIngredient(this.ingredient.id).subscribe({
      next: _=> {
        this.notification.success(`Zutat ${this.ingredient.name} gelöscht!`);
        this.delete.emit();
      },
      error: error => {
        this.errorService.handleError(error, 'Zutat wurde nicht gelöscht');
      }
    });
  }

  /**
   * Returns if current user is an admin.
   */
  isAdmin() {
    return this.authService.isAdmin();
  }
}
