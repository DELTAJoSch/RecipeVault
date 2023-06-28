package at.ac.tuwien.sepm.groupphase.backend.engines;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageTransformDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

/**
 * Prepare an image file for tesseract.
 */
public interface ImagePreparationEngine {
    /**
     * Loads the specified image file and transforms it according to the specified transform data.
     * Saves the transformed image under imageFile-transform.
     *
     * @param imageFile The path to the imageFile to transform
     * @param transform The transform data
     * @throws NotFoundException   Thrown, if the specified image could not be found
     * @throws ValidationException Thrown, if the transform coordinates are out of bounds for the image
     */
    void prepare(String imageFile, ImageTransformDto transform) throws NotFoundException, ValidationException;
}
