package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.CommentRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CommentEndpointTest {

    private static final String BASE_URI = "/api/v1";
    private static final String COMMENT_BASE_URI = BASE_URI + "/comments";

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
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void find_withExistingRecipe_findsCorrectComments() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(COMMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<CommentDto> commentDto = objectMapper.readValue(response.getContentAsString(),
                new TypeReference<List<CommentDto>>() {
                });

        CommentDto expected = new CommentDto(-43L, -1L, LocalDateTime.of(2023, 5, 10, 15, 20, 3),
                "sample comment content");
        assertEquals(expected, commentDto.get(0));
    }

    @Test
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void find_withNonexistentRecipe_throwsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(COMMENT_BASE_URI + "/-15")
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER2_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void delete_withValidCall_deletesComment() throws Exception {
        long commentCount = commentRepository.count();
        var deleted = new CommentDto(-43L, -4L, LocalDateTime.of(2023, 5, 10, 16, 17, 9), "test comment");
        String body = objectMapper.writeValueAsString(deleted);

        MvcResult mvcResult = this.mockMvc.perform(delete(COMMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_DETAILS, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        // check deletion by find
        mvcResult = this.mockMvc.perform(get(COMMENT_BASE_URI + "/-4")
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_DETAILS, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response2 = mvcResult.getResponse();
        assertEquals(response2.getContentLength(), 0);
        assertEquals(commentCount - 1, commentRepository.count());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void delete_withNonexistentComment_throwsNotFound() throws Exception {
        long commentCount = commentRepository.count();
        var deleted = new CommentDto(-43L, -2L, LocalDateTime.of(2023, 5, 10, 16, 17, 9), "test comment");
        String body = objectMapper.writeValueAsString(deleted);

        MvcResult mvcResult = this.mockMvc.perform(delete(COMMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_DETAILS, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());

        assertEquals(commentCount, commentRepository.count());
    }

    @Test

    @Sql(scripts = { "classpath:/sql/CommentServiceTest.sql" })
    public void create_withValidComment_createsComment() throws Exception {
        long commentCount = commentRepository.count();
        var inserted = new CommentDto(-44L, -1L, LocalDateTime.of(2023, 5, 18, 15, 20, 3), "test comment");

        String body = objectMapper.writeValueAsString(inserted);

        MvcResult mvcResult = this.mockMvc.perform(post(COMMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(USER2_DETAILS, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(commentCount + 1, commentRepository.count());
    }
}
