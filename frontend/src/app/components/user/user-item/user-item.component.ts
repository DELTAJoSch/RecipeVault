import {Component, EventEmitter, HostListener, Input, OnInit, Output, TemplateRef} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {MatDialog} from '@angular/material/dialog';
import {ErrorService} from '../../../services/error.service';
import {UserService} from '../../../services/user.service';
import {User} from '../../../dtos/user';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-user-item',
  templateUrl: './user-item.component.html',
  styleUrls: ['./user-item.component.scss']
})
export class UserItemComponent implements OnInit{
  @Input() user: User = {
    id: 0,
    email: '',
    password: '',
    admin: false
  };

  @Output() delete: EventEmitter<void> = new EventEmitter<void>();

  mobile = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService,
    private notification: ToastrService,
    private dialog: MatDialog,
    private errorService: ErrorService,
    private modalService: NgbModal,
  ){}

  @HostListener('window:resize')
  onResize() {
    this.mobile = window.innerWidth <= 900;
  }

  /**
   * Initialize this component.
   */
  ngOnInit() {
    this.mobile = window.innerWidth <= 900;
  }

  /**
   * Gets called when a user should be updated, opens update-dialog.
   */
  onEditClick() {
    if(this.user.email === this.authService.getUserEmail()) {
      this.router.navigateByUrl('user/me/edit');
    } else {
      this.router.navigateByUrl('user/' + this.user.id + '/edit');
    }
  }

  /**
   * Gets called when a user should be deleted.
   */
  deleteUser() {
    this.userService.deleteUser(this.user.id).subscribe({
      next: _=> {
        this.notification.success(`Benutzer ${this.user.email} gelöscht!`);
        this.modalService.dismissAll('close');
        this.delete.emit();
      },
      error: error => {
        this.errorService.handleError(error, 'Benutzer wurde nicht gelöscht');
        this.modalService.dismissAll('close');
      }
    });
  }

  /**
   * Returns if current user is an admin.
   */
  isAdmin() {
    return this.authService.isAdmin();
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
