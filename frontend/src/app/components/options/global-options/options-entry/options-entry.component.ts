import { Component, Input } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ApplicationOption, ApplicationOptionType } from 'src/app/dtos/option';
import { ErrorService } from 'src/app/services/error.service';
import { OptionsService } from 'src/app/services/options.service';

// This is a bit ugly, but ESLint is being stupid when enforcing this.
// It doesn't want to use components, but instead a directive, which does not have template support and is therefore useless here.
// A bit hacky, but still better than the alternative!
/* eslint-disable */
@Component({
  selector: '[app-options-entry]',
  templateUrl: './options-entry.component.html',
  styleUrls: ['./options-entry.component.scss']
})
/* eslint-enable */
export class OptionsEntryComponent {
  @Input() option: ApplicationOption = {
    id: 0,
    name: 'TEST',
    defaultValue: 'FALSE',
    value: 'TRUE',
    type: ApplicationOptionType.boolean
  };
  edit = false;
  types = ApplicationOptionType;
  editValue: any = null;

  constructor(
    private optionsService: OptionsService,
    private notification: ToastrService,
    private errorService: ErrorService,
    ){}

  /**
   * Change the mode to edit
   */
  changeEdit(){
    this.editValue = this.option.value ?? this.option.defaultValue;
    this.edit = true;
  }

  /**
   * Accepts the changes and sends them to the backend
   */
  accept(){
    const old = this.option.value;

    switch(this.option.type){
      case ApplicationOptionType.boolean: {
        this.option.value = this.editValue;
        break;
      }
      case ApplicationOptionType.longRange: {
        this.option.value = this.editValue.toString();
        break;
      }
      case ApplicationOptionType.shortRange: {
        this.option.value = this.editValue.toString();
        break;
      }
      case ApplicationOptionType.string: {
        this.option.value = this.editValue;
        break;
      }
      default: {}
    }

    this.optionsService.update(this.option, this.option.id).subscribe({
      next: opt => {
        this.option = opt;
      },
      error: err => {
        this.errorService.handleError(err,`Einstellung ${this.option.name} konnte nicht geändert werden`);
        this.option.value = old;
      }
    });
    this.edit = false;
  }

  /**
   * Revokes the changes and restores the previous value
   */
  revoke(){
    this.editValue = '';
    this.edit = false;
  }
}
