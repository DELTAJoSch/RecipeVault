package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

@Service
public class DefaultImageService implements ImageService {
    private static final String IMAGE_ROOT_PATH = "images/";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    ImageRepository imageRepository;


    @Override
    public Long saveImage(MultipartFile file) throws ValidationException {
        long maxSizeInBytes = 20 * 1024 * 1024; // 20MB

        if (file.getSize() > maxSizeInBytes) {
            // File size exceeds the allowed limit
            throw new ValidationException(Collections.singletonList("File size exceeds 20MB"));
        }


        Image image = new Image();

        var type = file.getContentType();
        if (type == null) {
            throw new InternalServerException("File type is null");
        }

        String extension = switch (type) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new ValidationException(Collections.singletonList("Image type is not jpg or png"));
        };

        imageRepository.save(image);

        String name = image.getId() + extension;
        image.setName(name);

        // save image to disk
        try {
            Files.createDirectories(Path.of(IMAGE_ROOT_PATH));
            var path = Paths.get(IMAGE_ROOT_PATH + name);
            Files.write(path, file.getBytes());
            imageRepository.saveAndFlush(image);
        } catch (IOException e) {
            throw new InternalServerException("Could not write image", e);
        }
        return image.getId();
    }

    @Override
    public void updateImage(MultipartFile file, Long id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("Image id is null");
        }

        long maxSizeInBytes = 20 * 1024 * 1024; // 20MB

        if (file.getSize() > maxSizeInBytes) {
            // File size exceeds the allowed limit
            throw new ValidationException(Collections.singletonList("File size exceeds 20MB"));
        }


        var optionalImage = imageRepository.findById(id);


        if (!optionalImage.isEmpty()) {

            Image image = optionalImage.get();

            deleteOnDisc(image.getName());

            var type = file.getContentType();
            if (type == null) {
                throw new InternalServerException("File type is null");
            }

            String extension = switch (type) {
                case "image/jpeg" -> ".jpg";
                case "image/png" -> ".png";
                default -> throw new ValidationException(Collections.singletonList("Image type is not jpg or png"));
            };

            var name = image.getId() + extension;
            image.setName(name);

            // save image to disk
            try {
                Files.createDirectories(Path.of(IMAGE_ROOT_PATH));
                var path = Paths.get(IMAGE_ROOT_PATH + name);
                Files.write(path, file.getBytes());
                image = imageRepository.saveAndFlush(image);
            } catch (IOException e) {
                throw new InternalServerException("Could not write image", e);
            }
        } else {
            throw new NotFoundException("Image couldn't be found");
        }
    }

    @Override
    public void deleteImage(Long id) throws NotFoundException {
        var image = imageRepository.findById(id);

        deleteOnDisc(image.get().getName());

        imageRepository.delete(image.get());
    }

    @Override
    public Path getImage(Long id) throws NotFoundException, ValidationException {


        if (id == null) {
            throw new ValidationException("Image id is null");
        }
        var image = imageRepository.findById(id);

        if (!image.isEmpty()) {

            String filePath = IMAGE_ROOT_PATH + image.get().getName();

            return Paths.get(filePath);
        } else {
            throw new NotFoundException();
        }
    }


    private void deleteOnDisc(String name) {

        if (!name.contains("datageneratorImages/")) {
            String filepath = IMAGE_ROOT_PATH + name;

            try {
                Path origPath = Path.of(filepath);
                if (Files.exists(origPath)) {
                    Files.delete(origPath);
                }

            } catch (IOException e) {
                LOGGER.warn("Could not delete image: "
                    + name
                    + " Error: "
                    + Arrays.toString(e.getStackTrace()));
            }
        }
    }
}