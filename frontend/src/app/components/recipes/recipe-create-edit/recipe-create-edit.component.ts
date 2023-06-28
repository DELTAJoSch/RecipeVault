import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Difficulty, RecipeDetails} from '../../../dtos/recipe-details';
import {RecipeService} from '../../../services/recipe.service';
import {IngredientService} from '../../../services/ingredient.service';
import {Amount, AmountUnit} from '../../../dtos/amount';
import {MatDialog} from '@angular/material/dialog';
import {OcrService} from 'src/app/services/ocr-service.service';
import {Author} from '../../../dtos/author';
import {of} from 'rxjs';
import {AuthorService} from '../../../services/author.service';
import {ErrorService} from 'src/app/services/error.service';
import {Ingredient} from '../../../dtos/ingredient';
import {ImageService} from '../../../services/image.service';

@Component({
  selector: 'app-recipe-create-edit',
  templateUrl: './recipe-create-edit.component.html',
  styleUrls: ['./recipe-create-edit.component.scss']
})
export class RecipeCreateEditComponent implements OnInit, AfterViewInit {
  @Input() create = true; // Input property for determining if it's a create or edit mode
  @Input() ocr = true; // Input property for determining if OCR is enabled
  showSpinner = false; // Flag for showing/hiding spinner

  id: number = null; // Recipe ID

  personCountString = '1'; // Person Count

  difficulty = Difficulty; // Difficulty enum
  values: any;

  newIng: Ingredient; // Variable for a new ingredient

  shortDescriptionText = ''; // Variable for the short description text
  descriptionGuide = ''; // Variable for the description guide text

  ingredientsList: Amount[] = new Array(); // Array for storing ingredient amounts

  selectedFile: File;

  imageUrl: string = null;

  newImageUrl: string = null;

  imageDelete = false;

  imageToLarge = false;

  maxSizeInBytes: number = 20 * 1024 * 1024; // 20MB

  recipe: RecipeDetails = { // Recipe object
    id: null,
    name: null,
    shortDescription: null,
    description: null,
    owner: null,
    difficulty: Difficulty.easy,
    ingredients: null,
    recommendationConfidence: null,
    recommendedCategory: null,
    imageId: null
  };

  constructor(
    private recipeService: RecipeService,
    private ingredientService: IngredientService,
    private ocrService: OcrService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private dialog: MatDialog,
    private authorService: AuthorService,
    private errorService: ErrorService,
    private imageService: ImageService
  ){
    this.values = Object.values; // Get Object values
    this.difficulty = Difficulty; // Assign Difficulty enum
  }

