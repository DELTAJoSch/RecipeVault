import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Note} from '../dtos/note';

@Injectable({
  providedIn: 'root'
})
export class NotesService {

  private notesBaseUri: string = this.globals.backendUri + '/notes';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) {}

  /**
   * Loads notes for a specific recipe from the backend.
   *
   * @param recipeId: id of the recipe to load notes of
   */
  getNotes(recipeId: number): Observable<Note> {
    return this.httpClient.get<Note>(this.notesBaseUri + '/' + recipeId);
  }

  /**
   * Adds a note to a recipe or edits existing one.
   *
   * @param note the note to add or edit
   */
  createUpdateNote(note: Note): Observable<Note> {
    return this.httpClient.post<Note>(this.notesBaseUri, note);
  }

  /**
   * Remove a note from a recipe.
   *
   * @param recipeId the recipe to remove the note from
   */
  deleteNote(recipeId: number): Observable<object>{
    return this.httpClient.delete(this.notesBaseUri + '/' + recipeId);
  }
}
