import {BrowserModule} from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {LOCALE_ID, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { ToastrModule } from 'ngx-toastr';
import {httpInterceptorProviders} from './interceptors';
import { WineCreateEditComponent } from './components/wine/wine-create-edit/wine-create-edit.component';
import { WineComponent } from './components/wine/wine/wine.component';
import { WineItemComponent } from './components/wine/wine/wine-item/wine-item.component';
import { UserComponent } from './components/user/user.component';
import { WineDetailsComponent } from './components/wine/wine-details/wine-details.component';
import { WineOverviewComponent } from './components/wine/wine-overview/wine-overview.component';
import { IngredientComponent } from './components/ingredient/ingredient.component';
import { GlobalOptionsComponent } from './components/options/global-options/global-options.component';
import { OptionsEntryComponent } from './components/options/global-options/options-entry/options-entry.component';
import { PrintRecipeComponent } from './components/print/print-recipe/print-recipe.component';
import { DifficultyPipe } from './pipes/difficulty.pipe';
import { UnitPipe } from './pipes/unit.pipe';
import { WineCategoryPipe } from './pipes/wine-category.pipe';
import {RecipeComponent} from './components/recipes/recipe/recipe.component';
import {RecipeItemComponent} from './components/recipes/recipe/recipe-item/recipe-item.component';
import {RecipeOverviewComponent} from './components/recipes/recipe-overview/recipe-overview.component';
import {RecipeCreateEditComponent} from './components/recipes/recipe-create-edit/recipe-create-edit.component';
import {RecipeDetailsComponent} from './components/recipes/recipe-details/recipe-details.component';
import { FavoriteComponent } from './components/favorites/favorite/favorite.component';
import { FavoriteOverviewComponent } from './components/favorites/favorite-overview/favorite-overview.component';
import { WineRecommendationComponent } from './components/wine/wine-recommendation/wine-recommendation.component';
import { WineCategoryInfoPipePipe } from './pipes/wine-category-info-pipe.pipe';
import { StringToHtmlPipe } from './pipes/string-to-html.pipe';
import { CommentItemComponent } from './components/comments/comment-item/comment-item.component';
import { CommentOverviewComponent } from './components/comments/comment-overview/comment-overview.component';
import { ListOverviewComponent } from './components/lists/list-overview/list-overview.component';
import { ListComponent } from './components/lists/list/list.component';
import { OcrUploadComponent } from './components/ocr/ocr-upload/ocr-upload.component';
import {AutocompleteComponent} from './components/autocomplete/autocomplete.component';
import {
  RecipeIngredientsItemComponent
} from './components/recipes/recipe-create-edit/recipe-ingredients-item/recipe-ingredients-item.component';
import { SignupComponent } from './components/signup/signup.component';
import { UserDetailsComponent } from './components/user/user-details/user-details.component';
import {RecipeWineCategoryLinkingPipe} from './pipes/recipe-wine-category-linking.pipe';
import {RecipeWineCategoryPipe} from './pipes/recipe-wine-category.pipe';
import { AuthorComponent } from './components/author/author.component';
import { AuthorCreateEditComponent } from './components/author/author-create-edit/author-create-edit.component';
import { AuthorDetailsComponent } from './components/author/author-details/author-details.component';
import { HeaderEntryComponent } from './components/header/header-entry/header-entry.component';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { IngredientMatchingCategoryPipe } from './pipes/ingredient-matching-category.pipe';
import { SpinnerModalComponent } from './components/spinner-modal/spinner-modal.component';
import { NotesComponent } from './components/notes/notes.component';
import {DecimalPipe, registerLocaleData} from '@angular/common';
import localeDe from '@angular/common/locales/de';
import localDeExtra from '@angular/common/locales/extra/de';
import { WineCategoryEnumPipe } from './pipes/wine-category-enum.pipe';
import { GrocerylistComponent } from './components/grocerylist/grocerylist.component';
import { PrintGroceryListComponent } from './components/print/print-grocery-list/print-grocery-list.component';
import { ShareGroceryListService } from './services/share-grocery-list.service';
import { HeaderDropdownComponent } from './components/header/header-dropdown/header-dropdown.component';
import { IngredientCategoryExamplePipe } from './pipes/ingredient-category-example.pipe';
import { IngredientOverviewComponent } from './components/ingredient/ingredient-overview/ingredient-overview.component';
import { IngredientListComponent } from './components/ingredient/ingredient-list/ingredient-list.component';
import { IngredientItemComponent } from './components/ingredient/ingredient-item/ingredient-item.component';
import { UserItemComponent } from './components/user/user-item/user-item.component';
import { UserListComponent } from './components/user/user-list/user-list.component';
import { AuthorItemComponent } from './components/author/author-item/author-item.component';
import { AuthorListComponent } from './components/author/author-list/author-list.component';

registerLocaleData(localeDe, 'de', localDeExtra);

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    LoginComponent,
    WineCreateEditComponent,
    WineComponent,
    WineItemComponent,
    UserComponent,
    WineDetailsComponent,
    WineOverviewComponent,
    IngredientComponent,
    GlobalOptionsComponent,
    OptionsEntryComponent,
    PrintRecipeComponent,
    DifficultyPipe,
    UnitPipe,
    WineCategoryPipe,
    RecipeComponent,
    RecipeItemComponent,
    RecipeOverviewComponent,
    RecipeCreateEditComponent,
    RecipeDetailsComponent,
    FavoriteComponent,
    FavoriteOverviewComponent,
    WineRecommendationComponent,
    WineCategoryInfoPipePipe,
    StringToHtmlPipe,
    CommentItemComponent,
    CommentOverviewComponent,
    ListOverviewComponent,
    ListComponent,
    OcrUploadComponent,
    AutocompleteComponent,
    RecipeIngredientsItemComponent,
    SignupComponent,
    UserDetailsComponent,
    RecipeWineCategoryLinkingPipe,
    RecipeWineCategoryPipe,
    AuthorComponent,
    AuthorCreateEditComponent,
    AuthorDetailsComponent,
    HeaderEntryComponent,
    HeaderComponent,
    IngredientMatchingCategoryPipe,
    SpinnerModalComponent,
    NotesComponent,
    GrocerylistComponent,
    WineCategoryEnumPipe,
    IngredientCategoryExamplePipe,
    PrintGroceryListComponent,
    HeaderDropdownComponent,
    IngredientOverviewComponent,
    IngredientListComponent,
    IngredientItemComponent,
    HeaderDropdownComponent,
    UserItemComponent,
    UserListComponent,
    AuthorItemComponent,
    AuthorListComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDialogModule,
    ToastrModule.forRoot({
      positionClass :'toast-top-right'
    })
  ],
  providers: [httpInterceptorProviders, MatDialog, {provide: LOCALE_ID, useValue: 'de'},
    WineCategoryEnumPipe, ShareGroceryListService, UnitPipe, DecimalPipe],
  bootstrap: [AppComponent],
  entryComponents: [IngredientComponent, NotesComponent],
})
export class AppModule {
}
