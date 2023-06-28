package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestUser;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.WineMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserSignUpDto;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserEndpointTest implements TestUser {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WineMapper userMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void findAll_whenGivenNoPage_findsCorrectUsers() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<UserInfoDto> userInfoDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
                UserInfoDto[].class));

        assertEquals(3, userInfoDtos.size());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void updateUser_makeSelveAdmin_PermissionsDenied() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto.UserUpdateDtoBuilder()
                .withEmail("user@example.com")
                .withIsAdmin(true)
                .withPassword(null)
                .withId((long) -43).build();
        String body = objectMapper.writeValueAsString(userUpdateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI + "/-43")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals( HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void updateUser_change_ownPassword_Success() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto.UserUpdateDtoBuilder()
                .withEmail(DEFAULT_USER).withIsAdmin(false).withPassword("password123").withId((long) -43).build();
        String body = objectMapper.writeValueAsString(userUpdateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI + "/-43")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void updateUser_changeOtherUser_Fail() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto.UserUpdateDtoBuilder()
                .withEmail(DEFAULT_USER2).withIsAdmin(false).withPassword("password123").withId((long) -44).build();
        String body = objectMapper.writeValueAsString(userUpdateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI + "/-44")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void updateUser_AdminChangeOtherUser_Sucess() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto.UserUpdateDtoBuilder()
                .withEmail(DEFAULT_USER2).withIsAdmin(false).withPassword("password123").withId((long) -44).build();
        String body = objectMapper.writeValueAsString(userUpdateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI + "/-44")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void deleteUser_nonAdmin_Fail() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/-44")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void deleteUser_Admin_Sucess() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/-44")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/UserTest.sql"})
    public void signUpUser_validData() throws Exception {
        UserSignUpDto userSignUpDto = new UserSignUpDto.UserSignUpDtoBuilder()
            .withEmail(DEFAULT_USER2).withPassword("testtest").build();
        String body = objectMapper.writeValueAsString(userSignUpDto);

        MvcResult mvcResult = this.mockMvc.perform(post(BASE_URI + "/signup")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        var user = userRepository.findByEmail(DEFAULT_USER2);
        assertEquals(user.getEmail(), DEFAULT_USER2);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/UserTest.sql"})
    public void signUpUser_EmailAlreadyExists_throwsValidationException() throws Exception {
        UserSignUpDto userSignUpDto = new UserSignUpDto.UserSignUpDtoBuilder()
            .withEmail(DEFAULT_USER).withPassword("testtest").build();
        String body = objectMapper.writeValueAsString(userSignUpDto);

        MvcResult mvcResult = this.mockMvc.perform(post(BASE_URI + "/signup")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/UserTest.sql"})
    public void signUpUser_passwordTooShort_throwsValidationException() throws Exception {
        UserSignUpDto userSignUpDto = new UserSignUpDto.UserSignUpDtoBuilder()
            .withEmail(DEFAULT_USER).withPassword("test").build();
        String body = objectMapper.writeValueAsString(userSignUpDto);

        MvcResult mvcResult = this.mockMvc.perform(post(BASE_URI + "/signup")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void deleteUser_nonAdmin_editsHimself_Success() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(USER_BASE_URI + "/-43")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void findByEmail_FindsCorrectUser() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(USER_BASE_URI).param("email", DEFAULT_USER)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        UserInfoDto userInfoDto = (objectMapper.readValue(response.getContentAsString(),
            UserInfoDto.class));

        assertEquals(-43, userInfoDto.getId());
    }

    @Test
    @Sql(scripts = { "classpath:/sql/UserTest.sql" })
    public void updateOwnUser_withValidUpdateData_Success() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto.UserUpdateDtoBuilder()
            .withEmail(DEFAULT_USER).withIsAdmin(false).withPassword("password123").withId(null).build();
        String body = objectMapper.writeValueAsString(userUpdateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(USER_BASE_URI + "/-43")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }


}
