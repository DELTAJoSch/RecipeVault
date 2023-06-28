import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import Konva from 'konva';
import { ToastrService } from 'ngx-toastr';
import { OcrTask } from 'src/app/dtos/ocr-task';
import { ImageTransform } from 'src/app/dtos/transform';
import { ErrorService } from 'src/app/services/error.service';
import { OcrService } from 'src/app/services/ocr-service.service';

@Component({
  selector: 'app-ocr-upload',
  templateUrl: './ocr-upload.component.html',
  styleUrls: ['./ocr-upload.component.scss']
})
export class OcrUploadComponent {
  imageSrc: string;
  file: File;
  uploaded = false;
  ocrTask: OcrTask = null;

  progress = 0;
  showSpinner = false;
  message = '';

  stage: Konva.Stage;

  sceneDim = 2500;
  scaleFactor = 0;
  inset = 5; // the inset specifies how much the initial positions are moved by

  boundingLayer: Konva.Layer;
  topLeft: [number, number] = [0, 0];
  topRight: [number, number] = [0, 0];
  bottomLeft: [number, number] = [0, 0];
  bottomRight: [number, number] = [0, 0];

  topLeftHandle: Konva.Circle;
  topRightHandle: Konva.Circle;
  bottomLeftHandle: Konva.Circle;
  bottomRightHandle: Konva.Circle;

  leftLine: Konva.Line;
  rightLine: Konva.Line;
  topLine: Konva.Line;
  bottomLine: Konva.Line;

  /**
   * Constructor
   */
  constructor(
    private notification: ToastrService,
    private ocrService: OcrService,
    private router: Router,
    private errorService: ErrorService,
  ){}

  /**
   * Resize the canvas (scale) when the window size changes.
   */
  @HostListener('window:resize')
  onSizeChange(){
    if(this.uploaded){
      this.fitCanvas();
    }
  }

  /**
   * Re-generate the canvas when the file changes.
   *
   * @param files The new file
   */
  uploadChanged(files: FileList): void {
    this.file = files[0];

    const reader = new FileReader();
    reader.onload = e => {
      this.imageSrc = reader.result as string;
      this.uploaded = true;
      this.prepareCanvas();
    };

    reader.readAsDataURL(this.file);
  }

  /**
   * Prepare the canvas for a new image
   *
   * @description Creates a new canvas, then populates it with the scaled image.
   * Generates the handles at the edges and the lines between them, draws them for the initial positions.
   */
  prepareCanvas(): void {
    const img = new Image();

    img.onload = e => {
      this.stage = new Konva.Stage({
        container: 'container',
        width: this.sceneDim,
        height: this.sceneDim
      });

      const imgLayer = new Konva.Layer();

      const imgObj = new Konva.Image({
        image: img
      });

      // scale the image to the borders of the stage
      this.scaleFactor = this.sceneDim/Math.max(imgObj.width(), imgObj.height());
      imgObj.scale({x: this.scaleFactor, y: this.scaleFactor});

      // calculate bounding coordinates for the bounding box
      this.topLeft[0] = this.inset * this.scaleFactor;
      this.topLeft[1] = this.inset * this.scaleFactor;
      this.bottomLeft[0] = this.inset * this.scaleFactor;
      this.bottomLeft[1] = imgObj.height() * this.scaleFactor - this.inset * this.scaleFactor;
      this.topRight[0] = imgObj.width() * this.scaleFactor - this.inset * this.scaleFactor;
      this.topRight[1] = this.inset * this.scaleFactor;
      this.bottomRight[0] = imgObj.width() * this.scaleFactor - this.inset * this.scaleFactor;
      this.bottomRight[1] = imgObj.height() * this.scaleFactor - this.inset * this.scaleFactor;

      // create bounding box handles
      this.topLeftHandle = new Konva.Circle({
        radius: 25,
        fill: '#ffb030',
        stroke: '#F2E9DC',
        strokeWidth: 4,
        x: this.topLeft[0],
        y: this.topLeft[1],
        draggable: true
      });

      this.topRightHandle = new Konva.Circle({
        radius: 25,
        fill: '#ffb030',
        stroke: '#F2E9DC',
        strokeWidth: 4,
        x: this.topRight[0],
        y: this.topRight[1],
        draggable: true
      });

      this.bottomLeftHandle = new Konva.Circle({
        radius: 25,
        fill: '#ffb030',
        stroke: '#F2E9DC',
        strokeWidth: 4,
        x: this.bottomLeft[0],
        y: this.bottomLeft[1],
        draggable: true
      });

      this.bottomRightHandle = new Konva.Circle({
        radius: 25,
        fill: '#ffb030',
        stroke: '#F2E9DC',
        strokeWidth: 4,
        x: this.bottomRight[0],
        y: this.bottomRight[1],
        draggable: true
      });

      // create lines
      this.leftLine = new Konva.Line({
        points: [this.bottomLeft[0], this.bottomLeft[1], this.topLeft[0], this.topLeft[1]],
        stroke: '#ffb030',
        lineCap: 'round',
        strokeWidth: 10
      });

      this.topLine = new Konva.Line({
        points: [this.topLeft[0], this.topLeft[1], this.topRight[0], this.topRight[1]],
        stroke: '#ffb030',
        lineCap: 'round',
        strokeWidth: 10
      });

      this.rightLine = new Konva.Line({
        points: [this.topRight[0], this.topRight[1], this.bottomRight[0], this.bottomRight[1]],
        stroke: '#ffb030',
        lineCap: 'round',
        strokeWidth: 10
      });

      this.bottomLine = new Konva.Line({
        points: [this.bottomRight[0], this.bottomRight[1], this.bottomLeft[0], this.bottomLeft[1]],
        stroke: '#ffb030',
        lineCap: 'round',
        strokeWidth: 10
      });

      //attach drag handlers
      this.topLeftHandle.on('dragmove', () => {
          this.topLeft[0] = this.topLeftHandle.x();
          this.topLeft[1] = this.topLeftHandle.y();
          this.drawBoundingBox();
      });

      this.topRightHandle.on('dragmove', () => {
        this.topRight[0] = this.topRightHandle.x();
        this.topRight[1] = this.topRightHandle.y();
        this.drawBoundingBox();
      });

      this.bottomLeftHandle.on('dragmove', () => {
        this.bottomLeft[0] = this.bottomLeftHandle.x();
        this.bottomLeft[1] = this.bottomLeftHandle.y();
        this.drawBoundingBox();
      });

      this.bottomRightHandle.on('dragmove', () => {
        this.bottomRight[0] = this.bottomRightHandle.x();
        this.bottomRight[1] = this.bottomRightHandle.y();
        this.drawBoundingBox();
      });

      imgLayer.add(imgObj);
      this.stage.add(imgLayer);

      // generate new bounding layer
      this.boundingLayer = new Konva.Layer();
      this.stage.add(this.boundingLayer);

      // add lines and handlers to bounding layer
      this.boundingLayer.add(this.leftLine);
      this.boundingLayer.add(this.rightLine);
      this.boundingLayer.add(this.bottomLine);
      this.boundingLayer.add(this.topLine);

      this.boundingLayer.add(this.topLeftHandle);
      this.boundingLayer.add(this.topRightHandle);
      this.boundingLayer.add(this.bottomLeftHandle);
      this.boundingLayer.add(this.bottomRightHandle);

      // draw new image and bounding box
      imgLayer.draw();
      this.drawBoundingBox();

      // scale all to fit canvas
      this.fitCanvas();
    };

    img.src = this.imageSrc;
  }

