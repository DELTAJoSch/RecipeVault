package at.ac.tuwien.sepm.groupphase.backend.engines.implementations;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageTransformDto;
import at.ac.tuwien.sepm.groupphase.backend.engines.ImagePreparationEngine;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;
import java.lang.invoke.MethodHandles;
import java.util.Collections;

import static org.opencv.calib3d.Calib3d.findHomography;
import static org.opencv.core.Core.convertScaleAbs;
import static org.opencv.imgproc.Imgproc.warpPerspective;

/**
 * Image Preparation Engine that uses OpenCv.
 */
@Component
public class OpenCvImagePreparationEngine implements ImagePreparationEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void prepare(String imageFile, ImageTransformDto transform) throws NotFoundException {
        LOGGER.trace("prepare({}, {})", imageFile, transform);
        var image = this.loadImage(imageFile);

        if (image.empty()) {
            throw new NotFoundException("Bild konnte nicht am Server gefunden werden");
        }

        var transformed = this.transform(image, transform);
        var corrected = this.correctContrast(transformed);

        this.saveImage(corrected, imageFile);
    }

    /**
     * Loads the specified image file as a grayscale image.
     *
     * @param imageFile The image to load.
     * @return Returns the loaded image.
     */
    private Mat loadImage(String imageFile) {
        LOGGER.trace("loadImage({})", imageFile);
        return Imgcodecs.imread(imageFile, Imgcodecs.IMREAD_GRAYSCALE);
    }

    /**
     * Automatically corrects the contrast of the image.
     *
     * @param image The image to correct
     * @return Returns the corrected image
     */
    private Mat correctContrast(Mat image) {
        LOGGER.trace("correctContrast({})", image.dims());
        int histSize = 256;
        float[] histRange = {0, histSize};
        Mat hist = new Mat();
        Imgproc.calcHist(Collections.singletonList(image), new MatOfInt(0), new Mat(), hist, new MatOfInt(histSize), new MatOfFloat(histRange), true);

        double[] accumulator = new double[histSize];

        int min = 0;
        int max = histSize - 1;

        for (int i = 1; i < histSize; i++) {
            var temp = hist.get(i, 0);
            accumulator[i] = accumulator[i - 1] + temp[0];
        }

        double absMax = accumulator[accumulator.length - 1];
        double percentage = 0.05 * (absMax / 100);

        while (accumulator[min] < percentage) {
            min++;
        }

        while (accumulator[max] >= (absMax - percentage)) {
            max--;
        }

        double alpha = (double) 255 / (max - min);
        double beta = -min * alpha;

        var out = new Mat();
        convertScaleAbs(image, out, alpha, beta);
        return out;
    }

    /**
     * Transform the image with the specified transform values.
     *
     * @param image     The image to transform
     * @param transform The transform data
     * @return Returns the transformed image
     */
    private Mat transform(Mat image, ImageTransformDto transform) {
        LOGGER.trace("transform({})", image.dims(), transform);
        var srcPoints = new MatOfPoint2f(
            new Point(transform.getTopLeftX(), transform.getTopLeftY()),
            new Point(transform.getTopRightX(), transform.getTopRightY()),
            new Point(transform.getBottomRightX(), transform.getBottomRightY()),
            new Point(transform.getBottomLeftX(), transform.getBottomLeftY())
        );

        // get maximum width and height of the matrix
        // https://www.microsoft.com/en-us/research/publication/whiteboard-scanning-image-enhancement/?from=https://research.microsoft.com/en-us/um/people/zhang/papers/tr03-39.pdf&type=exact
        // https://stackoverflow.com/questions/38285229/calculating-aspect-ratio-of-perspective-transform-destination-image
        // The above code WOULD work great, if it weren't for Java. I tried this with Nd4j, but this is terribly slow and inefficient...

        var widthOne = Point2D.distance(
            transform.getTopLeftX(),
            transform.getTopLeftY(),
            transform.getTopRightX(),
            transform.getTopRightY()
        );
        var widthTwo = Point2D.distance(
            transform.getBottomLeftX(),
            transform.getBottomLeftY(),
            transform.getBottomRightX(),
            transform.getBottomRightY()
        );

        var heightOne = Point2D.distance(
            transform.getTopLeftX(),
            transform.getTopLeftY(),
            transform.getBottomLeftX(),
            transform.getBottomLeftY()
        );
        var heightTwo = Point2D.distance(
            transform.getTopRightX(),
            transform.getTopRightY(),
            transform.getBottomRightX(),
            transform.getBottomRightY()
        );

        var widthEuclidean = Math.max(widthOne, widthTwo);
        var heightEuclidean = Math.max(heightOne, heightTwo);

        var arVisible = widthEuclidean / heightEuclidean;

        double width = 1000;
        double height = width / arVisible;

        // create destination points
        var destPoints = new MatOfPoint2f(
            new Point(0, 0),
            new Point(width, 0),
            new Point(width, height),
            new Point(0, height)
        );

        // create warping matrix
        var warpMat = findHomography(srcPoints, destPoints);

        // warp
        var result = new Mat();
        warpPerspective(image, result, warpMat, new Size(width, height));
        return result;
    }

    /**
     * Save the image to the resulting file.
     *
     * @param image     The image to save.
     * @param imageFile The path to the ORIGINAL image. The resulting image is saved as imageFile-transformed.png
     */
    private void saveImage(Mat image, String imageFile) {
        LOGGER.trace("saveImage({}, {})", image.dims(), imageFile);
        var nameTokens = imageFile.split("\\.");
        var namePos = nameTokens.length - 2;

        if (namePos >= 0) {
            Imgcodecs.imwrite(nameTokens[namePos] + "-transformed.png", image);
        } else {
            throw new InternalServerException("Der Name vom zu transformierenden Bild konnte nicht errechnet werden Filename: " + imageFile);
        }
    }
}
