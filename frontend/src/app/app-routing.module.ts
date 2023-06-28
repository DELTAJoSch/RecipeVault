import {NgModule, inject} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {WineCreateEditComponent} from './components/wine/wine-create-edit/wine-create-edit.component';
import {WineOverviewComponent} from './components/wine/wine-overview/wine-overview.component';
import {WineDetailsComponent} from './components/wine/wine-details/wine-details.component';
import {IngredientComponent} from './components/ingredient/ingredient.component';
import {UserComponent} from './components/user/user.component';
import {GlobalOptionsComponent} from './components/options/global-options/global-options.component';
import {AuthService} from './services/auth.service';
import {PrintRecipeComponent} from './components/print/print-recipe/print-recipe.component';
import {FavoriteOverviewComponent} from './components/favorites/favorite-overview/favorite-overview.component';
import {ListOverviewComponent} from './components/lists/list-overview/list-overview.component';
import {RecipeOverviewComponent} from './components/recipes/recipe-overview/recipe-overview.component';
import {RecipeCreateEditComponent} from './components/recipes/recipe-create-edit/recipe-create-edit.component';
import {RecipeDetailsComponent} from './components/recipes/recipe-details/recipe-details.component';
import {WineRecommendationComponent} from './components/wine/wine-recommendation/wine-recommendation.component';
import {OcrUploadComponent} from './components/ocr/ocr-upload/ocr-upload.component';
import {SignupComponent} from './components/signup/signup.component';
import {UserDetailsComponent} from './components/user/user-details/user-details.component';
import {AuthorComponent} from './components/author/author.component';
import {AuthorCreateEditComponent} from './components/author/author-create-edit/author-create-edit.component';
import {AuthorDetailsComponent} from './components/author/author-details/author-details.component';
import {IngredientOverviewComponent} from './components/ingredient/ingredient-overview/ingredient-overview.component';
import {GrocerylistComponent} from './components/grocerylist/grocerylist.component';
import {PrintGroceryListComponent} from './components/print/print-grocery-list/print-grocery-list.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'ingredients', canActivate: [() => inject(AuthService).isLoggedIn()], component: IngredientOverviewComponent},
  {path: 'options', canActivate: [() => inject(AuthService).isAdmin()], component: GlobalOptionsComponent},
  {path: 'recipe', canActivate: [() => inject(AuthService).isLoggedIn()], component: RecipeOverviewComponent},
  {
    path: 'wine/create', canActivate: [() => inject(AuthService).isLoggedIn()],
    component: WineCreateEditComponent, data: {create: true}
  },
  {
    path: 'wine/:id/edit', canActivate: [() => inject(AuthService).isLoggedIn()],
    component: WineCreateEditComponent, data: {create: false}
  },
  {
    path: 'recipe/create', canActivate: [() => inject(AuthService).isLoggedIn()],
    component: RecipeCreateEditComponent, data: {create: true, ocr: false}
  },
  {
    path: 'recipe/:id/edit', canActivate: [() => inject(AuthService).isLoggedIn()],
    component: RecipeCreateEditComponent, data: {create: false}
  },
  {path: 'wine', canActivate: [() => inject(AuthService).isLoggedIn()], component: WineOverviewComponent},
  {path: 'wine/:id/details', canActivate: [() => inject(AuthService).isLoggedIn()], component: WineDetailsComponent},
  {
    path: 'wine/recommendations/:category',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: WineRecommendationComponent
  },
  {
    path: 'recipe/:id/details',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: RecipeDetailsComponent
  },
  {
    path: 'user/me/edit',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: SignupComponent,
    data: {signup: false, currentUser: true}
  },
  {
    path: 'user/:id/edit',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: SignupComponent,
    data: {signup: false, currentUser: false}
  },
  {
    path: 'user/me/details',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: UserDetailsComponent,
    data: {currentUser: true}
  },
  {
    path: 'user/:id/details',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: UserDetailsComponent,
    data: {currentUser: false}
  },
  {path: 'users', canActivate: [() => inject(AuthService).isAdmin()], component: UserComponent},
  {path: 'print/:id/:people', canActivate: [() => inject(AuthService).isLoggedIn()], component: PrintRecipeComponent},
  {path: 'ocr/upload', canActivate: [() => inject(AuthService).isLoggedIn()], component: OcrUploadComponent},
  {
    path: 'ocr/:id/edit', canActivate: [() => inject(AuthService).isLoggedIn()],
    component: RecipeCreateEditComponent, data: {create: true, ocr: true}
  },
  {path: 'ingredient', canActivate: [() => inject(AuthService).isLoggedIn()], component: IngredientComponent},
  {path: 'signup', component: SignupComponent, data: {signup: true}},
  {path: 'favorites', canActivate: [() => inject(AuthService).isLoggedIn()], component: FavoriteOverviewComponent},
  {path: 'list', canActivate: [() => inject(AuthService).isLoggedIn()], component: ListOverviewComponent},
  {path: 'author', canActivate: [() => inject(AuthService).isLoggedIn()], component: AuthorComponent},
  {
    path: 'author/create',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: AuthorCreateEditComponent,
    data: {edit: false}
  },
  {
    path: 'author/:id/edit',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: AuthorCreateEditComponent,
    data: {edit: true}
  },
  {
    path: 'author/:id/details',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: AuthorDetailsComponent
  },
  {path: 'grocerylist', canActivate: [() => inject(AuthService).isLoggedIn()], component: GrocerylistComponent},
  {
    path: 'grocerylist/print',
    canActivate: [() => inject(AuthService).isLoggedIn()],
    component: PrintGroceryListComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
