package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface ImageService {

    Long saveImage(MultipartFile file)
        throws ValidationException;

    void updateImage(MultipartFile file, Long id)
        throws ValidationException, NotFoundException;

    void deleteImage(Long id)
        throws NotFoundException;

    Path getImage(Long id)
        throws NotFoundException, ValidationException;

}