import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private imageBaseUri: string = this.globals.backendUri + '/image';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads image from the backend.
   *
   * @return imageUrl string
   */
  getImage(id: number): Observable<Blob> {
    const headers = new HttpHeaders().set('Accept', 'image/jpeg');
    return this.httpClient.get(this.imageBaseUri + '/' + id, { responseType: 'blob', headers });
  }

  /**
   * saves an image to the disc.
   *
   * @param file the image to save
   */
  saveImage(file: File): Observable<number> {
    const formData = new FormData();
    formData.append('image', file);

    return this.httpClient.put<number>(this.imageBaseUri, formData, { headers: {} });
  }

  /**
   * updates an image to the disc.
   *
   * @param file the image to save
   * @param id the id of the last image
   */
  updateImage(file: File, id: number) {
    const formData = new FormData();
    formData.append('image', file);

    return this.httpClient.post<number>(this.imageBaseUri + '/' + id, formData, { headers: {} });
  }

  /**
   * Remove an image from the disc.
   *
   * @param id the id of the image to delete
   */
  deleteImage(id: number) {
    console.log('Remove image from disc');
    return this.httpClient.delete(this.imageBaseUri + '/' + id);
  }
}
