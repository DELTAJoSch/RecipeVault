import {Component, Inject, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Ingredient, IngredientMatchingCategory} from 'src/app/dtos/ingredient';
import {IngredientService} from '../../services/ingredient.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-ingredient',
  templateUrl: './ingredient.component.html',
  styleUrls: ['./ingredient.component.scss']
})
export class IngredientComponent implements OnInit{

  modeSwitch = false;
  values;
  categories;

  ingredient: Ingredient = {
    name: null,
    category: IngredientMatchingCategory.cheeseCream,
    id: null
  };

  constructor(
    private ingredientService: IngredientService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    public dialogRef: MatDialogRef<IngredientComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private errorService: ErrorService,
  ){
    this.values = Object.values;
    this.categories = IngredientMatchingCategory;
  }

  /**
   * Initialize component
   */
  ngOnInit(): void {
    this.ingredient.name = this.data.name;
    this.ingredient.id = this.data.id;
    this.ingredient.category = this.data.category;
    if (this.ingredient.id != null) {
      this.modeSwitch = true;
    }
  }

  /**
   * Gets called when a new ingredient should be added to the system.
   *
   * @param form the form to submit
   */
  onSubmit(form: NgForm) {
    if(form.valid){
      this.ingredientService.createIngredient(this.ingredient).subscribe({
          next: _ => {
            this.notification.success('Zutat hinzugefügt!');
            this.dialogRef.close({data:this.ingredient});
          },
          error: error => {
            this.errorService.handleError(error,`Zutat konnte nicht hinzugefügt werden`);
          }
        });
    } else{
      this.notification.warning('Es fehlen Daten zur erstellung!');
    }
  }

  /**
   * Gets called when an ingredient should be updated.
   *
   * @param form the form for the update
   */
  onUpdate(form: NgForm) {
    if(form.valid){
      this.ingredientService.updateIngredient(this.ingredient, this.ingredient.id).subscribe({
        next: _ => {
          this.notification.success('Zutat erneuert!');
          this.dialogRef.close({data: {id: this.ingredient.id, name: this.ingredient.name, category: this.ingredient.category}});
        },
        error: error => {
          this.errorService.handleError(error, 'Zutat konnte nicht erneuert werden');
        }
      });
    } else{
      this.notification.warning('Es fehlen Daten zur Erneuerung!');
    }
  }
}

