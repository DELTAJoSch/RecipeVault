package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.AuthorTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import at.ac.tuwien.sepm.groupphase.backend.repository.AuthorRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AuthorManagementEndpointTest implements AuthorTestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findAll_findsAllAuthors() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(AUTHOR_MANAGEMENT_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AuthorDetailsDto> authorDetailsDtoList = Arrays.asList(objectMapper.readValue(
                response.getContentAsString(), AuthorDetailsDto[].class));
        assertEquals(3, authorDetailsDtoList.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findById_calledByAdmin_givenIdIsValid_findsCorrectAuthor() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        AuthorDetailsDto authorDetailsDto = objectMapper.readValue(response.getContentAsString(), AuthorDetailsDto.class);

        assertEquals("one", authorDetailsDto.getFirstname());
        assertEquals("author", authorDetailsDto.getLastname());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void findById_calledByAdmin_givenIdIsInvalid_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(AUTHOR_MANAGEMENT_BASE_URI + "/-10")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void create_validCreationData_createsNewAuthor() throws Exception{
        AuthorCreateDto authorCreateDto = new AuthorCreateDto.AuthorCreateDtoBuilder()
                .withFirstname(FIRSTNAME)
                .withLastname(LASTNAME)
                .build();
        String body = objectMapper.writeValueAsString(authorCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        var read = authorRepository.findByFirstnameAndLastname(FIRSTNAME,LASTNAME);

        assertEquals(FIRSTNAME, read.get().getFirstname());
        assertEquals(LASTNAME, read.get().getLastname());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void create_FirstLastnameCombinationAlreadyExists_returnsUnprocessableEntity() throws Exception{
        AuthorCreateDto authorCreateDto = new AuthorCreateDto.AuthorCreateDtoBuilder()
                .withFirstname("one")
                .withLastname("author")
                .build();
        String body = objectMapper.writeValueAsString(authorCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void create_invalidCreationData_FirstnameIsEmpty_throwsBadRequest() throws Exception{
        AuthorCreateDto authorCreateDto = new AuthorCreateDto.AuthorCreateDtoBuilder()
                .withFirstname("test")
                .withLastname("")
                .build();
        String body = objectMapper.writeValueAsString(authorCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void update_validData_updatesAuthorInDatabase() throws Exception{
        AuthorDetailsDto updateDto = new AuthorDetailsDto.AuthorDetailsDtoBuilder()
                .withId(-1L)
                .withFirstname(FIRSTNAME)
                .withLastname(LASTNAME)
                .build();
        String body = objectMapper.writeValueAsString(updateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        var read = authorRepository.findById(-1L);

        assertEquals(-1L, read.get().getId());
        assertEquals(FIRSTNAME, read.get().getFirstname());
        assertEquals(LASTNAME, read.get().getLastname());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/AuthorRepositoryTest.sql"})
    public void update_invalidData_throwsValidationException() throws Exception{
        AuthorDetailsDto updateDto = new AuthorDetailsDto.AuthorDetailsDtoBuilder()
                .withId(-1L)
                .withFirstname(FIRSTNAME)
                .withLastname("     ")
                .build();
        String body = objectMapper.writeValueAsString(updateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/AuthorRepositoryTest.sql" })
    public void createAuthor_whenGivenAuthorWithInvalidFirstname_throwsValidationException() throws Exception {
        AuthorCreateDto createDto = new AuthorCreateDto.AuthorCreateDtoBuilder()
                .withFirstname("          ")
                .build();

        String body = objectMapper.writeValueAsString(createDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

    }

    @Test
    @Sql(scripts = { "classpath:/sql/AuthorRepositoryTest.sql" })
    public void createAuthor_whenGivenAuthorWithInvalidLastname_throwsValidationException() throws Exception {
        AuthorCreateDto createDto = new AuthorCreateDto.AuthorCreateDtoBuilder()
                .withFirstname("H")
                .withLastname("Lorem ipsum dolor sit amet, consetetur " +
                        "sadipscing elitr, sed diam nonumy eirmod tempor " +
                        "invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                        "At vero eos et accusam et justo duo dolores et ea rebum. " +
                        "Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum " +
                        "dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy " +
                        "eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                        "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata s"
                        +
                        "anctus est Lorem ipsum dolor sit amet.")
                .build();

        String body = objectMapper.writeValueAsString(createDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/AuthorRepositoryTest.sql" })
    public void updateAuthor_whenGivenAuthorWithInvalidFirstname_throwsValidationException() throws Exception {
        AuthorDetailsDto update = new AuthorDetailsDto.AuthorDetailsDtoBuilder()
                .withId(-1L)
                .withFirstname("          ")
                .build();

        String body = objectMapper.writeValueAsString(update);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/AuthorRepositoryTest.sql" })
    public void updateAuthor_whenGivenAuthorWithInvalidLastname_throwsValidationException() throws Exception {
        AuthorDetailsDto update = new AuthorDetailsDto.AuthorDetailsDtoBuilder()
                .withId(-1L)
                .withFirstname("test")
                .withLastname("   ")
                .build();

        String body = objectMapper.writeValueAsString(update);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/AuthorRepositoryTest.sql" })
    public void update_similarAuthorAlreadyExists_returnsUnprocessableEntity() throws Exception {
        AuthorDetailsDto updateDto = new AuthorDetailsDto.AuthorDetailsDtoBuilder()
                .withId(-2L)
                .withFirstname("one")
                .withLastname("author")
                .build();
        String body = objectMapper.writeValueAsString(updateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = "classpath:/sql/AuthorRepositoryTest.sql")
    public void delete_whenGivenIdCorrect_calledByAdmin_deletesAuthor() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Author> authors = authorRepository.findAll();

        assertEquals(2, authors.size());

        var search = authorRepository.findById(-1L);
        assertTrue(search.isEmpty());
    }

    @Test
    @Sql(scripts = "classpath:/sql/AuthorRepositoryTest.sql")
    public void delete_whenGivenIdIncorrect_calledByAdmin_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(AUTHOR_MANAGEMENT_BASE_URI + "/-100")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = "classpath:/sql/AuthorRepositoryTest.sql")
    public void delete_whenGivenIdCorrect_notCalledByAdmin_returnsForbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(AUTHOR_MANAGEMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = "classpath:/sql/AuthorRepositoryTest.sql")
    public void delete_whenGivenIdIncorrect_notCalledByAdmin_returnsForbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(AUTHOR_MANAGEMENT_BASE_URI + "/-100")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }
}
