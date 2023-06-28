import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-spinner-modal',
  templateUrl: './spinner-modal.component.html',
  styleUrls: ['./spinner-modal.component.scss']
})
export class SpinnerModalComponent implements OnChanges{
  @Input() message = '';
  @Input() show = true;
  @Input() progress = 0;

  progPercentage = 0;

  /**
   * React to changes of the input - in this case, the progress
   *
   * @param changes The changes
   */
  ngOnChanges(changes: SimpleChanges){
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case 'progress': {
            if(this.progress >= 0 && this.progress <= 1.0){
              this.progPercentage = this.progress*100;
            }
            break;
          }
        }
      }
    }
  }
}
