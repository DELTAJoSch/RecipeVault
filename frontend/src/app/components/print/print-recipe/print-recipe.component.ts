import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Difficulty, RecipeDetails} from 'src/app/dtos/recipe-details';
import {WineCategory} from '../../../dtos/wine';
import {RecipeService} from '../../../services/recipe.service';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-print-recipe',
  templateUrl: './print-recipe.component.html',
  styleUrls: ['./print-recipe.component.scss']
})
export class PrintRecipeComponent implements OnInit{
  id: number;
  people: number;
  recipe: RecipeDetails = {
    id: 0,
    name: '',
    shortDescription: '',
    description: '',
    owner: undefined,
    difficulty: Difficulty.easy,
    ingredients: [],
    recommendationConfidence: 0,
    recommendedCategory: WineCategory.lightWhite,
    imageId: null
  };

  constructor(
    private router: Router,
    private notification: ToastrService,
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private errorService: ErrorService,
  ){}

  /**
   * Initializes this component
   */
  ngOnInit(){
      this.id = Number(this.route.snapshot.paramMap.get('id'));
      this.people = Number(this.route.snapshot.paramMap.get('people'));

      this.recipeService.getRecipeById(this.id).subscribe({
        next: recipe => {
          this.recipe = recipe;
        },
        error: err => {
          this.errorService.handleError(err,`Rezept konnte nicht geladen werden`);
          this.router.navigateByUrl('/recipe/' + this.id + '/details');
        }
      });
  }

  /**
   * Opens the print view
   */
  print(){
    const content = document.getElementById('printContent');
    const windowRef = window.open('', '', 'left=0,top=0,width=900,height=900,toolbar=0,scrollbars=0,status=0');

    windowRef.document.write('<style> .desc{white-space: pre-wrap}</style>' + content.innerHTML);
    windowRef.document.close();
    windowRef.focus();
    windowRef.print();
    windowRef.close();
  }

  /**
   * Navigates back to the recipe's details page
   */
  back(){
      this.router.navigateByUrl(`/recipe/${this.recipe.id}/details`).then(
        fulfilled => {},
        failed => {
          this.notification.error('Navigation nicht erfolgreich. Startseite gewählt.');
          this.router.navigateByUrl('/');
        }
      );
  }
}
