import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Comment} from '../dtos/comment';

@Injectable({
  providedIn: 'root'
})

export class CommentService {

  private commentBaseUri: string = this.globals.backendUri + '/comments';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) {}

  /**
   * Loads comments for specific recipe from the backend.
   *
   * @param recipeId the id of the recipe to load comments of
   */
  getComments(recipeId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(this.commentBaseUri + '/' + recipeId);
  }

  /**
   * Adds a comment to a recipe.
   *
   * @param comment the comment to add to recipe
   */
  createComment(comment: Comment): Observable<Comment> {
    return this.httpClient.post<Comment>(this.commentBaseUri, comment);
  }

  /**
   * Remove a comment from a recipe.
   *
   * @param comment the comment to delete from recipe
   */
  deleteComment(comment: Comment): Observable<object>{
    return this.httpClient.delete(this.commentBaseUri, {body: comment});
  }
}
