import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {AuthService} from '../../../services/auth.service';
import {Author} from '../../../dtos/author';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Location} from '@angular/common';
import {NgForm} from '@angular/forms';
import {AuthorService} from '../../../services/author.service';
import { ErrorService } from 'src/app/services/error.service';
import {ImageService} from '../../../services/image.service';

@Component({
  selector: 'app-author-create-edit',
  templateUrl: './author-create-edit.component.html',
  styleUrls: ['./author-create-edit.component.scss']
})
export class AuthorCreateEditComponent implements OnInit {
  @Input() edit = false;
  author: Author = {
    id: null,
    firstname: null,
    lastname: null,
    description: null
  };
  authorId: number = null;
  calledByAdmin = this.authService.getUserRole() === 'ADMIN';

  selectedFile: File;

  imageUrl: string = null;

  newImageUrl: string = null;

  maxSizeInBytes: number = 20 * 1024 * 1024; // 20MB

  imageDelete = false;

  imageToLarge = false;

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
   * Initialize component based on selected mode.
   */
  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.edit = data.edit;
    });
    // if used for edit, load author details from backend
    if (this.edit === true) {
      this.authorId = Number(this.route.snapshot.paramMap.get('id'));

      this.authorService.getAuthorById(this.authorId).subscribe({
        next: author => {
          this.author = author;
          if (this.author.imageId != null) {
            this.loadImage();
          }
        },
        error: err => {
          this.errorService.handleError(err, 'Autor konnte nicht geladen werden');
          this.location.back();
        }
      });
    }
  }

  /**
   * Submit the form with the filled in author details.
   *
   * @param form The form to submit
   */
  async onSubmit(form: NgForm) {
    if (form.valid) {
      if (this.author.firstname === '') {
        delete this.author.firstname;
      }
      if (this.author.lastname === '') {
        delete this.author.lastname;
      }
      if (this.author.description === '') {
        delete this.author.description;
      }
      if (this.imageDelete && !this.selectedFile) {
        this.deleteImage();
      }

      await this.uploadImage();

      if (this.edit) {
        this.author.id = this.authorId;
        this.update(this.author);
      } else {
        this.create(this.author);
      }
    } else {
      this.notification.warning('Es fehlen Daten zur Erstellung!');
    }
  }


  /**
   * Create new author with given data.
   *
   * @param author The data to create new author from
   */
  create(author: Author) {
    this.authorService.createAuthor(author).subscribe({
      next: _ => {
        this.notification.success(`Neuer Autor ${author.firstname} ${author.lastname} erstellt!`);
        this.location.back();
      },
      error: err => {
        this.errorService.handleError(err,`Neuer Autor ${author.firstname} ${author.lastname} konnte nicht erstellt weden`);
        this.location.back();
      }
    });
  }

  /**
   * Update author with given data.
   *
   * @param author The data to update the author with.
   */
  update(author: Author) {
    this.authorService.updateAuthor(author).subscribe({
      next: _ => {
        this.notification.success(`Autor ${author.firstname} ${author.lastname} erfolgreich bearbeitet!`);
        this.location.back();
      },
      error: error => {
        this.errorService.handleError(error, `Autor ${author.firstname} ${author.lastname} konnte nicht geupdated werden`);
        this.location.back();
      }
    });
  }

  /**
   * Delete current author. (viewed in component)
   */
  delete() {
    this.authorService.deleteAuthor(this.authorId).subscribe({
      next: _ => {
        this.notification.success(`Autor gelöscht!`);
        this.modalService.dismissAll();
        this.location.back();
      },
      error: error => {
        this.errorService.handleError(error, `Autor konnte nicht gelöscht werden`, '/author');
        this.modalService.dismissAll();
      }
    });
  }

  /**
   * Go back to previous page (on cancel button click)
   */
  onCancelClick() {
    this.location.back();
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
   * If a file gets selected makes it the selected File and generates an Url so it can be displayed
   *
   * @param event that a file gets selected from directory
   */
  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (file && file.size <= this.maxSizeInBytes) {
      this.imageToLarge = false;
      // File size is within the allowed limit
      this.selectedFile = file;
      this.newImageUrl = URL.createObjectURL(this.selectedFile);
    } else {
      // File size exceeds the allowed limit
      this.selectedFile = null;
      this.imageToLarge = true;
    }
  }

  /**
   *  Saves or updates an image in the backend and lets the OnSubmit wait till it is finished
   *  If it saves an image it gives the author an imageId
   */
  uploadImage(): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      if (this.selectedFile != null) {
        if (this.author.imageId != null) {
          this.imageService.updateImage(this.selectedFile, this.author.imageId).subscribe(
            () => {
              resolve(); // Image upload completed, resolve the promise
            },
            error => {
              this.errorService.handleError(error,'Bild konnte nicht gespeichert werden');
              reject(error); // Image upload failed, reject the promise
            }
          );
        } else {
          this.imageService.saveImage(this.selectedFile).subscribe(
            value => {
              this.author.imageId = value;
              resolve(); // Image upload completed, resolve the promise
            },
            error => {
              this.errorService.handleError(error,'Bild konnte nicht gespeichert werden');
              reject(error); // Image upload failed, reject the promise
            }
          );
        }
      } else {
        resolve(); // No image selected, resolve the promise
      }
    });
  }

  /**
   * Deletes image in backend and sets imageId to null
   */
  deleteImage() {
    if (this.author.imageId != null) {
      this.imageService.deleteImage(this.author.imageId).subscribe();
      this.author.imageId = null;
    }
  }

  /**
   * loads image from backend if there is an imageId
   */
  loadImage() {
    if (this.author.imageId != null) {
      this.imageService.getImage(this.author.imageId).subscribe({
        next: imageBlob => {
          this.imageUrl = URL.createObjectURL(imageBlob);
        }, error: err => {
          this.errorService.handleError(err,'Bild konnte nicht geladen werden');
        }
      });
    }
  }
}
