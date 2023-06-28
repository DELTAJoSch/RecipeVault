import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Amount, AmountUnit } from '../../../../dtos/amount';
import { Subject, debounceTime, of, switchMap, tap } from 'rxjs';
import { Ingredient, IngredientMatchingCategory } from '../../../../dtos/ingredient';
import { IngredientService } from '../../../../services/ingredient.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { IngredientComponent } from 'src/app/components/ingredient/ingredient.component';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-recipe-ingredients-item',
  templateUrl: './recipe-ingredients-item.component.html',
  styleUrls: ['./recipe-ingredients-item.component.scss']
})
export class RecipeIngredientsItemComponent {
  static counter = 0;

  // The @Input decorator is used to receive data from the parent component.
  @Input() amount: Amount = {
    ingredient: null,
    amount: 0,
    unit: AmountUnit.kg,
  };

  // The @Output decorator is used to emit events to the parent component.
  @Output() emptyInput = new EventEmitter<any>();
  @Output() removeIngredient = new EventEmitter<any>();

  unit = AmountUnit;
  values: any;
  amountForm: FormGroup;

  newIng: Ingredient;

  // Variables for autocomplete
  datalistClass: { [klass: string]: any } = [];
  dataListId: string;
  ingredientInputText = '';
  valueCandidates = new Map<string, Ingredient>();
  touched = false;
  disabled = false;
  matchesSuggestion = true;
  ingredientInputChange = new Subject<string>();

  constructor(
    private ingredientService: IngredientService,
    private dialog: MatDialog,
    private errorService: ErrorService,
  ) {
    this.values = Object.values;
    this.unit = AmountUnit;

    // Auto increment for autocomplete
    const autocompleteId = RecipeIngredientsItemComponent.counter++;
    this.dataListId = `app-autocomplete-ingredients-${autocompleteId}`;
  }

  onChange = (quantity: any) => { };
  onTouched = () => { };

  // The ngOnInit method is called after the component is initialized.
  // It is used to perform any necessary initialization tasks.
  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnInit(): void {
    this.amountForm = new FormGroup({
      name: new FormControl(this.amount.amount, [
        Validators.required,
      ]),
    });

    this.ingredientInputChange
      .pipe(
        tap(this.checkIfInputMatchesCandidate.bind(this)),
        debounceTime(300),
        switchMap(this.ingredientSuggestions),
      )
      .subscribe({
        next: this.onReceiveNewCandidates.bind(this),
        error: err => {
          this.errorService.handleError(err, `Automatische Vervollständigung für Zutaten konnte nicht geladen werden`);
        },
      });

    if (this.amount.ingredient != null) {
      this.ingredientInputChange.next(this.amount.ingredient.name);
    }
  }

  // This method fetches ingredient suggestions based on the user input.
  ingredientSuggestions = (input: string) => (input === '')
    ? of([])
    : this.ingredientService.searchByName(input);

  public formatIngredientName(ingredient: Ingredient | null | undefined): string {
    return (ingredient == null)
      ? ''
      : `${ingredient.name}`;
  }

  public createIngredientByName(ingredientName: string | null | undefined): Ingredient | null {
    if (ingredientName === null || ingredientName === undefined) {
      return null;
    } else {
      const ingredient = new Ingredient();
      ingredient.name = ingredientName;
      return ingredient;
    }
  }

  // Emits the removeIngredient event when called.
  emitRemoveIngredientEvent() {
    this.removeIngredient.emit();
  }

  // Opens the ingredient creator dialog.
  openIngredientCreator() {
    this.newIng = {
      name: this.ingredientInputText,
      category: IngredientMatchingCategory.undefined,
      id: null
    } as Ingredient;
    const dialogRef = this.dialog.open(IngredientComponent, {
      height: '30&',
      width: '60%',
      data: this.newIng,
    });
    dialogRef.afterClosed().subscribe(result => {
      this.ingredientInputChange.next(this.amount.ingredient.name);
    });
  }

  public registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  public registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  // Checks if the input matches any of the candidate ingredients.
  private checkIfInputMatchesCandidate(input: string): void {
    this.markAsTouched();
    if (input === '') {
      this.emptyInput.emit();
      this.setValue(null);
    } else {
      /* Type cast hack. The option `valueNeedsToMatchSuggestion` only makes sense
       * if  the model type parameter `T` actually is meant to be `string` anyway.
       * If it is not, expect hell to break loose here.
      */
      this.setValue(this.createIngredientByName(input));
      const selectedValue = this.valueCandidates.get(input);
      this.matchesSuggestion = selectedValue !== undefined;
      this.emptyInput.emit();
    }
  }

  // Sets the value of the ingredient.
  private setValue(newValue: Ingredient | null) {
    this.amount.ingredient = newValue;
    this.ingredientInputText = this.formatIngredientName(this.amount.ingredient);
    this.onChange(this.amount.ingredient);
  }

  // Updates the valueCandidates with the new list of ingredient suggestions.
  private onReceiveNewCandidates(result: Ingredient[]) {
    this.valueCandidates.clear();
    for (const candidate of result) {
      this.valueCandidates.set(this.formatIngredientName(candidate), candidate);
    }
    this.checkIfInputMatchesCandidate(this.ingredientInputText);
  }

  // Marks the input as touched.
  private markAsTouched(): void {
    if (!this.touched) {
      this.touched = true;
      this.onTouched();
    }
  }
}
