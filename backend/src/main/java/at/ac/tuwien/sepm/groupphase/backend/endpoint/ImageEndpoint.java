package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;

@RestController
@RequestMapping(value = "/api/v1/image")
public class ImageEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private ImageService imageService;

    @Autowired
    public ImageEndpoint(ImageService imageService) {
        this.imageService = imageService;
    }


    /**
     * save an image.
     *
     * @param file The picture to save
     * @return the id of the image
     * @throws ValidationException if file type is not correct
     */
    @Secured("ROLE_USER")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "save Image", security = @SecurityRequirement(name = "apiKey"))
    public Long saveImage(@Valid @RequestParam("image") MultipartFile file)
        throws ValidationException {
        LOGGER.info("PUT /api/v1/image");
        return (imageService.saveImage(file));
    }

    /**
     * update the specified image.
     *
     * @param id   The id of the image to update
     * @param file The picture to save
     * @throws NotFoundException   Thrown if the image couldn't be found
     * @throws ValidationException if the id is null or file type is not correct
     */
    @Secured("ROLE_USER")
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "update Image", security = @SecurityRequirement(name = "apiKey"))
    public void updateImage(@Valid @RequestParam("image") MultipartFile file, @PathVariable Long id)
        throws NotFoundException, ValidationException {
        LOGGER.info("POST /api/v1/image/" + id);
        imageService.updateImage(file, id);
    }


    /**
     * get the specified image.
     *
     * @param id The id of the image to get
     * @return Image Url
     * @throws NotFoundException   Thrown if the image couldn't be found
     * @throws ValidationException if the id is null
     * @throws IOException         if something with the read went wrong
     */
    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    @Operation(summary = "get Image", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<Resource> getImage(@Valid @PathVariable Long id)
        throws NotFoundException, ValidationException, IOException {
        LOGGER.info("GET /api/v1/image/" + id);
        Path imageFilePath = imageService.getImage(id);
        try {
            Resource imageResource = new UrlResource(imageFilePath.toUri());
            if (imageResource.exists() && imageResource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_JPEG);

                return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageResource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete the specified image.
     *
     * @param id The id of the image to delete
     * @throws NotFoundException Thrown if the image couldn't be found
     */
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete the image specified by the id", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable Long id) throws NotFoundException {
        imageService.deleteImage(id);
    }


}
