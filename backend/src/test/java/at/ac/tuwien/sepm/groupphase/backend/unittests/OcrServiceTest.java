package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.OcrTestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.OcrStep;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.OcrTaskRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OcrService;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OcrServiceTest implements OcrTestData {
    @Autowired
    private OcrService ocrService;
    @Autowired
    private OcrTaskRepository ocrTaskRepository;

    /**
     * Delete all files that are not whitelisted.
     */
    @AfterEach
    public void cleanFiles(){
        try{
            var files = Files.fileNamesIn(BASE_PATH, false);

            for(var file : files){
                if(WHITELIST_FILES.stream().noneMatch(file::contains)){
                    var fileObject = new File(file);

                    if(!fileObject.exists() || !fileObject.isFile()) {
                        System.out.println("Could not delete file " + file);
                        continue;
                    }

                    fileObject.delete();
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Directory probably doesn't exist: " + e);
        }
    }

    /**
     * Copy files to ocr directory for testing purposes
     */
    @BeforeEach
    public void copyTestData() throws Exception{
        java.nio.file.Files.createDirectories(Path.of(BASE_PATH));

        try {
            java.nio.file.Files.copy(
                Path.of("src/test/resources/images/create.jpg"),
                Path.of(BASE_PATH + "pending.jpg")
            );
        } catch (FileAlreadyExistsException ignored) {

        }

        try {
            java.nio.file.Files.copy(
                Path.of("src/test/resources/images/create.jpg"),
                Path.of(BASE_PATH + "preparation.jpg")
            );
        } catch (FileAlreadyExistsException ignored) {

        }

        try {
            java.nio.file.Files.copy(
                Path.of("src/test/resources/images/create.jpg"),
                Path.of(BASE_PATH + "reading.jpg")
            );
        } catch (FileAlreadyExistsException ignored) {

        }

        try {
            java.nio.file.Files.copy(
                Path.of("src/test/resources/images/create.jpg"),
                Path.of(BASE_PATH + "finished.jpg")
            );
        } catch (FileAlreadyExistsException ignored) {

        }
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void create_WhenCalledWithJpg_CreatesTask() throws Exception {
        MultipartFile file = new MockMultipartFile(
            "file",
            "create.jpg",
            MediaType.IMAGE_JPEG_VALUE,
            java.nio.file.Files.readAllBytes(Path.of("src/test/resources/images/create.jpg"))
        );

        var result = this.ocrService.create(file);

        assertNotNull(result);
        assertNotNull(result.getId());

        var entity = ocrTaskRepository.findById(result.getId());

        assertTrue(entity.isPresent());
        assertEquals(entity.get().getStep(), OcrStep.PENDING);
        assertEquals(entity.get().getName(), result.getId()+".jpg");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void create_WhenCalledWithPng_CreatesTask() throws Exception {
        MultipartFile file = new MockMultipartFile(
            "file",
            "create.png",
            MediaType.IMAGE_JPEG_VALUE,
            java.nio.file.Files.readAllBytes(Path.of("src/test/resources/images/create.png"))
        );

        var result = this.ocrService.create(file);

        assertNotNull(result);
        assertNotNull(result.getId());

        var entity = ocrTaskRepository.findById(result.getId());

        assertTrue(entity.isPresent());
        assertEquals(entity.get().getStep(), OcrStep.PENDING);
        assertEquals(entity.get().getName(), result.getId()+".jpg");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void create_WhenCalledWithOtherFormat_ThrowsValidationException() throws Exception{
        MultipartFile file = new MockMultipartFile(
            "file",
            "hans-otto.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "LALALALA".getBytes()
        );

        assertThrows(ValidationException.class, () -> this.ocrService.create(file));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void prepare_WhenCalledWithValidId_PreparesFile() throws Exception {
        var result = this.ocrService.prepare(PENDING, TRANSFORM_DTO);

        assertNotNull(result);
        assertNotNull(result.getId());

        var entity = ocrTaskRepository.findById(result.getId());

        assertTrue(entity.isPresent());
        assertEquals(entity.get().getStep(), OcrStep.PREPARATION);
        assertTrue(java.nio.file.Files.exists(Path.of(PENDING_OUTPUT_PATH)));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void prepare_WhenCalledWithInvalidId_ThrowsNotFoundException() throws Exception {
        assertThrows(NotFoundException.class, () -> this.ocrService.prepare(-150L, TRANSFORM_DTO));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void prepare_WhenCalledWithInvalidStep_ThrowsValidationException() throws Exception {
        assertThrows(ValidationException.class, () -> this.ocrService.prepare(FINISHED, TRANSFORM_DTO));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void ocr_WhenCalledWithInvalidId_ThrowsNotFoundException() throws Exception {
        assertThrows(NotFoundException.class, () -> this.ocrService.ocr(-150L));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void ocr_WhenCalledWithInvalidStep_ThrowsValidationException() throws Exception {
        assertThrows(ValidationException.class, () -> this.ocrService.ocr(FINISHED));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void finish_WhenCalledWithInvalidId_ThrowsNotFoundException() throws Exception {
        assertThrows(NotFoundException.class, () -> this.ocrService.finish(-150L));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void finish_WhenCalledWithInvalidStep_ThrowsValidationException() throws Exception {
        assertThrows(ValidationException.class, () -> this.ocrService.finish(READING));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void finish_WhenCalledWithValidId_DeletesFilesAndTask() throws Exception {
        this.ocrService.finish(FINISHED);

        assertFalse(java.nio.file.Files.exists(Path.of(FINISHED_PATH)));

        var entity = ocrTaskRepository.findById(FINISHED);

        assertFalse(entity.isPresent());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void clean_WhenCalled_DeletesFilesAndTasks() throws Exception {
        this.ocrService.clean();

        assertTrue(Files.fileNamesIn(BASE_PATH, false).isEmpty());

        var entitíes = ocrTaskRepository.findAll();

        assertEquals(entitíes.size(), 0);
    }
}