  /**
   * Initialize component based on selected mode
   */
  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.create = data.create; // Set create mode
      this.ocr = data.ocr; // Set OCR mode
    });

    // If the mode is edit, load recipe from backend
    if (this.create === false) {
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
          this.ingredientsList = recipe.ingredients;

          // Add empty ingredient to the back
          const amount: Amount = new Amount();
          amount.unit = AmountUnit.kg;
          this.ingredientsList.push(amount);

          this.ingredientsList = recipe.ingredients;
          if (recipe.imageId != null) {
            this.loadImage();
          }
        },
        error: error => {
          this.errorService.handleError(error,`Rezept konnte nicht geladen werden`, '/recipe');
        }
      });
    } else if (this.ocr === true) {
      this.id = Number(this.route.snapshot.paramMap.get('id'));
      this.showSpinner = true;
      this.ocrService.executeOcr(this.id).subscribe({
        next: res => {
          this.recipe.description = res.description;
          this.recipe.ingredients = res.ingredients;
          this.ingredientsList = this.recipe.ingredients;
          this.descriptionGuide = this.recipe.description;
          this.showSpinner = false;

          // Add empty ingredient to the back
          const amount: Amount = new Amount();
          amount.unit = AmountUnit.kg;
          this.ingredientsList.push(amount);
        },
        error: error => {
          this.showSpinner = false;
          this.errorService.handleError(error,`OCR-KI Bearbeitung fehlgeschlagen:`, '/ocr/upload',);
        }
      });
    } else {
      const amount: Amount = new Amount();
      amount.unit = AmountUnit.kg;
      this.ingredientsList.push(amount);
    }

  }

  ngAfterViewInit() {
    (document.getElementById('headerBar') as HTMLElement).scrollIntoView({behavior: 'smooth', block: 'start', inline: 'nearest'});
  }

  async onSubmit(form: NgForm) {
    const numberRe = /^[0-9]+$/;
    if(!numberRe.test(this.personCountString)){
      this.notification.warning('Die Personenanzahl wurde nicht angegeben!');
      return;
    }

    // Filter empty ingredients to remove empty ones
    this.ingredientsList = this.ingredientsList.filter(ingredient => {
      if (ingredient.ingredient === null || ingredient.ingredient === undefined) {
        return false;
      }
      return (ingredient.ingredient.name !== null && ingredient.ingredient.name !== undefined && ingredient.ingredient.name !== '');
    });

    if(form.valid){
      this.recipe.description = this.descriptionGuide;
      this.recipe.shortDescription = this.shortDescriptionText;

      // Change amounts before sending!
      const persons = Math.round(Number(this.personCountString));
      this.ingredientsList.forEach((ingredient) => {
        ingredient.amount = ingredient.amount / persons;
      });

      this.recipe.ingredients = this.ingredientsList;

      if (this.imageDelete && !this.selectedFile) {
        this.deleteImage();
      }

      await this.uploadImage();

      if(this.create){
        this.recipeService.createRecipe(this.recipe).subscribe({
          next: _ => {
            this.notification.success('Rezept hinzugefügt!');
            this.router.navigateByUrl('/recipe');
          },
          error: err => {
            this.errorService.handleError(err,`Rezept konnte nicht erstellt werden`);
            // add empty ingredient to back
            const amount: Amount = new Amount();
            amount.unit = AmountUnit.kg;
            this.ingredientsList.push(amount);
          }
        });
      }else{
        this.recipeService.updateRecipe(this.recipe, this.id).subscribe({
          next: _ => {
            this.notification.success('Änderungen gespeichert!');
            this.router.navigateByUrl('/recipe');
          },
          error: err => {
            this.errorService.handleError(err,`Rezept konnte nicht geändert werde`);
            // add empty ingredient to back
            const amount: Amount = new Amount();
            amount.unit = AmountUnit.kg;
            this.ingredientsList.push(amount);
          }
        });
      }
    } else {
      this.notification.warning('Es fehlen Daten zur Erstellung!');
    }
  }


  /**
   * Check if the last two ingredients are not used, if so remove the last one
   */
  removeEmptyIngredient() {
    const listLength = this.ingredientsList.length;
    const lastIngredient = this.ingredientsList[listLength - 1].ingredient;

    // last one is empty add one
    if (lastIngredient && lastIngredient.name !== '') {
      const amount: Amount = new Amount();
      amount.unit = AmountUnit.kg;
      this.ingredientsList.push(amount);
    } else if (listLength > 1) {
      const l2Ingredient = this.ingredientsList[listLength - 2].ingredient;

      // last two are empty remove one
      if (!l2Ingredient || l2Ingredient.name === '') {
        this.ingredientsList.pop();
      }
    }

  }

  // Remove an element from the ingredient list
  deleteIngredientById(amount: Amount) {
    const id = this.ingredientsList.indexOf(amount);
    const listLength = this.ingredientsList.length;
    if (listLength - 1 !== id) { // Don't remove the last element
      this.ingredientsList.splice(id, 1);
    }
  }

  // autocompletion for authors
  authorSuggestions = (input: string) => (input === '')
    ? of([])
    : this.authorService.searchByName(input);

  public formatAuthorName(author: Author | null | undefined): string {
    return (author == null)
      ? ''
      : `${author.firstname + ' ' + author.lastname}`;
  }

  /**
   * If a file gets selected makes it the selected File and generates an Url so it can be displayed
   *
   * @param event that a file gets selected from directory
   */
  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file && file.size <= this.maxSizeInBytes) {
      this.imageToLarge = false;
      // File size is within the allowed limit
      this.selectedFile = file;
      this.newImageUrl = URL.createObjectURL(this.selectedFile);
    } else {
      // File size exceeds the allowed limit
      this.selectedFile = null;
      this.imageToLarge = true;
    }
  }

  /**
   *  Saves or updates an image in the backend and lets the OnSubmit wait till it is finished
   *  If it saves an image it gives the recipe an imageId
   */
  uploadImage(): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      if (this.selectedFile != null) {
        if (this.recipe.imageId != null) {
          this.imageService.updateImage(this.selectedFile, this.recipe.imageId).subscribe(
            () => {
              resolve(); // Image upload completed, resolve the promise
            },
            error => {
              this.errorService.handleError(error,'Bild konnte nicht gespeichert werden');
              reject(error); // Image upload failed, reject the promise
            }
          );
        } else {
          this.imageService.saveImage(this.selectedFile).subscribe(
            value => {
              this.recipe.imageId = value;
              resolve(); // Image upload completed, resolve the promise
            },
            error => {
              this.errorService.handleError(error,'Bild konnte nicht gespeichert werden');
              reject(error); // Image upload failed, reject the promise
            }
          );
        }
      } else {
        resolve(); // No image selected, resolve the promise
      }
    });
  }

  /**
   * Deletes image in backend and sets imageId to null
   */
  deleteImage() {
    if (this.recipe.imageId != null) {
      this.imageService.deleteImage(this.recipe.imageId).subscribe();
      this.recipe.imageId = null;
    }
  }

  /**
   * loads image from backend if there is an imageId
   */
  loadImage() {
    if (this.recipe.imageId != null) {
      this.imageService.getImage(this.recipe.imageId).subscribe({
        next: imageBlob => {
          this.imageUrl = URL.createObjectURL(imageBlob);
        }, error: err => {
          this.errorService.handleError(err,'Bild konnte nicht geladen werden');
        }
      });
    }
  }
}
