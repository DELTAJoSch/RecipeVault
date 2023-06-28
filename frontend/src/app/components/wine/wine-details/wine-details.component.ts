import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { countries } from 'src/app/data/country-codes';
import { Wine, WineCategory } from 'src/app/dtos/wine';
import { WineSearch } from 'src/app/dtos/wine-search';
import { ErrorService } from 'src/app/services/error.service';
import { WineService } from 'src/app/services/wine.service';

@Component({
  selector: 'app-wine-details',
  templateUrl: './wine-details.component.html',
  styleUrls: ['./wine-details.component.scss']
})
export class WineDetailsComponent implements OnInit {
  id: number = null;
  countryList = countries;

  countryDetails = this.countryList[0]; //initial value is needed, otherwise errors are thrown
  randomImageSelector = 0;

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

  recommendations: Wine[] = [];

  mobile = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private wineService: WineService,
    private notification: ToastrService,
    private errorService: ErrorService,
  ){}

  /**
   * React to resize events
   */
  @HostListener('window:resize')
  onResize() {
    this.mobile = window.innerWidth <= 1000;
  }

  /**
   * Initialize component based on selected mode
   */
  ngOnInit(): void {
    this.mobile = window.innerWidth <= 1000;
    this.randomImageSelector = Math.floor(Math.random() * 8);

    this.route.params.subscribe(data => {
      this.id = data['id'];

      if(this.id !== undefined){
        this.wineService.getWineById(this.id).subscribe({
          next: wine => {
            this.wine = wine;
            this.countryDetails = this.countryList.find((x) => x.code === this.wine?.country);
            this.randomImageSelector = Math.floor(Math.random() * 8);

            this.loadRecommendations();
          },
          error: err => {
            this.errorService.handleError(err,`Wein konnte nicht geladen werden`, '/wine');
          }
        });
      }
    });
  }

  /**
   * Checks whether a description is present
   *
   * @returns True, if a description is available
   */
  descriptionAvailable(): boolean{
    return (this.wine.description !== null) && (this.wine.description.length !== 0);
  }

  /**
   * Load the recommended wines of the same category
   */
  loadRecommendations() {
    const searchParameters = new WineSearch();

    searchParameters.category = this.wine.category;
    searchParameters.country = null;
    searchParameters.name = null;
    searchParameters.vinyard = null;

    this.wineService.getSearchResults(0, 25, searchParameters).subscribe({
      next: resp => {
        let result = resp.body;

        // filter out this wine from result (if present)
        result = result.filter(w => w.id !== this.wine.id);

        if(result.length > 3){
          const lower = Math.floor(Math.random() * (result.length - 3));
          this.recommendations = result.slice(lower, lower+3);
        }else{
          this.recommendations = result;
        }
      },
      error: error => {
        this.errorService.handleError(error,`Weinempfehlung konnte nicht geladen werden`);
      }
    });
  }
}
