import { animate, state, style, transition, trigger } from '@angular/animations';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NavigationStart, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-header-dropdown',
  templateUrl: './header-dropdown.component.html',
  styleUrls: ['./header-dropdown.component.scss'],
  animations: [
    trigger('visibleHidden', [
      state(
        'visible',
        style({
          opacity: 1,
          height: '*'
        })
      ),
      state(
        'hidden, void',
        style({
          opacity: 0,
          height: 0
        })
      ),
      transition('* => visible', animate('300ms ease-in-out')),
      transition('* => hidden, * => void', animate('300ms ease-in-out'))
    ])
  ]
})
export class HeaderDropdownComponent implements OnInit {
  @Input() tag: string = null;
  @Input() name = '';
  @Input() showExpandState = true;
  @Input() toggle: Observable<string>;
  @Output() toggled = new EventEmitter<string>();

  expanded = false;

  constructor(private router: Router){
    // close when naviagtion happens
    this.router.events.forEach((event) => {
      if(event instanceof NavigationStart) {
        if(this.expanded === true) {
          this.expanded = false;
        }
      }
    });
  }

  /**
   * Initializes this component
   */
  ngOnInit() {
    // required inputs are supported from Angular 16 upwards. This is a simple way for Angular 15.
    if(this.tag === null) {
      console.error('TAG-property is required. Expected a value.');
    }

    this.toggle.subscribe(s => {
      if(this.expanded === true && this.tag !== s){
        this.expanded = false;
      }
    });
  }

  /**
   * Toggles the expansion of this element. Sends an event to signal the state has changed
   */
  toggleExpand() {
    this.expanded = !this.expanded;
    if(this.expanded === true) {
      this.toggled.emit(this.tag);
    }
  }
}
