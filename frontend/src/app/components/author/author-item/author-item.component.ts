import {Component, EventEmitter, HostListener, Input, OnInit, Output, TemplateRef} from '@angular/core';
import {Router} from '@angular/router';
import {AuthService} from '../../../services/auth.service';
import {ToastrService} from 'ngx-toastr';
import {MatDialog} from '@angular/material/dialog';
import {ErrorService} from '../../../services/error.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Author} from '../../../dtos/author';
import {AuthorService} from '../../../services/author.service';

@Component({
  selector: 'app-author-item',
  templateUrl: './author-item.component.html',
  styleUrls: ['./author-item.component.scss']
})
export class AuthorItemComponent implements OnInit{
  @Input() author: Author = {
    id: 0,
    firstname: '',
    lastname: '',
    description: ''
  };

  @Output() delete: EventEmitter<void> = new EventEmitter<void>();

  mobile = false;

  constructor(
    private router: Router,
    private authorService: AuthorService,
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
   * Gets called when an author should be deleted.
   */
  deleteAuthor() {
    this.authorService.deleteAuthor(this.author.id).subscribe({
      next: _=> {
        this.notification.success(`Autor ${this.author.firstname + ' ' + this.author.lastname} gelöscht!`);
        this.modalService.dismissAll();
        this.delete.emit();
      },
      error: error => {
        this.errorService.handleError(error, 'Autor wurde nicht gelöscht');
        this.modalService.dismissAll();
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
   * Show popup to confirm delete of author.
   *
   * @param messageAddModal Modal used
   */
  openAddModal(messageAddModal: TemplateRef<any>) {
    this.modalService.open(messageAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }
}
