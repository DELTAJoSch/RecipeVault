package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.FavoriteTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Favorite;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.repository.FavoriteRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class FavoriteEndpointTest implements FavoriteTestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;


    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void getFavorites_whenGivenNoPage_findsAllFavorites() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(FAVORITE_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<RecipeListDto> favoriteDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class));

        assertEquals(3, favoriteDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void getFavorites_whenGivenPage_findsCorrectNumber_ofFavorites() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(FAVORITE_BASE_URI + "?page=0&size=100")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<RecipeListDto> favoriteDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            RecipeListDto[].class));

        assertEquals(3, favoriteDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void count_returnsCorrectNumberOfElements() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(FAVORITE_BASE_URI + "/number")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        long count = objectMapper.readValue(response.getContentAsString(), long.class);

        assertEquals(3, count);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void delete_whenGivenCorrectIdAndAuthentication_deletesFavorite() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(FAVORITE_BASE_URI)
                .param("recipeId", "-1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());

        List<Recipe> favs = favoriteRepository.getFavorites(-42L, PageRequest.of(0, 100));
        assertThat(favs)
            .extracting(Recipe::getId)
            .doesNotContain(-1L);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void delete_whenGivenIncorrectId_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(FAVORITE_BASE_URI)
                .param("recipeId", "-1000")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void create_whenCalledWithNewFavorite_createsFavoriteInDatabase() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(FAVORITE_BASE_URI)
                .param("id", "-3")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        Optional<Favorite> read = favoriteRepository.findByUserIdAndRecipeId(USER.getId(), -3L);
        assertThat(read.isPresent());
    }
    
}
