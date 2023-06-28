import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription, timer } from 'rxjs';
import { RecipeDetails } from 'src/app/dtos/recipe-details';
import { RecipeService } from 'src/app/services/recipe.service';
import {AuthService} from '../../services/auth.service';
import { ErrorService } from 'src/app/services/error.service';
import { RecipeListElement } from 'src/app/dtos/recipe-list-element';
import { ImageService } from 'src/app/services/image.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy, AfterViewInit {
  @ViewChild('carousel')
  carousel: ElementRef;


  delay = 250;

  currentTimer: Subscription = null;

  startupTimer: Subscription = null;
  startupDelayCompleted = false;


  recipes: RecipeListElement[] = [];
  images: Map<number, string> = new Map<number, string>();
  constructor(
    public authService: AuthService,
    private recipeService: RecipeService,
    private notification: ToastrService,
    private router: Router,
    private errorService: ErrorService,
    private imageService: ImageService,
  ) {
    this.startupTimer = this.currentTimer = timer(this.delay).subscribe(
      _ => {
        this.loadRecipes();
        this.startupDelayCompleted = true;
        this.startupTimer.unsubscribe();
      }
    );
  }

  /**
   * set images on init.
   */
  ngOnInit() {
    if (!this.authService.isLoggedIn()) {
      this.router.navigateByUrl('/login');
    }
  }

  /**
   * start carousel after view init
   */
  ngAfterViewInit() {
    this.carousel = this.carousel.nativeElement.carousel;
  }


  /**
   * loads image from backend if there is an imageId
   */
  loadImages(){
    for (let i = 0; i < this.recipes.length; i++) {
      if(this.recipes[i].imageId != null){
        this.imageService.getImage(this.recipes[i].imageId).subscribe({
          next : imageBlob => {
            this.images.set(i, URL.createObjectURL(imageBlob));
          }, error: err => {
            this.errorService.handleError(err,'Bild konnte nicht geladen werden');
          }
        });
      } else{
        this.images.set(i, '../../assets/images/pizza.jpg');
      }
    }
  }

  /**
   * Handle cleanup on destroy
   */
  ngOnDestroy(): void {
    if(this.currentTimer !== null){ //unsubscribe to ensure no errors occur
      this.currentTimer.unsubscribe();
    }
  }

  /**
   * load recipes for carousel.
   */
  loadRecipes(): void {
    this.recipeService.getRecipes(0, 5).subscribe({
      next: resp => {
        this.recipes = resp.body;
        this.loadImages();
      },
      error: error => {
        this.errorService.handleError(error,'Rezepte konnten nicht geladen werden');
      }
    });
  }

  /**
   * get short description of recipe
   *
   * @param index of recipe in recipes array
   */
  getShortDescription(index: number): string {
    return this.recipes[index].shortDescription? this.recipes[index].shortDescription : '';
  }
}
