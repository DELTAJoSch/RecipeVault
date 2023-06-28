import { DecimalPipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { Amount, AmountUnit } from 'src/app/dtos/amount';
import { UnitPipe } from 'src/app/pipes/unit.pipe';
import { ShareGroceryListService } from 'src/app/services/share-grocery-list.service';

@Component({
  selector: 'app-print-grocery-list',
  templateUrl: './print-grocery-list.component.html',
  styleUrls: ['./print-grocery-list.component.scss']
})
export class PrintGroceryListComponent implements OnInit{
  grocerylist: Amount[];
  display: boolean[] = [];
  groceryListCondensed: string[] = [];

  constructor(
    private service: ShareGroceryListService,
    private unitPipe: UnitPipe,
    private decimalPipe: DecimalPipe,
  ) {}

  /**
   * loads the items on the grocery list on init, and initializes the visibility array for the items
   */
  ngOnInit() {
    this.service.currentGroceryList.subscribe(
      data => this.grocerylist = data
    );
    let j = 0;
    for (let i = 0; i < this.grocerylist.length; i++) {
      if (i > 0) {
        if (this.grocerylist[i].ingredient.name === this.grocerylist[i-1].ingredient.name) {
          this.groceryListCondensed[j] = this.groceryListCondensed[j] + '\n' + this.amountToString(this.grocerylist[i]);
        } else {
          this.groceryListCondensed[++j] = this.amountToString(this.grocerylist[i]);
        }
      } else {
        this.groceryListCondensed[j] = this.amountToString(this.grocerylist[i]);
      }
    }
    for (let i = 0; i < this.groceryListCondensed.length; i++) {
      this.display[i] = true;
    }
  }

  /**
   * prints the grocery list
   */
  print() {
    const listContent = document.getElementById('listContent');
    listContent.classList.remove('justify-content-center');
    const content = document.getElementById('printContent');
    const windowRef = window.open('', '', 'left=0,top=0,width=900,height=900,toolbar=0,scrollbars=0,status=0');

    windowRef.document.write('<style> li {white-space: pre-wrap} ul { list-style: circle}</style>' + content.innerHTML);
    windowRef.document.close();
    windowRef.focus();
    windowRef.print();
    windowRef.close();
  }

  /**
   * returns the amount of an ingredient as a string.
   *
   * @param ingredient
   */
  getAmount(ingredient: Amount): string{
    return this.decimalPipe.transform(ingredient.amount.toString(), '1.0-5', 'de');
  }

  /**
   * returns the unit of an ingredient as a string
   *
   * @param ingredient
   */
  getUnit(ingredient: Amount): string {
    return this.unitPipe.transform(ingredient.unit);
  }

  /**
   * returns the name of the ingredient as a string
   *
   * @param ingredient
   */
  getIngredient(ingredient: Amount): string{
    return ingredient.ingredient.name;
  }

  /**
   * returns a readable representation of an amount
   *
   * @param a amount
   */
  amountToString(a: Amount): string {
    return this.getAmount(a) + ' ' + this.getUnit(a) + ' ' + this.getIngredient(a);
}

  /**
   * toggles if the item on the list will be printed
   *
   * @param i index in visibility array
   */
  toggleItem(i) {
    this.display[i] = !this.display[i];
  }
}
