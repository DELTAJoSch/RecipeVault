package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ImageTransformDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OcrRecipeDto;
import at.ac.tuwien.sepm.groupphase.backend.engines.ImagePreparationEngine;
import at.ac.tuwien.sepm.groupphase.backend.engines.OcrEngine;
import at.ac.tuwien.sepm.groupphase.backend.entity.OcrStep;
import at.ac.tuwien.sepm.groupphase.backend.entity.OcrTask;
import at.ac.tuwien.sepm.groupphase.backend.exception.InternalServerException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OcrTaskRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OcrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Default OcrService.
 */
@Service
public class DefaultOcrService implements OcrService {
    private static final String OCR_ROOT_PATH = "ocr/";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private OcrTaskRepository ocrTaskRepository;

    @Autowired
    private OcrEngine ocrEngine;

    @Autowired
    private ImagePreparationEngine imagePreparationEngine;

    // For anyone writing tests: Multipart Files can be "created" using
    // MockMultipartFiles
    @Override
    public OcrTask create(MultipartFile file) throws ValidationException {
        LOGGER.trace("create({})", file.getOriginalFilename());
        var ocrTask = new OcrTask.OcrTaskBuilder()
                .setStatus(false)
                .setStep(OcrStep.PENDING)
                .setCreationDate(ZonedDateTime.now(ZoneId.of("UTC")))
                .build();

        ocrTask = ocrTaskRepository.save(ocrTask);

        var type = file.getContentType();
        if (type == null) {
            throw new InternalServerException("Dateityp ist null");
        }

        String extension = switch (type) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            default -> throw new ValidationException(Collections.singletonList("Bildtyp ist nicht jpg oder png"));
        };

        var name = ocrTask.getId() + extension;
        ocrTask.setName(name);
        ocrTask = ocrTaskRepository.saveAndFlush(ocrTask);

        // save image to disk
        try {
            Files.createDirectories(Path.of(OCR_ROOT_PATH));
            var path = Paths.get(OCR_ROOT_PATH + name);
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new InternalServerException("Das Bild konnte nicht geschrieben werden", e);
        }

        return ocrTask;
    }

    @Override
    public OcrTask prepare(Long id, ImageTransformDto transform) throws NotFoundException, ValidationException {
        LOGGER.trace("prepare({}, {})", id, transform);
        var optional = ocrTaskRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException("OCR-Aufgabe mit der ID " + id + " wurde nicht gefunden!");
        }

        var task = optional.get();
        if (task.getStep() != OcrStep.PENDING) {
            throw new ValidationException(Collections.singletonList("Schritt ist falsch!"));
        }

        imagePreparationEngine.prepare(OCR_ROOT_PATH + task.getName(), transform);

        task.setStep(OcrStep.PREPARATION);
        task = ocrTaskRepository.saveAndFlush(task);

        return task;
    }

    @Override
    public OcrRecipeDto ocr(Long id) throws NotFoundException, ValidationException {
        LOGGER.trace("ocr({})", id);
        var optional = ocrTaskRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException("OCR-Aufgabe mit der ID " + id + " wurde nicht gefunden!");
        }

        var task = optional.get();
        if (task.getStep() != OcrStep.PREPARATION) {
            throw new ValidationException(Collections.singletonList("Schritt ist falsch!"));
        }

        String imageFileName = OCR_ROOT_PATH + task.getName();

        LOGGER.info("start ocr" + imageFileName);

        File imageFile = new File(imageFileName);
        String recipeText = ocrEngine.ocrFile(imageFile);
        LOGGER.info("ocr-finished");
        List<AmountDto> ingredientList = ocrEngine.extractIngredientList(recipeText);


        LOGGER.info("extract ingredients finished");

        var result = new OcrRecipeDto.OcrRecipeDtoBuilder()
                .setDescription(recipeText)
                .setIngredients(ingredientList)
                .build();
        task.setStep(OcrStep.FINISHED);
        task = ocrTaskRepository.saveAndFlush(task);

        return result;
    }

    @Override
    public void finish(Long id) throws NotFoundException, ValidationException {
        LOGGER.trace("finish({})", id);
        var optional = ocrTaskRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundException("OCR-Aufgabe mit der ID " + id + " wurde nicht gefunden!");
        }

        var task = optional.get();
        if (task.getStep() != OcrStep.FINISHED) {
            throw new ValidationException(Collections.singletonList("Schritt ist falsch!"));
        }

        String original = OCR_ROOT_PATH + task.getName();
        String transformed = OCR_ROOT_PATH + getTransformedName(task.getName());

        try {
            Path origPath = Path.of(original);
            if (Files.exists(origPath)) {
                Files.delete(origPath);
            }

            Path transPath = Path.of(transformed);
            if (Files.exists(transPath)) {
                Files.delete(transPath);
            }
        } catch (IOException e) {
            throw new InternalServerException("Die Dateien konnten nicht gelöscht werden", e);
        }

        ocrTaskRepository.delete(task);
    }

    @Override
    public void clean() {
        LOGGER.trace("clean()");
        var date = ZonedDateTime.now(ZoneId.of("UTC")).minusHours(24);
        var tooOld = ocrTaskRepository.findByCreationDateLessThanEqual(date);

        for (var task : tooOld) {
            String original = OCR_ROOT_PATH + task.getName();
            String transformed = OCR_ROOT_PATH + getTransformedName(task.getName());

            try {
                Path origPath = Path.of(original);
                if (Files.exists(origPath)) {
                    Files.delete(origPath);
                }

                Path transPath = Path.of(transformed);
                if (Files.exists(transPath)) {
                    Files.delete(transPath);
                }
            } catch (IOException e) {
                LOGGER.warn("Could not delete files for task: "
                        + task.getId()
                        + ", file: "
                        + task.getName()
                        + ". Error: "
                        + Arrays.toString(e.getStackTrace()));
            }

            ocrTaskRepository.delete(task);
        }
    }

    /**
     * Returns the name of the transformed file.
     *
     * @param name The name of the original
     * @return Returns the transformed name
     */
    private String getTransformedName(String name) {
        LOGGER.trace("getTransformedName({})", name);
        var nameTokens = name.split("\\.");
        var namePos = nameTokens.length - 2;

        if (namePos >= 0) {
            return nameTokens[namePos] + "-transformed.png";
        }

        throw new InternalServerException("Der Name der transformierten Datei konnte nicht generiert werden");
    }
}
