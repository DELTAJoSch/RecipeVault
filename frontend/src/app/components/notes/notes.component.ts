import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {NgForm} from '@angular/forms';
import {Note} from '../../dtos/note';
import {NotesService} from '../../services/notes.service';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-notes',
  templateUrl: './notes.component.html',
  styleUrls: ['./notes.component.scss']
})
export class NotesComponent implements OnInit{

  @Output() event: EventEmitter<Note> = new EventEmitter<Note>();
  note: Note = {
    creatorId: null,
    recipeId: 0,
    content: '',
};

  constructor(
    private noteService: NotesService,
    private notification: ToastrService,
    private errorService: ErrorService,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ){}

  /**
   * Initialize component.
   */
  ngOnInit(): void {
    this.note.recipeId = this.data.recipeId;
    this.note.content = this.data.content;
  }

  /**
   * Submits form and calls service to create or edit the note.
   *
   * @param form the form to submit
   */
  onSubmit(form: NgForm) {
    if(form.valid){
      this.noteService.createUpdateNote(this.note).subscribe({
        next: _ => {
          this.note.content = this.data.content;
          this.notification.success('Notiz hinzugefügt!');
          this.event.emit(this.note);
        },
        error: error => {
          this.errorService.handleError(error, `Notiz konnte nicht hinzugefügt werden`);
        }
      });
    } else{
      this.notification.warning('Es fehlen Daten zur erstellung!');
    }
  }

  /**
   * Checks whether the note contains text.
   */
  contentIsPresent(): boolean{
    return this.note.content !== '';
  }
}
