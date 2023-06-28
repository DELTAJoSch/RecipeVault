import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {Comment} from '../../../dtos/comment';
import {CommentService} from '../../../services/comment.service';
import {FormGroup, NgForm} from '@angular/forms';
import {UserService} from '../../../services/user.service';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-comment-overview',
  templateUrl: './comment-overview.component.html',
  styleUrls: ['./comment-overview.component.scss']
})
export class CommentOverviewComponent implements OnInit{
  recipeId: number;

  comments: Comment[];
  validationForm: FormGroup;
  created: Comment = {
    creatorId: null,
    recipeId: 0,
    dateTime: new Date(),
    content: null
  };

  constructor(
    private router: Router,
    private commentService: CommentService,
    private userService: UserService,
    private notification: ToastrService,
    private route: ActivatedRoute,
    private errorService: ErrorService,
  ) {}

  /**
   * Loads comments of the recipe on init.
   */
  ngOnInit() {
    this.validationForm = new FormGroup({});
    this.route.params.subscribe((data) => {
      this.recipeId = data.id;
      this.loadComments();
    });
  }

  /**
   * Loads all comments of this recipe.
   */
  loadComments(){

    this.commentService.getComments(this.recipeId).subscribe({
        next: data => {
          this.comments = data;
        },
        error: error => {
          this.comments = [];
          this.errorService.handleError(error, `Kommentare konnten nicht geladen werden`);
        }
      });
  }

  /**
   * Is called when a comment should be posted to this recipe.
   *
   * @param form the form to submit
   */
  onSubmit(form: NgForm) {
    if(form.valid){
      this.created.recipeId = this.recipeId;
      this.created.dateTime = new Date();
      this.created.dateTime.setHours(this.created.dateTime.getHours() + 2);
      this.commentService.createComment(this.created).subscribe({
        next: _ => {
          this.notification.success('Kommentar hinzugefügt!');
          this.loadComments();
          this.router.navigateByUrl('/recipe/' + this.recipeId + '/details');
        },
        error: error => {
          this.errorService.handleError(error, `Kommentar konnte nicht erstellt werden`);
        }
      });
      form.reset();
    } else{
      this.notification.warning('Ein Kommentar kann nicht leer sein!');
    }
  }

  /**
   * Deletes specific comment from current comments of this recipe.
   *
   * @param com the comment to delete
   */
  delete(com: Comment) {
    this.comments = this.comments.filter(element => element !== com);
  }
}
