import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {User} from '../../../dtos/user';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../../../services/auth.service';
import {UserService} from '../../../services/user.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { ErrorService } from 'src/app/services/error.service';
import {Location} from '@angular/common';


@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.scss']
})
export class UserDetailsComponent implements OnInit {
  @Input() currentUser = true;
  userId: number = null;
  user: User = {
    id: null,
    email: null,
    password: null,
    admin: null
  };
  calledByAdmin = this.authService.getUserRole() === 'ADMIN';
  header = 'details';

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private notification: ToastrService,
    private authService: AuthService,
    private modalService: NgbModal,
    private errorService: ErrorService,
    private location: Location,
  ) {
  }

  /**
   * Initialize component based on given data
   */
  ngOnInit() {
    this.route.data.subscribe( data => {
      this.currentUser = data.currentUser;
    });
    // check if called for /me or not
    if (this.currentUser === false) {
      this.userId = Number(this.route.snapshot.paramMap.get('id'));

      this.userService.getUserById(this.userId).subscribe({
        next: user => {
          this.user = user;
        },
        error: err => {
          this.errorService.handleError(err, `Benutzer konnten nicht geladen werden`);
          this.location.back();
        }
      });
    } else {
      this.userService.getCurrentUser().subscribe({
        next: user => {
          this.user = user;
          this.userId = user.id;
        },
        error: err => {
          this.errorService.handleError(err, 'Benutzer konnte nicht geladen werden');
          this.location.back();
        }
      });
    }
  }

  /**
   * Delete user currently shown in component.
   */
  delete() {
    if(this.currentUser) {
      this.userService.deleteCurrentUser().subscribe({
        next: _ => {
          this.notification.success('User wurde gelöscht');
          this.authService.logoutUser();
          this.router.navigateByUrl('/');
          this.modalService.dismissAll();
        },
        error: err => {
          this.errorService.handleError(err, 'Benutzer konnten nicht gelöscht werden');
          this.modalService.dismissAll();
        }
      });
    } else {
      this.userService.deleteUser(this.userId).subscribe({
        next: _ => {
          this.notification.success(`User gelöscht!`);
          this.authService.logoutUser();
          this.router.navigateByUrl('/');
          this.modalService.dismissAll();
        },
        error: error => {
          this.errorService.handleError(error, `Benutzer konnten nicht gelöscht werden`);
          this.modalService.dismissAll();
        }
      });
    }
  }

  /**
   * Navigate to edit page of user.
   *
   * @param id Id of the user to be edited.
   */
  onEditClick(id: number) {
    if(this.currentUser) {
      this.router.navigateByUrl('user/me/edit');
    } else {
      this.router.navigateByUrl('user/' + id + '/edit');
    }
  }

  /**
   * Show popup to confirm delete of author.
   *
   * @param messageAddModal Modal used
   */
  openAddModal(messageAddModal: TemplateRef<any>) {
    this.modalService.open(messageAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }
}