  /**
   * Darw or redraw the bounding box and the handles
   */
  drawBoundingBox(): void{
    this.boundingLayer.clear();

    this.topLine.points([this.topLeft[0], this.topLeft[1], this.topRight[0], this.topRight[1]]);
    this.bottomLine.points([this.bottomRight[0], this.bottomRight[1], this.bottomLeft[0], this.bottomLeft[1]]);
    this.leftLine.points([this.bottomLeft[0], this.bottomLeft[1], this.topLeft[0], this.topLeft[1]]);
    this.rightLine.points([this.topRight[0], this.topRight[1], this.bottomRight[0], this.bottomRight[1]]);

    this.boundingLayer.draw();
  }


  /**
   * Scale to fit canvas
   */
  fitCanvas(): void{
    const canvasCol = document.getElementById('canvasCol');
    const sz = canvasCol.offsetWidth - 0.05*canvasCol.offsetWidth;

    const scale = sz / this.sceneDim;
    this.stage.width(this.sceneDim * scale);
    this.stage.height(this.sceneDim * scale);
    this.stage.scale({ x: scale, y: scale });
  }

  /**
   * Send the images and the coordinates to the backend
   */
  sendClicked(): void {
    // prepare for send
    this.message = 'Hochladen';
    this.progress = 0;
    this.showSpinner = true;

    // create transform dto
    const transform = new ImageTransform();
    transform.topLeftX = this.topLeft[0]*(1/this.scaleFactor);
    transform.topLeftY = this.topLeft[1]*(1/this.scaleFactor);
    transform.topRightX = this.topRight[0]*(1/this.scaleFactor);
    transform.topRightY = this.topRight[1]*(1/this.scaleFactor);
    transform.bottomLeftX = this.bottomLeft[0]*(1/this.scaleFactor);
    transform.bottomLeftY = this.bottomLeft[1]*(1/this.scaleFactor);
    transform.bottomRightX = this.bottomRight[0]*(1/this.scaleFactor);
    transform.bottomRightY = this.bottomRight[1]*(1/this.scaleFactor);

    // send transform request
    this.ocrService.createOcrTask(this.file).subscribe(
      event => {
        // for progress update
        if (event.type === HttpEventType.UploadProgress) {
          this.progress = event.loaded / event.total;
        } else if (event instanceof HttpResponse) { // for responses
           const response = event as HttpResponse<any>;

           // status code == 2XX
           if(response.ok){
            // change text for transform
            this.ocrTask = response.body;
            this.message = 'Transformieren';

            // send transform request
            this.ocrService.prepareOcrTask(this.ocrTask.id, transform).subscribe({
              next: _ => {
                this.showSpinner = false;
                this.router.navigateByUrl(`/ocr/${this.ocrTask.id}/edit`);
              },
              error: error => {
                this.showSpinner = false;
                this.errorService.handleError(error, `OCR-Vorbereitung fehlgeschlagen`);
              }
            });
          } else { // for errors print error message
            this.errorService.handleError(response.body.error);
          }
        }
      }
    );
  }
}
