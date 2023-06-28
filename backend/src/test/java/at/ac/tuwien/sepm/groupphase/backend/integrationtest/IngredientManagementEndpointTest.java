package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.IngredientTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.IngredientMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class IngredientManagementEndpointTest implements IngredientTestData {
    private final Ingredient INGREDIENT = new Ingredient.IngredientBuilder()
        .setCategory(IngredientMatchingCategory.SHELLFISH)
        .setName(NAME)
        .build();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IngredientMapper ingredientMapper;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void search_withExistingName_findsCorrectIngredients() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(INGREDIENT_MANAGEMENT_BASE_URI + "/Test")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<IngredientCreateDto> ingredientCreateDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            IngredientCreateDto[].class));

        assertEquals(3, ingredientCreateDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void create_calledWithNewIngredient_createsIngredientInDatabase() throws Exception {
        String name = "New Ingredient";

        IngredientCreateDto ingredientCreateDto = new IngredientCreateDto.IngredientCreateDtoBuilder()
            .withCategory(IngredientMatchingCategory.VEGETABLE_GREEN)
            .withName(name)
            .build();
        String body = objectMapper.writeValueAsString(ingredientCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(INGREDIENT_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        var read = ingredientRepository.findByNameContaining(name);

        assertEquals(1, read.size());
        assertEquals(read.get(0).getName(), name);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void create_calledWithIngredientThatAlreadyExists_returnsUnprocessableEntity() throws Exception {
        String name = "TestIngredient";

        IngredientCreateDto ingredientCreateDto = new IngredientCreateDto.IngredientCreateDtoBuilder()
            .withCategory(IngredientMatchingCategory.HERBS_AROMATIC)
            .withName(name)
            .build();
        String body = objectMapper.writeValueAsString(ingredientCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(INGREDIENT_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void create_calledWithIngredientThatViolatesNaming_throwsValidationException() throws Exception {
        String name = "        ";

        IngredientCreateDto ingredientCreateDto = new IngredientCreateDto.IngredientCreateDtoBuilder()
            .withCategory(IngredientMatchingCategory.CHEESE_NUTTY)
            .withName(name)
            .build();
        String body = objectMapper.writeValueAsString(ingredientCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(INGREDIENT_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void update_calledWithUpdatedIngredient_updatesIngredientInDatabase() throws Exception {
        String name = "newIng";

        IngredientDetailsDto ingredientDetailsDto = new IngredientDetailsDto.IngredientDetailsDtoBuilder()
            .withCategory(IngredientMatchingCategory.HERBS_AROMATIC)
            .withName(name)
            .withId(-1L)
            .build();
        String body = objectMapper.writeValueAsString(ingredientDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(INGREDIENT_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        var read = ingredientRepository.findByNameContaining(name);

        assertEquals(1, read.size());
        assertEquals(read.get(0).getName(), name);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void update_calledWithIngredientThatAlreadyExists_returnsUnprocessableEntity() throws Exception {
        String name = "TestIng";

        IngredientDetailsDto ingredientDetailsDto = new IngredientDetailsDto.IngredientDetailsDtoBuilder()
            .withCategory(IngredientMatchingCategory.FISH)
            .withName(name)
            .withId(-1L)
            .build();
        String body = objectMapper.writeValueAsString(ingredientDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(INGREDIENT_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void update_calledWithIngredientThatViolatesNaming_returnsBadRequest() throws Exception {
        IngredientDetailsDto ingredientDetailsDto = new IngredientDetailsDto.IngredientDetailsDtoBuilder()
            .withCategory(IngredientMatchingCategory.PEPPERS_CHILLIS)
            .withName("  ")
            .withId(-1L)
            .build();

        String body = objectMapper.writeValueAsString(ingredientDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(INGREDIENT_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void update_calledWithIngredientThatDoesNotExist_throwsNotFoundException() throws Exception {
        IngredientDetailsDto ingredientDetailsDto = new IngredientDetailsDto.IngredientDetailsDtoBuilder()
            .withCategory(IngredientMatchingCategory.CHEESE_NUTTY)
            .withName(":)")
            .withId(-1000L)
            .build();

        String body = objectMapper.writeValueAsString(ingredientDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(INGREDIENT_MANAGEMENT_BASE_URI + "/-1000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void delete_givenCorrectIdAndAdmin_deletesIngredient() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(INGREDIENT_MANAGEMENT_BASE_URI + "/-4")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Ingredient> ingredients = ingredientRepository.findAll();

        assertEquals(3, ingredients.size());
        assertEquals(0, ingredients.stream().filter(ing -> ing.getName().equals("TestIng")).toList().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void delete_givenIncorrectId_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(INGREDIENT_MANAGEMENT_BASE_URI + "/-1000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void delete_givenCorrectIdAndWrongUser_returnsForbidden() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(INGREDIENT_MANAGEMENT_BASE_URI + "/-3")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findBy_whenCalledWithNoParameters_ReturnsAllIngredients() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(INGREDIENT_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("4", response.getHeaderValue("X-Total-Count"));

        List<IngredientDetailsDto> ingredientDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            IngredientDetailsDto[].class));

        assertEquals(4, ingredientDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findBy_whenCalledWithName_ReturnsCorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(INGREDIENT_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "st ing")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("1", response.getHeaderValue("X-Total-Count"));

        List<IngredientDetailsDto> ingredientDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            IngredientDetailsDto[].class));

        assertEquals(1, ingredientDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findBy_whenCalledWithCategory_ReturnsCorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(INGREDIENT_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", "SHELLFISH")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("1", response.getHeaderValue("X-Total-Count"));

        List<IngredientDetailsDto> ingredientDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            IngredientDetailsDto[].class));

        assertEquals(1, ingredientDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findBy_whenCalledWithInvalidParameter_ReturnsNoIngredients() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(INGREDIENT_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "NONEXISTENT")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("0", response.getHeaderValue("X-Total-Count"));

        List<IngredientDetailsDto> ingredientDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            IngredientDetailsDto[].class));

        assertEquals(0, ingredientDetailsDtos.size());
    }
}