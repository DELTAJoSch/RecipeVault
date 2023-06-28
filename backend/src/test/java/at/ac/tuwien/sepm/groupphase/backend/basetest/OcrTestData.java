package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageTransformDto;

import java.util.Arrays;
import java.util.List;

public interface OcrTestData {
    List<String> WHITELIST_FILES = Arrays.asList(
        "pending.jpg",
        "preparation.jpg",
        "reading.jpg",
        "finished.jpg"
    );

    String BASE_PATH = "ocr/";

    Long PENDING = -1L;
    Long PREPARATION = -2L;
    Long READING = -3L;
    Long FINISHED = -4L;

    String PENDING_OUTPUT_PATH = BASE_PATH + "pending-transformed.png";

    String FINISHED_PATH = BASE_PATH + "finished.png";

    ImageTransformDto TRANSFORM_DTO = new ImageTransformDto.Builder()
        .topLeftX(0)
        .topLeftY(10)
        .topRightX(50)
        .topRightY(100)
        .bottomLeftX(0)
        .bottomLeftY(250)
        .bottomRightX(150)
        .bottomRightY(225)
        .build();
}
