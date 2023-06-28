import { Component, Input, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { countries } from 'src/app/data/country-codes';
import { Wine, WineCategory } from 'src/app/dtos/wine';
import { ErrorService } from 'src/app/services/error.service';
import { WineService } from 'src/app/services/wine.service';

@Component({
  selector: 'app-wine-create-edit',
  templateUrl: './wine-create-edit.component.html',
  styleUrls: ['./wine-create-edit.component.scss']
})
export class WineCreateEditComponent implements OnInit {
  @Input() create = true;

  countryList = countries;
  values;
  categories;

  id: number = null;

  wine: Wine = {
    id: null,
    name: null,
    description: null,
    grape: null,
    link: null,
    temperature: 0,
    vinyard: null,
    owner: null,
    country: null,
    category: WineCategory.sparkling
  };

  constructor(
    private wineService: WineService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private errorService: ErrorService
  ){
    this.values = Object.values;
    this.categories = WineCategory;
  }

  /**
   * Initialize component based on selected mode
   */
  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.create = data.create;
    });

    // if the mode is edit, load wine from backend
    if(this.create === false){
      this.id = Number(this.route.snapshot.paramMap.get('id'));

      this.wineService.getWineById(this.id).subscribe({
        next: wine => {
          this.wine = wine;
        },
        error: err => {
          this.errorService.handleError(err,`Wein konnte nicht geladen werden`, '/wine');
        }
      });
    }
  }

  /**
   * Submit the form with the filled in wine details
   *
   * @param form The form to submit
   */
  onSubmit(form: NgForm) {
    if(form.valid){
      if(this.wine.description === ''){
        delete this.wine.description;
      }

      if(this.wine.link === ''){
        delete this.wine.link;
      }

      if(this.wine.grape === ''){
        delete this.wine.grape;
      }

      if(this.create){
        this.wineService.createWine(this.wine).subscribe({
          next: _ => {
            this.notification.success('Wein hinzugefügt!');
            this.router.navigateByUrl('/wine');
          },
          error: err => {
            this.errorService.handleError(err,`Wein konnte nicht erstellt werden`);
          }
        });
      }else{
        this.wineService.updateWine(this.wine, this.id).subscribe({
          next: _ => {
            this.notification.success('Änderungen gespeichert!');
            this.router.navigateByUrl('/wine');
          },
          error: err => {
            this.errorService.handleError(err,`Wein konnte nicht geändert werden`);
          }
        });
      }
    } else{
      this.notification.warning('Es fehlen Daten zur erstellung!');
    }
  }
}
