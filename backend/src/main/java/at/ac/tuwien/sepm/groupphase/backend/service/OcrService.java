package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageTransformDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OcrRecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.OcrTask;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

/**
 * This service handles all tasks related to OCR.
 */
public interface OcrService {
    /**
     * Creates a new OcrTask when a file is uploaded.
     *
     * @param file The uplaoded file
     * @return Returns the created tasks data
     * @throws ValidationException Thrown, if the file is of an invalid type
     */
    OcrTask create(MultipartFile file) throws ValidationException;

    /**
     * Prepares the image specified by the id and transforms it using the specified transform data.
     *
     * @param id        The id of the OcrTask
     * @param transform The transform data
     * @return Returns the OcrTask
     * @throws NotFoundException   Thrown, if the task could not be found
     * @throws ValidationException Thrown if the task is not in step PENDING
     */
    OcrTask prepare(Long id, ImageTransformDto transform) throws NotFoundException, ValidationException;

    /**
     * Execute the actual OCR step.
     *
     * @param id The id of the OcrTask
     * @return Returns the resulting output
     * @throws NotFoundException   Thrown, if the task could not be found
     * @throws ValidationException Thrown if the task is not in step READING
     */
    OcrRecipeDto ocr(Long id) throws NotFoundException, ValidationException;

    /**
     * Finishes the OcrTask and cleans up the resources.
     *
     * @param id The id of the task to finish
     * @throws NotFoundException   Thrown, if the task to finish could not be found.
     * @throws ValidationException Thrown if the task is not in step FINISHED
     */
    void finish(Long id) throws NotFoundException, ValidationException;

    /**
     * Cleans up all orphaned tasks. (Tasks that are older than 1 day).
     */
    void clean();
}
