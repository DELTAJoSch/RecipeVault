import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { List } from 'src/app/dtos/list';
import { FavoriteService } from 'src/app/services/favorite.service';
import { ListService } from 'src/app/services/list.service';
import { Difficulty, RecipeDetails } from '../../../dtos/recipe-details';
import { RecipeService } from '../../../services/recipe.service';
import { Amount, AmountUnit } from '../../../dtos/amount';
import { WineCategoryEnumPipe } from '../../../pipes/wine-category-enum.pipe';
import { Note } from '../../../dtos/note';
import { NotesService } from '../../../services/notes.service';
import { MatDialog } from '@angular/material/dialog';
import { NotesComponent } from 'src/app/components/notes/notes.component';
import { ErrorService } from 'src/app/services/error.service';
import {ImageService} from '../../../services/image.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-recipe-details',
  templateUrl: './recipe-details.component.html',
  styleUrls: ['./recipe-details.component.scss']
})
export class RecipeDetailsComponent implements OnInit {
  id: number = null;

  shortDescriptionText = 'Hier sollte die Kurzbeschreibung stehen';
  descriptionGuide = 'Hier sollte die Anleitung stehen';

  lists: List[] = [];
  ingredients: Amount[] = [];

  recipe: RecipeDetails = {
    id: null,
    name: null,
    favorite:false,
    shortDescription: null,
    description: null,
    difficulty: Difficulty.easy,
    ingredients: null,
    owner: null,
    recommendationConfidence: null,
    recommendedCategory: null,
    imageId: null
  };

  imageUrl: string = null;

  people = 1;
  recommendationCategory = '';

  note: Note = {
    creatorId: null,
    recipeId: 0,
    content: '',
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private recipeService: RecipeService,
    private favoriteService: FavoriteService,
    private listService: ListService,
    private noteService: NotesService,
    private imageService: ImageService,
    private notification: ToastrService,
    private pipe: WineCategoryEnumPipe,
    private dialog: MatDialog,
    private errorService: ErrorService,
    private authService: AuthService,
  ) { }

  /**
   * Initialize component
   */
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    this.recipeService.getRecipeById(this.id).subscribe({
      next: recipe => {
        this.recipe = recipe;
        if(recipe.shortDescription !== null) {
          this.shortDescriptionText = this.recipe.shortDescription;
        }
        if(recipe.description !== null) {
          this.descriptionGuide = this.recipe.description;
        }
        this.ingredients = recipe.ingredients;
        this.recommendationCategory = (recipe.recommendedCategory) ? recipe.recommendedCategory.toString() : '';

        if (recipe.imageId != null){
          this.loadImage();
        }
      },
      error: err => {
        this.errorService.handleError(err,`Rezept konnte nicht geladen werden`, '/recipe');
      }
    });

    this.loadNotes();
  }

  /**
   * get amount of ingredient
   *
   * @param ingredient to get amount of
   */
  getAmount(ingredient: Amount): string{
    return (this.people * ingredient.amount).toString();
  }

  /**
   * get unit of ingredient
   *
   * @param ingredient to get unit of
   */
  getUnit(ingredient: Amount): AmountUnit{
    return ingredient.unit;
  }


  /**
   * returns name of ingredient
   *
   * @param ingredient to get name of
   */
  getIngredient(ingredient: Amount): string{
    return ingredient.ingredient.name;
  }

  /**
   * returns if there is a description available for the recipe
   */
  descriptionAvailable(): boolean{
    return (this.recipe.description !== null) && (this.recipe.description.length !== 0);
  }

  /**
   * Adds a recipe to the user's favorites.
   *
   * @param id of the recipe
   */
  addToFavorites(id: number) {
    this.favoriteService.addFavorite(id).subscribe({
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
   *
   * @param id of the recipe
   */
  removeFromFavorites(id: number) {
    this.favoriteService.remove(id).subscribe({
      next: _ => {
        this.notification.success('Erfolgreich enfernt!');
        this.recipe.favorite = false;
      },
      error: error => {
        this.errorService.handleError(error,`Rezept "${this.recipe.name}" konnte nicht von Favoriten entfernt werden`);
      }
    });
  }

  /**
   * add the recipe to the selected list
   *
   * @param value name of selected list
   */
  addToList(value: string) {
    if (value !== 'Liste auswählen') {
      this.listService.addToList(value, this.recipe.id).subscribe({
        next: _ => {
          this.notification.success('Erfolgreich zu Liste hinzugefügt!');
        },
        error: error => {
        this.errorService.handleError(error,`Rezept "${this.recipe.name}" konnte nicht zu Liste "${value}" hinzugefügt werden`);
        }
      });
    } else {
      this.notification.error('Bitte Liste auswählen');
    }

  }

  /**
   * load all userdefined lists.
   */
    loadLists() {
      this.listService.getLists().subscribe({
        next: data => {
          this.lists = data;
        },
        error: error => {
          this.errorService.handleError(error,`Listen konnten nicht geladen werden`);
        }
      });
    }

  /**
   * navigates to wine recommendations for recipe
   */
  getWineRecommendations(){
    this.router.navigateByUrl('wine/recommendations/' + this.pipe.transform(this.recipe.recommendedCategory));
    }

  /**
   * navigates to print view of recipe
   */
  print(){
    this.router.navigateByUrl('/print/' + this.recipe.id + '/' + this.people);
    }

  /**
   * loads the notes for this recipe
   */
  loadNotes(){
    this.noteService.getNotes(this.id).subscribe({
      next: data => {
        this.note = data;
      },
      error: error => {
        this.errorService.handleError(error,`Notiz konnte nicht geladen werden`);
      }
    });
  }

  /**
   * returns if there is already a note
   */
  noteIsPresent(): boolean{
    return this.note.content !== '';
  }

  /**
   * opens the dialog for creating a note
   */
  openNoteCreator() {
    const dialogRef = this.dialog.open(NotesComponent, {
      height: '45%',
      width: '70%',
      data: this.note
    });
    dialogRef.componentInstance.event.subscribe(next => {
      this.note = next;
      this.loadNotes();
    });
  }

  /**
   * deletes note
   */
  deleteNote() {
    this.noteService.deleteNote(this.id).subscribe({
      next: _ => {
        this.note.content = '';
        this.note.creatorId = null;
        this.notification.success('Notiz gelöscht!');
      },
      error: err => {
        this.errorService.handleError(err);
      }
    });
  }

  /**
   * navigates to author details page
   */
  onAuthorClick() {
    this.router.navigateByUrl('author/' + this.recipe.author.id + '/details');
  }

  /**
   * returns the name of the author of the recipe
   */
  getAuthorName() {
    return (this.recipe.author !== null && this.recipe.author !== undefined)?
      (this.recipe.author.firstname + ' ' + this.recipe.author.lastname) : '';
  }

  /**
   * returs the email of the owner of the recipe
   */
  getOwnerMail() {
    return (this.recipe.owner !== null)? this.recipe.owner.email : '';
  }

  /**
   * loads image from backend if there is an imageId
   */
  loadImage(){
    if(this.recipe.imageId != null){
      this.imageService.getImage(this.recipe.imageId).subscribe({
        next : imageBlob => {
          this.imageUrl = URL.createObjectURL(imageBlob);
        }, error: err => {
          this.errorService.handleError(err,'Bild konnte nicht geladen werden');
        }
      });
    }
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
}
