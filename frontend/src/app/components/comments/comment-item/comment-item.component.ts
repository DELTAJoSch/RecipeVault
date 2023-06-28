import {Component, EventEmitter, Input, OnInit, Output, TemplateRef} from '@angular/core';
import {Router} from '@angular/router';
import {CommentService} from '../../../services/comment.service';
import {Comment} from '../../../dtos/comment';
import {UserService} from '../../../services/user.service';
import { ErrorService } from 'src/app/services/error.service';
import {AuthService} from '../../../services/auth.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';


@Component({
  selector: 'app-comment-item',
  templateUrl: './comment-item.component.html',
  styleUrls: ['./comment-item.component.scss']
})
export class CommentItemComponent implements OnInit {
  @Input() com: Comment = {
    creatorId: 0,
    recipeId: 0,
    dateTime: new Date(),
    content: '',
  };
  @Output() deleted: EventEmitter<Comment> = new EventEmitter<Comment>();
  formattedDate: string;
  creatorEmail: string;

  constructor(
    private userService: UserService,
    private errorService: ErrorService,
    private router: Router,
    private commentService: CommentService,
    private authService: AuthService,
    private modalService: NgbModal
  ){}

  /**
   * Loads creator of this comment on init.
   */
  ngOnInit() {
    this.getCreator();
  }

  /**
   * Gets called if this comment should be deleted.
   */
  onDelete() {
    this.commentService.deleteComment(this.com).subscribe();
    this.deleted.emit(this.com);
  }

  /**
   * Gets the creator of this comment.
   */
  getCreator() {
    this.userService.getUserById(this.com.creatorId).subscribe({
      next: user => {
        this.creatorEmail = user.email;
      },
      error: error => {
         this.errorService.handleError(error, `Verfasser konnte nicht geladen werden`);
      }
    });
  }

  /**
   * Returns if the current user is an admin.
   */
  isAdmin(): boolean {
    return this.authService.isAdmin();
  }

  /**
   * Opens the modal for deletion-confirmation.
   *
   * @param messageAddModal the modal to open
   */
  openAddModal(messageAddModal: TemplateRef<any>) {
    this.modalService.open(messageAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }
}
