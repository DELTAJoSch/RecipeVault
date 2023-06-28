import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header-entry',
  templateUrl: './header-entry.component.html',
  styleUrls: ['./header-entry.component.scss'],
})
export class HeaderEntryComponent{
  @Output() clicked: EventEmitter<void> = new EventEmitter<void>();
  @Input() routerLink = '';
  @Input() text = '';
  @Input() icon = '';
  @Input() alwaysVisible = false;

  constructor(
    public authService: AuthService,
    public router: Router
  ) { }

  /**
   * If the link is pressed, start interaction
   */
  interact() {
    this.clicked.emit();

    if(this.routerLink && this.routerLink !== ''){
      this.router.navigateByUrl(this.routerLink);
    }
  }
}
