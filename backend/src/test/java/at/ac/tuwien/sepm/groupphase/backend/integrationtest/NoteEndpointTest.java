package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NoteDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.NoteRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NoteEndpointTest {

    private static final String BASE_URI = "/api/v1";
    private static final String NOTE_BASE_URI = BASE_URI + "/notes";

    private final String ADMIN_DETAILS = "admin@example.com";
    private final String USER_DETAILS = "user@example.com";
    private final String USER2_DETAILS = "user2@example.com";

    private final List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    private final List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void find_findsNote() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(NOTE_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        NoteDto noteDto = objectMapper.readValue(response.getContentAsString(),
                NoteDto.class);

        NoteDto expecNoteDto = new NoteDto(-43L, -1L, "sample note content");
        assertEquals(expecNoteDto, noteDto);
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void delete_deletesNote() throws Exception {
        long noteCount = noteRepository.count();
        MvcResult mvcResult = this.mockMvc.perform(delete(NOTE_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        // check deletion by find
        mvcResult = this.mockMvc.perform(get(NOTE_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        assertEquals(noteCount - 1, noteRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void delete_notExistingNote_NotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(NOTE_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER2_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test

    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void create_createsNoteDatabase() throws Exception {
        long noteCount = noteRepository.count();
        var inserted = new NoteDto(-44L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        String body = objectMapper.writeValueAsString(inserted);

        MvcResult mvcResult = this.mockMvc.perform(post(NOTE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER2_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(noteCount + 1, noteRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void create_otherUser_PermisisonsDenied() throws Exception {
        long noteCount = noteRepository.count();
        var inserted = new NoteDto(-44L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        String body = objectMapper.writeValueAsString(inserted);

        MvcResult mvcResult = this.mockMvc.perform(post(NOTE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals(noteCount, noteRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void create_otherAdminUser_Success() throws Exception {
        long noteCount = noteRepository.count();
        var inserted = new NoteDto(-44L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        String body = objectMapper.writeValueAsString(inserted);

        MvcResult mvcResult = this.mockMvc.perform(post(NOTE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_DETAILS, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(noteCount + 1, noteRepository.count());
    }

    @Test

    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void update_updatesNoteDatabase() throws Exception {
        long noteCount = noteRepository.count();
        var inserted = new NoteDto(-43L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        String body = objectMapper.writeValueAsString(inserted);

        MvcResult mvcResult = this.mockMvc.perform(post(NOTE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(noteCount, noteRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void update_otherUser_PermisisonsDenied() throws Exception {
        long noteCount = noteRepository.count();
        var inserted = new NoteDto(-43L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        String body = objectMapper.writeValueAsString(inserted);

        MvcResult mvcResult = this.mockMvc.perform(post(NOTE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER2_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
        assertEquals(noteCount, noteRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/NoteServiceTest.sql" })
    public void update_otherAdminUser_Success() throws Exception {
        long noteCount = noteRepository.count();
        var inserted = new NoteDto(-43L, -1L, "sdkljfalöskjf vsdkljsaiuböaerkr");

        String body = objectMapper.writeValueAsString(inserted);

        MvcResult mvcResult = this.mockMvc.perform(post(NOTE_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_DETAILS, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(noteCount, noteRepository.count());
    }

  }
