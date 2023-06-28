package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageTransformDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OcrRecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OcrTaskDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OcrTaskMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.OcrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.invoke.MethodHandles;

/**
 * Contains all endpoints for Ocr.
 */
@RestController
@RequestMapping(value = "/api/v1/ocr")
public class OcrEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OcrService ocrService;

    private final OcrTaskMapper ocrMapper;

    public OcrEndpoint(OcrService ocrService, OcrTaskMapper ocrMapper) {
        this.ocrService = ocrService;
        this.ocrMapper = ocrMapper;
    }

    /**
     * Creates a new OcrTask by uploading a file.
     *
     * @param file The file to upload
     * @return Returns the created Ocr task
     * @throws ValidationException Thrown, if the uploaded file does not have the correct file type
     */
    @PostMapping(
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
        produces = MediaType.ALL_VALUE
    )
    @Secured("ROLE_USER")
    @Operation(summary = "Create a new ocr task", security = @SecurityRequirement(name = "apiKey"))
    public OcrTaskDto create(@RequestParam("image") MultipartFile file) throws ValidationException {
        LOGGER.trace("create({})", file.getOriginalFilename());
        var res = ocrService.create(file);
        return ocrMapper.ocrTaskToOcrTaskDto(res);
    }

    /**
     * Prepares the image for ocr.
     *
     * @param id The id of the task to prepare.
     * @param imageTransformDto The transform data used to transform the image.
     * @return Returns the updated ocr task
     * @throws ValidationException Thrown if the step of the task is not PENDING
     */
    @PostMapping("/{id}")
    @Secured("ROLE_USER")
    @Operation(summary = "Prepare an ocr task", security = @SecurityRequirement(name = "apiKey"))
    public OcrTaskDto prepare(@PathVariable Long id, @RequestBody ImageTransformDto imageTransformDto) throws ValidationException {
        LOGGER.trace("prepare({}, {})", id, imageTransformDto);
        return ocrMapper.ocrTaskToOcrTaskDto(ocrService.prepare(id, imageTransformDto));
    }

    /**
     * Execute ocr for the given task.
     *
     * @param id The id of the task
     * @return Returns the text read from the image.
     * @throws ValidationException Thrown if the task is not at the step PREPARATION
     */
    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    @Operation(summary = "ocr the given task", security = @SecurityRequirement(name = "apiKey"))
    public OcrRecipeDto ocr(@PathVariable Long id) throws ValidationException {
        LOGGER.trace("ocr({})", id);
        return ocrService.ocr(id);
    }

    /**
     * Finish the given task.
     *
     * @param id The id of the task
     * @throws ValidationException Thrown if the task is not at the step FINISHED
     */
    @DeleteMapping("/{id}")
    @Secured("ROLE_USER")
    @Operation(summary = "finish the given task", security = @SecurityRequirement(name = "apiKey"))
    public void finish(@PathVariable Long id) throws ValidationException {
        LOGGER.trace("finish({})", id);
        ocrService.finish(id);
    }
}
