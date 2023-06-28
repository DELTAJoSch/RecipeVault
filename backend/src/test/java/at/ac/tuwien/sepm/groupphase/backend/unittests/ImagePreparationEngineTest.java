package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageTransformDto;
import at.ac.tuwien.sepm.groupphase.backend.engines.ImagePreparationEngine;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ImagePreparationEngineTest {
    @Autowired
    private ImagePreparationEngine engine;

    @Test
    public void prepare_WhenCalledWithValidArgumentsForSsdImage_TransformsImageCorrectly() throws Exception {
        String path = "src/test/resources";

        File file = new File(path);
        String testDataPath = file.getAbsolutePath();

        String imageFile = testDataPath + "/images/test_image_ssd.jpeg";
        assertTrue(Files.exists(Path.of(imageFile)));

        ImageTransformDto transform = new ImageTransformDto.Builder()
            .topLeftX(407)
            .topLeftY(650)
            .topRightX(708)
            .topRightY(221)
            .bottomLeftX(1012)
            .bottomLeftY(1108)
            .bottomRightX(1346)
            .bottomRightY(668)
            .build();

        engine.prepare(imageFile, transform);

        String result = path + "/images/test_image_ssd-transformed.png";
        String reference = path + "/images/test_image_ssd-transformed-reference.png";
        assertTrue(Files.exists(Path.of(result)));

        var image = Imgcodecs.imread(result, Imgcodecs.IMREAD_GRAYSCALE);
        var referenceImage = Imgcodecs.imread(reference, Imgcodecs.IMREAD_GRAYSCALE);

        // compare that values are within bounds
        assertEquals(image.height(), referenceImage.height());
        assertEquals(image.width(), referenceImage.width());

        double diff = 0.0;

        for (int i = 0; i < image.height(); i++) {
            for (int j = 0; j < image.width(); j++) {
                var pixel = image.get(i, j);
                var referencePixel = referenceImage.get(i, j);

                diff += Math.abs(pixel[0] - referencePixel[0]);
            }
        }

        double avg = diff / (image.height() * image.width());
        double percentage = (avg / 255) * 100;

        assertTrue(percentage <= 0.05);
        assertTrue(percentage >= 0.0);
    }

    @Test
    public void prepare_WhenCalledWithValidArgumentsForGlenMorayImage_TransformsImageCorrectly() throws Exception {
        String path = "src/test/resources";

        File file = new File(path);
        String testDataPath = file.getAbsolutePath();

        String imageFile = testDataPath + "/images/test_image_glen_moray.jpeg";
        assertTrue(Files.exists(Path.of(imageFile)));

        ImageTransformDto transform = new ImageTransformDto.Builder()
            .topLeftX(1072)
            .topLeftY(322)
            .topRightX(1277)
            .topRightY(429)
            .bottomLeftX(318)
            .bottomLeftY(757)
            .bottomRightX(474)
            .bottomRightY(993)
            .build();

        engine.prepare(imageFile, transform);

        String result = path + "/images/test_image_glen_moray-transformed.png";
        String reference = path + "/images/test_image_glen_moray-transformed-reference.png";
        assertTrue(Files.exists(Path.of(result)));

        var image = Imgcodecs.imread(result, Imgcodecs.IMREAD_GRAYSCALE);
        var referenceImage = Imgcodecs.imread(reference, Imgcodecs.IMREAD_GRAYSCALE);

        // compare that values are within bounds
        assertEquals(image.height(), referenceImage.height());
        assertEquals(image.width(), referenceImage.width());

        double diff = 0.0;

        for (int i = 0; i < image.height(); i++) {
            for (int j = 0; j < image.width(); j++) {
                var pixel = image.get(i, j);
                var referencePixel = referenceImage.get(i, j);

                diff += Math.abs(pixel[0] - referencePixel[0]);
            }
        }

        double avg = diff / (image.height() * image.width());
        double percentage = (avg / 255) * 100;

        assertTrue(percentage <= 0.050);
        assertTrue(percentage >= 0.0);
    }

    @Test
    public void prepare_WhenCalledWithValidArgumentsForBolognaImage_TransformsImageCorrectly() throws Exception {
        String path = "src/test/resources";

        File file = new File(path);
        String testDataPath = file.getAbsolutePath();

        String imageFile = testDataPath + "/images/test_image_bologna.jpeg";
        assertTrue(Files.exists(Path.of(imageFile)));

        ImageTransformDto transform = new ImageTransformDto.Builder()
            .topLeftX(788)
            .topLeftY(358)
            .topRightX(1035)
            .topRightY(527)
            .bottomLeftX(323)
            .bottomLeftY(589)
            .bottomRightX(531)
            .bottomRightY(873)
            .build();

        engine.prepare(imageFile, transform);

        String result = path + "/images/test_image_bologna-transformed.png";
        String reference = path + "/images/test_image_bologna-transformed-reference.png";
        assertTrue(Files.exists(Path.of(result)));

        var image = Imgcodecs.imread(result, Imgcodecs.IMREAD_GRAYSCALE);
        var referenceImage = Imgcodecs.imread(reference, Imgcodecs.IMREAD_GRAYSCALE);

        // compare that values are within bounds
        assertEquals(image.height(), referenceImage.height());
        assertEquals(image.width(), referenceImage.width());

        double diff = 0.0;

        for (int i = 0; i < image.height(); i++) {
            for (int j = 0; j < image.width(); j++) {
                var pixel = image.get(i, j);
                var referencePixel = referenceImage.get(i, j);

                diff += Math.abs(pixel[0] - referencePixel[0]);
            }
        }

        double avg = diff / (image.height() * image.width());
        double percentage = (avg / 255) * 100;

        assertTrue(percentage <= 0.050);
        assertTrue(percentage >= 0.0);
    }

    @Test
    public void prepare_WhenCalledWithInvalidImage_ThrowsNotFoundException() throws Exception {
        String imageFile = "/a/very/cool/path/with_an_image.png";
        assertFalse(Files.exists(Path.of(imageFile)));

        ImageTransformDto transform = new ImageTransformDto.Builder()
            .topLeftX(788)
            .topLeftY(358)
            .topRightX(1035)
            .topRightY(527)
            .bottomLeftX(323)
            .bottomLeftY(589)
            .bottomRightX(531)
            .bottomRightY(873)
            .build();

        assertThrows(NotFoundException.class, () -> engine.prepare(imageFile, transform));
    }

}
