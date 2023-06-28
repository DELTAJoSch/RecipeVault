import {Component, HostListener, OnInit, TemplateRef} from '@angular/core';
import {Author} from '../../../dtos/author';
import {AuthorService} from '../../../services/author.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../../../services/auth.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Location} from '@angular/common';
import { ErrorService } from 'src/app/services/error.service';
import {ImageService} from '../../../services/image.service';

@Component({
  selector: 'app-author-details',
  templateUrl: './author-details.component.html',
  styleUrls: ['./author-details.component.scss']
})
export class AuthorDetailsComponent implements OnInit {
  author: Author = {
    id: null,
    firstname: null,
    lastname: null,
    description: null,
    imageId: null
  };
  authorId: number = null;
  calledByAdmin = this.authService.getUserRole() === 'ADMIN';
  mobile = false;

  imageUrl: string = null;

  constructor(
    private authorService: AuthorService,
    private imageService: ImageService,
    private router: Router,
    private route: ActivatedRoute,
    private notification: ToastrService,
    private authService: AuthService,
    private modalService: NgbModal,
    private errorService: ErrorService,
    private location: Location) {
  }

  /**
   * React to resize events
   */
  @HostListener('window:resize')
  onResize() {
    this.mobile = window.innerWidth<=1000;
  }

  /**
   * Initialize component based on selected mode
   */
  ngOnInit(): void {
    this.mobile = window.innerWidth <= 1000;
    this.authorId = Number(this.route.snapshot.paramMap.get('id'));

    this.authorService.getAuthorById(this.authorId).subscribe({
      next: author => {
        this.author = author;
        if (author.imageId != null){
          this.loadImage();
        }
      },
      error: error => {
        this.errorService.handleError(error, 'Autor konnte nicht geladen werden');
        this.location.back();
      }
    });
  }

  /**
   * Delete author for which the details page is currently viewed.
   *
   */
  delete() {
    this.authorService.deleteAuthor(this.authorId).subscribe({
      next: _ => {
        this.notification.success('Autor gelöscht!');
        this.modalService.dismissAll('close');
        this.location.back();
      },
      error: error => {
        this.errorService.handleError(error, `Autor konnte nicht gelöscht werden`);
        this.modalService.dismissAll();
      }
    });
  }

  /**
   * Load previous page on click of cancel-button
   */
  onCancelClick() {
    this.location.back();
  }

  /**
   * show edit component for currently viewed author
   */
  onEditClick() {
    this.router.navigateByUrl('author/' + this.authorId + '/edit');
  }

  /**
   * Show popup to confirm delete of author.
   *
   * @param messageAddModal Modal used
   */
  openAddModal(messageAddModal: TemplateRef<any>) {
    this.modalService.open(messageAddModal, {ariaLabelledBy: 'modal-basic-title'});
  }

  /**
   * loads image from backend if there is an imageId
   */
  loadImage(){
    if(this.author.imageId != null){
      this.imageService.getImage(this.author.imageId).subscribe({
        next : imageBlob => {
          this.imageUrl = URL.createObjectURL(imageBlob);
        }, error: err => {
          this.errorService.handleError(err,'Bild konnte nicht geladen werden');
        }
      });
    }
  }
}
