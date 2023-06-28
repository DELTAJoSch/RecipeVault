package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.OcrTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OcrTaskDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.OcrStep;
import at.ac.tuwien.sepm.groupphase.backend.repository.OcrTaskRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OcrEndpointTest implements OcrTestData {
    private static final String BASE_URI = "/api/v1";
    private static final String OCR_BASE_URI = BASE_URI + "/ocr";

    private final String USER_DETAILS = "user@example.com";
    private final List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

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
        MockMultipartFile file = new MockMultipartFile(
            "image",
            "create.jpg",
            "image/jpeg",
            java.nio.file.Files.readAllBytes(Path.of("src/test/resources/images/create.jpg"))
        );

        MvcResult mvcResult = this.mockMvc.perform(multipart(OCR_BASE_URI)
                .file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        var entity = objectMapper.readValue(response.getContentAsString(), OcrTaskDto.class);
        assertEquals(entity.getStep(), OcrStep.PENDING);
        assertNotNull(entity.getId());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void create_WhenCalledWithPng_CreatesTask() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "image",
            "create.png",
            MediaType.IMAGE_PNG_VALUE,
            java.nio.file.Files.readAllBytes(Path.of("src/test/resources/images/create.png"))
        );

        MvcResult mvcResult = this.mockMvc.perform(multipart(OCR_BASE_URI)
                .file(file)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        var entity = objectMapper.readValue(response.getContentAsString(), OcrTaskDto.class);
        assertEquals(entity.getStep(), OcrStep.PENDING);
        assertNotNull(entity.getId());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void create_WhenCalledWithOtherFormat_ThrowsValidationException() throws Exception{
        MockMultipartFile file = new MockMultipartFile(
            "image",
            "hans-otto.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "LALALALA".getBytes()
        );

        MvcResult mvcResult = this.mockMvc.perform(multipart(OCR_BASE_URI)
                .file(file)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void prepare_WhenCalledWithValidId_PreparesFile() throws Exception {
        var content = objectMapper.writeValueAsString(TRANSFORM_DTO);

        MvcResult mvcResult = this.mockMvc.perform(post(OCR_BASE_URI + "/-1")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        var entity = objectMapper.readValue(response.getContentAsString(), OcrTaskDto.class);

        assertEquals(entity.getStep(), OcrStep.PREPARATION);
        assertNotNull(entity.getId());
        assertTrue(java.nio.file.Files.exists(Path.of(PENDING_OUTPUT_PATH)));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void prepare_WhenCalledWithInvalidId_ThrowsNotFoundException() throws Exception {
        var content = objectMapper.writeValueAsString(TRANSFORM_DTO);

        MvcResult mvcResult = this.mockMvc.perform(post(OCR_BASE_URI + "/-150")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void prepare_WhenCalledWithInvalidStep_ThrowsValidationException() throws Exception {
        var content = objectMapper.writeValueAsString(TRANSFORM_DTO);

        MvcResult mvcResult = this.mockMvc.perform(post(OCR_BASE_URI + "/-4")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void ocr_WhenCalledWithInvalidId_ThrowsNotFoundException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(OCR_BASE_URI + "/-150")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void ocr_WhenCalledWithInvalidStep_ThrowsValidationException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(OCR_BASE_URI + "/-4")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void finish_WhenCalledWithInvalidId_ThrowsNotFoundException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(OCR_BASE_URI + "/-150")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void finish_WhenCalledWithInvalidStep_ThrowsValidationException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(OCR_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/OcrTaskTests.sql"})
    public void finish_WhenCalledWithValidId_DeletesFilesAndTask() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(OCR_BASE_URI + "/-4")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        assertFalse(java.nio.file.Files.exists(Path.of(FINISHED_PATH)));
        var entity = ocrTaskRepository.findById(FINISHED);
        assertFalse(entity.isPresent());
    }
}
