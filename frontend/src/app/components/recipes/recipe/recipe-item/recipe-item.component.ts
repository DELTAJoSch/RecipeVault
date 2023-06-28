import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {OwnerInfo} from 'src/app/dtos/owner-info';
import {Difficulty, RecipeDetails} from '../../../../dtos/recipe-details';
import {RecipeService} from '../../../../services/recipe.service';
import { ErrorService } from 'src/app/services/error.service';
import { AuthService } from 'src/app/services/auth.service';
import { FavoriteService } from 'src/app/services/favorite.service';
import { RecipeListElement } from 'src/app/dtos/recipe-list-element';
import { ListService } from 'src/app/services/list.service';
import { ImageService } from 'src/app/services/image.service';

@Component({
  selector: 'app-recipe-item',
  templateUrl: './recipe-item.component.html',
  styleUrls: ['./recipe-item.component.scss']
})
export class RecipeItemComponent implements OnInit{
  @Input() recipe: RecipeListElement = {
    id: 0,
    name: '',
    shortDescription: '',
    difficulty: Difficulty.easy,
    favorite: false,
    owner: null,
    imageId: null
  };

  @Input() isInFavorites = false;
  @Input() isInList = false;

  @Input() listName = '';

  @Output() delete: EventEmitter<void> = new EventEmitter<void>();

  imgSrc = '';


  constructor(
    private recipeService: RecipeService,
    private notification: ToastrService,
    private errorService: ErrorService,
    private authService: AuthService,
    private favoriteService: FavoriteService,
    private listService: ListService,
    private imageService: ImageService,
  ){}


  /**
   * loads image on init.
   */
  ngOnInit() {
    if (this.recipe.imageId != null) {
      this.imageService.getImage(this.recipe.imageId).subscribe({
        next: blob => {
          this.imgSrc = URL.createObjectURL(blob);
        },
        error: err => {
          this.errorService.handleError(err, 'Bild konnte nicht geladen werden');
        }
      });
    }
  }


  /**
   * deletes the recipe.
   */
  onDeleteClicked() {
    this.recipeService.deleteRecipe(this.recipe.id).subscribe({
      next: _=> {
        this.notification.success(`Rezept ${this.recipe.name} gelöscht!`);
        this.delete.emit();
      },
      error: err => {
        this.errorService.handleError(err,`Rezept ${this.recipe.name} konnte nicht gelöscht werden`);
      }
    });
  }

  /**
   * returns the short description of the recipe.
   */
  getShortDescription(): string {
    return (this.recipe.shortDescription)? this.recipe.shortDescription : '';
  }

  /**
   * returns if the user is permitted to edit (and delete) the recipe.
   */
  isPermittedToEdit(): boolean {
    if (this.recipe.owner === null) {
      return this.authService.isAdmin();
    }
    return (this.recipe.owner.email === this.authService.getUserEmail()) || this.authService.isAdmin();
  }

  /**
   * Adds a recipe to the user's favorites.
   */
  addToFavorites() {
    this.favoriteService.addFavorite(this.recipe.id).subscribe({
      next: _ => {
        this.notification.success('Erfolgreich zu Favoriten hinzugefügt!' );
        this.recipe.favorite = true;
      },
      error: error => {
        this.errorService.handleError(error,`Rezept "${this.recipe.name}" konnte nicht zu Favoriten hinzugefügt werden`);
      }
    });
  }

  /**
   * Removes a recipe from the user's favorites.
   */
  removeFromFavorites() {
    this.favoriteService.remove(this.recipe.id).subscribe({
      next: _ => {
        this.notification.success('Erfolgreich enfernt!');
        if(this.isInFavorites) {
          this.delete.emit();
        }
        this.recipe.favorite = false;
      },
      error: error => {
        this.errorService.handleError(error,`Rezept "${this.recipe.name}" konnte nicht von Favoriten entfernt werden`);
      }
    });
  }

  /**
   * remove a recipe from a list.
   */
  removeFromList() {
    this.listService.removeFromList(this.listName, this.recipe.id).subscribe({
      next: _ => {
        this.notification.success(`${this.recipe.name} von Liste entfernt!`);
        this.delete.emit();
      },
      error: error => {
        this.errorService.handleError(error,`Rezepte konnten nicht entfernt werden`);
      }
    });
  }
}
