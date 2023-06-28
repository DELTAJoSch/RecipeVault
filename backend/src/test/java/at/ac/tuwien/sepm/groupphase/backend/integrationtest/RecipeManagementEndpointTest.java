package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.RecipeTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class RecipeManagementEndpointTest implements RecipeTestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    private UserInfoDto ADMIN_USER_INFO = new UserInfoDto.UserInfoDtoBuilder()
        .withId(-42L)
        .withName("test@12122392.xyz")
        .build();
    


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findAll_whenGivenNoPage_findsCorrectRecipe() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_MANAGEMENT_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<RecipeDetailsDto> recipeDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            RecipeDetailsDto[].class));

        assertEquals(5, recipeDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findById_whenGivenValidId_findsCorrectRecipe() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_MANAGEMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        RecipeDetailsDto recipeDetailsDtos = objectMapper.readValue(response.getContentAsString(),
            RecipeDetailsDto.class);

        assertEquals("test recipe one", recipeDetailsDtos.getName());

    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findById_whenGivenInvalidId_throwsNotFoundException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(RECIPE_MANAGEMENT_BASE_URI + "/-1000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void delete_whenGivenCorrectIdAndOwner_deletesRecipe() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(RECIPE_MANAGEMENT_BASE_URI + "/-5")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Recipe> recipes = recipeRepository.findAll();

        assertEquals(4, recipes.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void delete_whenGivenCorrectIdAndAdmin_deletesRecipe() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(RECIPE_MANAGEMENT_BASE_URI + "/-5")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Recipe> recipes = recipeRepository.findAll();

        assertEquals(4, recipes.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void delete_whenGivenIncorrectId_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(RECIPE_MANAGEMENT_BASE_URI + "/-1000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void delete_whenGivenCorrectIdAndWrongUser_returnsNotAllowed() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(RECIPE_MANAGEMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void createRecipe_whenGivenRecipeWithInvalidName_throwsValidationException() throws Exception {
        RecipeCreateDto createDto = new RecipeCreateDto.RecipeCreateDtoBuilder()
            .setDifficulty(Difficulty.EASY)
            .setName("          ")
            .setDescription(DESCRIPTION)
            .setIngredients(AMOUNT_DTOS)
            .build();

        String body = objectMapper.writeValueAsString(createDto);

        MvcResult mvcResult = this.mockMvc.perform(post(RECIPE_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void updateRecipe_whenGivenInvalidName_throwsValidationException() throws Exception {
        RecipeDetailsDto updateDto = new RecipeDetailsDto.RecipeDetailsDtoBuilder()
            .withDifficulty(Difficulty.EASY)
            .withName("          ")
            .withDescription(DESCRIPTION)
            .withOwner(ADMIN_USER_INFO)
            .withId(-1L)
            .withIngredients(AMOUNT_DTOS)
            .build();
            String body = objectMapper.writeValueAsString(updateDto);

            MvcResult mvcResult = this.mockMvc.perform(post(RECIPE_MANAGEMENT_BASE_URI + "/-1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                    .andDo(print())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void updateRecipe_whenGivenInvalidDescription_throwsValidationException() throws Exception {
        RecipeDetailsDto updateDto = new RecipeDetailsDto.RecipeDetailsDtoBuilder()
            .withDifficulty(Difficulty.EASY)
            .withName(NAME)
            .withDescription("          ")
            .withOwner(ADMIN_USER_INFO)
            .withId(-1L)
            .withIngredients(AMOUNT_DTOS)
            .build();

            String body = objectMapper.writeValueAsString(updateDto);

            MvcResult mvcResult = this.mockMvc.perform(post(RECIPE_MANAGEMENT_BASE_URI + "/-1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
                    .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                    .andDo(print())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
