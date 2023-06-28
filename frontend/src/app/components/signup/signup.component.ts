import { Component, Input, OnInit, TemplateRef } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { AuthService } from '../../services/auth.service';
import { User } from '../../dtos/user';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import { ErrorService } from 'src/app/services/error.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  @Input() signup = true;
  @Input() currentUser = true;
  calledByAdmin = this.authService.getUserRole() === 'ADMIN';

  user: User = {
    id: null,
    email: null,
    password: null,
    admin: false
  };
  userId: number = null;
  repeatPassword: string = null;
  submitDisable = false;
  detailComponentPath: string = null;

  constructor(
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private authService: AuthService,
    private modalService: NgbModal,
    private location: Location,
    private errorService: ErrorService,
  ) {
  }

  /**
   * Initialize component based on selected data
   */
  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.signup = data.signup;
      this.currentUser = data.currentUser;
    });
    // if used for edit, load user details from backend
    if (this.signup === false) {
      if (this.currentUser === false) {
        this.userId = Number(this.route.snapshot.paramMap.get('id'));

        this.userService.getUserById(this.userId).subscribe({
          next: user => {
            this.user = user;
          },
          error: err => {
            this.errorService.handleError(err, `Benutzerdaten konnten nicht geladen werden`, 'user');
          }
        });
      } else {
        this.userService.getCurrentUser().subscribe({
          next: user => {
            this.user = user;
          },
          error: err => {
            this.errorService.handleError(err, 'Benutzerdaten konnten nicht geladen werden');
          }
        });
      }
    }
  }

  /**
   * Submit the form with the filled in user details.
   *
   * @param form The form to submit.
   */
  onSubmit(form: NgForm) {
    if (form.valid) {
      if (this.user.email === '') {
        delete this.user.email;
      }
      if (this.user.password === '') {
        delete this.user.password;
      }
      if (this.repeatPassword === '') {
        delete this.repeatPassword;
      }

      if (this.signup) {
        if (this.calledByAdmin) {
          this.create(this.user);
        } else {
          this.signUp(this.user);
        }
      } else {
        this.user.id = this.userId;
        this.update(this.user);
      }
    } else {
      this.notification.warning('Es fehlen Daten zur erstellung!');
    }
  }

  /**
   * Sign-up a new user.
   *
   * @param user Data of the user to be created
   */
  signUp(user: User) {
    this.userService.signUpUser(user).subscribe({
      next: _ => {
        this.notification.success(`Nutzer wurde erstellt!`);
        this.router.navigateByUrl('/login');
      },
      error: error => {
        this.errorService.handleError(error, `Benutzer konnten nicht registriert werden`);
      }
    });
  }

  /**
   * Update user Data of a given user.
   *
   * @param user Data to update the user details with.
   */
  update(user: User) {
    if (this.currentUser) {
      this.userService.updateCurrentUser(user).subscribe({
        next: _ => {
          // if user updates own account user has to log in again
          this.notification.success('Benutzer wurde erfolgreich geändert! Bitte erneut einloggen.');
          this.authService.logoutUser();
          this.router.navigateByUrl('/');
        },
        error: err => {
          this.errorService.handleError(err, 'Benutzer konnte nicht geändert werden.');
        }
      });
    } else {
      this.userService.updateUser(user, user.id).subscribe({
        next: _ => {
          this.notification.success('Nutzer wurde erfolgreich verändert!');
          this.location.back();
        },
        error: err => {
          this.errorService.handleError(err, 'Benutzerdaten konnten nicht geladen werden');
        }
      });
    }
  }

  /**
   * Create new User from given user data. (Only called by Admins)
   * Normal Users have to sign up to create an account.
   *
   * @param user Data of the user to be created.
   */
  create(user: User) {
    this.userService.createUser(user).subscribe({
      next: _ => {
        this.notification.success(`Nutzer wurde erstellt!`);
        if (this.authService.isAdmin()) {
          this.router.navigateByUrl('/users');
        }
      },
      error: error => {
        this.errorService.handleError(error, `Benutzer konnten nicht erstellt werden`, '/users');
      }
    });
  }

  /**
   * Delete user with the given id.
   *
   * @param id Id of the user to be deleted.
   */
  delete(id: number) {
    this.userService.deleteUser(id).subscribe({
      next: _ => {
        this.notification.success(`Nutzer gelöscht!`);
        this.modalService.dismissAll();
        if (this.calledByAdmin) {
          this.router.navigateByUrl('/users');
          if (!this.authService.isLoggedIn()) {
            this.router.navigateByUrl('/');
          }
        } else {
          this.router.navigateByUrl('/');
        }
      },
      error: error => {
        this.errorService.handleError(error, `Benutzer konnten nicht gelöscht werden`);
        this.modalService.dismissAll();
      }
    });
  }


  /**
   * check if given passwords are matching or not.
   * this.submitDisable is true if the passwords don´t match.
   */
  checkPass() {
    this.submitDisable = this.user.password !== this.repeatPassword;
  }

  /**
   * Load previous page. (On click of cancel button)
   */
  onCancelClick() {
    if (this.signup) {
      this.router.navigateByUrl('/');
    } else {
      this.location.back();
    }
  }

  /**
   * Show popup to confirm delete of user.
   *
   * @param messageAddModal Modal used
   */
  openAddModal(messageAddModal: TemplateRef<any>) {
    this.modalService.open(messageAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }
}
