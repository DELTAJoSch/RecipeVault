package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.ListTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ListOverviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import at.ac.tuwien.sepm.groupphase.backend.repository.ListRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ListEndpointTest implements ListTestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListRepository listRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getLists_findsAllListsOfUser() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(LIST_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ListOverviewDto> listOverviewDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ListOverviewDto[].class));

        assertEquals(2, listOverviewDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getRecipes_whenGivenSmallPage_findsCorrectNumber_ofRecipes() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(LIST_BASE_URI + "/my wonderful list 2?page=0&size=1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<RecipeListDto> recipeListDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class));

        assertEquals(1, recipeListDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getRecipes_whenGivenBigPage_findsCorrectNumber_ofRecipes() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(LIST_BASE_URI + "/my wonderful list 2?page=0&size=10")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<RecipeListDto> recipeListDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class));

        assertEquals(2, recipeListDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getRecipes_whenGivenNoPage_returnsAllRecipes_ofList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(LIST_BASE_URI + "/my wonderful list 2")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<RecipeListDto> recipeListDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class));

        assertEquals(2, recipeListDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void delete_whenGivenCorrectIdAndAuthentication_deletesRecipeFromList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(LIST_BASE_URI)
                .param("recipeId", "-1")
                .param("name", "list")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        List<Recipe> favs = listRepository.getRecipesByNameAndUserId("list", -42L, PageRequest.of(0, 100));
        assertThat(favs)
            .extracting(Recipe::getId)
            .doesNotContain(-1L);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void delete_withoutAuthentication_returnsUnauthorized() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(LIST_BASE_URI)
                .param("recipeId", "-100")
                .param("name", "list")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("", new ArrayList<>())))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void count_withValidListName_returnsCorrectCount() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(LIST_BASE_URI + "/number")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "list")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        long count = objectMapper.readValue(response.getContentAsString(),
            long.class);
        assertThat(count).isEqualTo(2);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void createList_withValidName_createsList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "list 1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertThat(listRepository.getAllByUserId(USER.getId())).extracting(RecipeList::getName).contains("list 1");
    }


    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void createList_withAlreadyExistingName_returnsUnprocessable() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "list")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void createList_withEmptyName_returnsConflict() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void deleteList_givenValidList_deletesList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "list")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        assertThat(listRepository.getAllByUserId(-42L)).extracting(RecipeList::getName).doesNotContain("list");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void deleteList_givenNonExistentList_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "liiiiiiiiist")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void addRecipeToList_givenValidRecipe_addsRecipe() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(LIST_BASE_URI + "/list")
                .contentType(MediaType.APPLICATION_JSON)
                .param("recipeId", "-2")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertThat(listRepository.getRecipesByNameAndUserId("list", -42L)).extracting(Recipe::getId).contains(-2L);

    }


    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void addRecipeToList_givenAlreadyAddedRecipe_returnsUnprocessable() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(LIST_BASE_URI + "/list")
                .contentType(MediaType.APPLICATION_JSON)
                .param("recipeId", "-4")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());

    }
}
