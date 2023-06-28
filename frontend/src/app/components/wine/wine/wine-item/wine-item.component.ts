import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { OwnerInfo } from 'src/app/dtos/owner-info';
import { Wine, WineCategory } from 'src/app/dtos/wine';
import { AuthService } from 'src/app/services/auth.service';
import { ErrorService } from 'src/app/services/error.service';
import { WineService } from 'src/app/services/wine.service';

@Component({
  selector: 'app-wine-item',
  templateUrl: './wine-item.component.html',
  styleUrls: ['./wine-item.component.scss']
})
export class WineItemComponent implements OnInit{
  @Input() wine: Wine = {
    id: 0,
    name: '',
    description: '',
    grape: '',
    link: '',
    temperature: 0,
    vinyard: '',
    owner: new OwnerInfo(),
    country: '',
    category: WineCategory.sparkling
  };

  @Output() delete: EventEmitter<void> = new EventEmitter<void>();

  mobile = false;

  constructor(
    private router: Router,
    private wineService: WineService,
    private notification: ToastrService,
    private errorService: ErrorService,
    private authService: AuthService,
  ){}

  /**
   * React to resizing events
   */
  @HostListener('window:resize')
  onResize() {
    this.mobile = window.innerWidth <= 900;
  }

  /**
   * Initialize this component
   */
  ngOnInit() {
    this.mobile = window.innerWidth <= 900;
  }

  /**
   * Delete the wine
   */
  onDeleteClicked() {
    this.wineService.deleteWine(this.wine.id).subscribe({
      next: _=> {
        this.notification.success(`Wein ${this.wine.name} gelöscht!`);
        this.delete.emit();
      },
      error: err => {
        this.errorService.handleError(err,`Wein "${this.wine.name}" konnte nicht gelöscht werden`);
      }
    });
  }

  /**
   * returns if the user is permitted to edit (and delete) the recipe.
   */
  isPermittedToEdit(): boolean {
    if (this.wine.owner === null) {
      return this.authService.isAdmin();
    }
    return (this.wine.owner.email === this.authService.getUserEmail()) || this.authService.isAdmin();
  }
}
