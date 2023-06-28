package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.ApplicationTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationOptionMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.ApplicationOptionRepository;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ApplicationOptionEndpointTest implements ApplicationTestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationOptionRepository applicationOptionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationOptionMapper applicationOptionMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenLargePageAndNoName_findsAll() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(APPLICATION_OPTION_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("4", response.getHeaderValue("X-Total-Count"));

        List<ApplicationOptionDto> applicationOptionDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ApplicationOptionDto[].class));

        assertEquals(4, applicationOptionDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenSmallPageAndNoName_findsCorrectNumber() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(APPLICATION_OPTION_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .param("size", "2"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("4", response.getHeaderValue("X-Total-Count"));

        List<ApplicationOptionDto> applicationOptionDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ApplicationOptionDto[].class));

        assertEquals(2, applicationOptionDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenName_findsAllMatching() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(APPLICATION_OPTION_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .param("size", "10")
                .param("page", "0")
                .param("name", "one"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("1", response.getHeaderValue("X-Total-Count"));

        List<ApplicationOptionDto> applicationOptionDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ApplicationOptionDto[].class));

        assertEquals(1, applicationOptionDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenInvalidName_findsNone() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(APPLICATION_OPTION_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .param("size", "10")
                .param("page", "0")
                .param("name", "HEINZ-WILHELM - DIE OPTION"))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
        assertEquals("0", response.getHeaderValue("X-Total-Count"));

        List<ApplicationOptionDto> applicationOptionDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ApplicationOptionDto[].class));

        assertEquals(0, applicationOptionDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void findLike_whenGivenTooLongName_throwsValidationException() throws Exception {
        String fail = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        MvcResult mvcResult = this.mockMvc.perform(get(APPLICATION_OPTION_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .param("size", "10")
                .param("page", "0")
                .param("name", fail))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidId_throwsNotFoundException() throws Exception {
        ApplicationOptionDto applicationOptionDto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withValue("TRUE")
            .build();
        String body = objectMapper.writeValueAsString(applicationOptionDto);

        MvcResult mvcResult = this.mockMvc.perform(post(APPLICATION_OPTION_BASE_URI + "/-10000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenValid_UpdatesOption() throws Exception {
        ApplicationOptionDto applicationOptionDto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withValue("FALSE")
            .build();
        String body = objectMapper.writeValueAsString(applicationOptionDto);

        MvcResult mvcResult = this.mockMvc.perform(post(APPLICATION_OPTION_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidBoolean_throwsValidationException() throws Exception {
        ApplicationOptionDto applicationOptionDto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withValue("LELELELELELE")
            .build();
        String body = objectMapper.writeValueAsString(applicationOptionDto);

        MvcResult mvcResult = this.mockMvc.perform(post(APPLICATION_OPTION_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidLongRange_throwsValidationException() throws Exception {
        ApplicationOptionDto applicationOptionDto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withValue("65536")
            .build();
        String body = objectMapper.writeValueAsString(applicationOptionDto);

        MvcResult mvcResult = this.mockMvc.perform(post(APPLICATION_OPTION_BASE_URI + "/-2")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidShortRange_throwsValidationException() throws Exception {
        ApplicationOptionDto applicationOptionDto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withValue("280")
            .build();
        String body = objectMapper.writeValueAsString(applicationOptionDto);

        MvcResult mvcResult = this.mockMvc.perform(post(APPLICATION_OPTION_BASE_URI + "/-3")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ApplicationOptionTests.sql"})
    public void update_WhenGivenInvalidStringRange_throwsValidationException() throws Exception {
        String fail = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
            + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        ApplicationOptionDto applicationOptionDto = new ApplicationOptionDto.ApplicationOptionBuilder()
            .withValue(fail)
            .build();
        String body = objectMapper.writeValueAsString(applicationOptionDto);

        MvcResult mvcResult = this.mockMvc.perform(post(APPLICATION_OPTION_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }
}
