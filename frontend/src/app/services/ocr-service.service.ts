import { HttpClient, HttpEvent, HttpRequest, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { ImageTransform } from '../dtos/transform';
import { Observable } from 'rxjs';
import { OcrTask } from '../dtos/ocr-task';
import { OcrResult } from '../dtos/ocr-result';

@Injectable({
  providedIn: 'root'
})
export class OcrService {

  private ocrBaseUri: string = this.globals.backendUri + '/ocr';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
    ) {}

  /**
   * Create a new ocr task
   *
   * @param file The image to upload
   * @returns Returns the created ocr task
   */
  createOcrTask(file: File): Observable<HttpEvent<OcrTask>> {
    const formData = new FormData();
    formData.append('image', file);

    const request = new HttpRequest(
      'POST',
      this.ocrBaseUri,
      formData,
      {
        reportProgress: true,
        responseType: 'json'
      }
    );

    return this.httpClient.request<OcrTask>(request);
  }

  /**
   * Prepare the Ocr task using transform
   *
   * @param id The id of the task
   * @param transform The transform data
   * @returns The ocr task that was transformed
   */
  prepareOcrTask(id: number, transform: ImageTransform): Observable<OcrTask> {
    return this.httpClient.post<OcrTask>(this.ocrBaseUri + `/${id}`, transform); //TODO: Add Timeout if necessary
  }

  /**
   * Execute ocr on the image
   *
   * @param id The id of the task
   * @returns The ingested text
   */
  executeOcr(id: number): Observable<OcrResult> {
    return this.httpClient.get<OcrResult>(this.ocrBaseUri + `/${id}`);
  }

  /**
   * deletes a finished ocr task
   *
   * @param id The id of the ocr task to delete
   * @returns The returned data
   */
  finish(id: number): Observable<any> {
    return this.httpClient.delete(this.ocrBaseUri + `/${id}`);
  }
}
