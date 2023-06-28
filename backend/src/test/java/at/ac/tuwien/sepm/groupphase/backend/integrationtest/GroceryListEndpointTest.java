package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GroceryListTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AmountDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.groups.Tuple;
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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class GroceryListEndpointTest implements GroceryListTestData {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_returnsCorrectAmounts() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(GROCERY_LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[[-1, 2], [-4, 1]]")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AmountDto> amountDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            AmountDto[].class));
        assertThat(amountDtos).size().isEqualTo(5);
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-1L, 200.0, AmountUnit.KG));
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-2L, 320.0, AmountUnit.G));
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-3L, 200.0, AmountUnit.PIECE));
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-3L, 10.0, AmountUnit.KG));
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-4L, 50.0, AmountUnit.CUP));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_oneRecipeHasZeroPortions_returnsCorrectAmounts() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(GROCERY_LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[[-1, 0], [-4, 1]]")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AmountDto> amountDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            AmountDto[].class));
        assertThat(amountDtos).size().isEqualTo(3);
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-2L, 120.0, AmountUnit.G));
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-3L, 10.0, AmountUnit.KG));
        assertThat(amountDtos).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-4L, 50.0, AmountUnit.CUP));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_withZeroPortions_returnsEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(GROCERY_LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[[-1, 0], [-4, 0]]")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<AmountDto> amountDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            AmountDto[].class));
        assertThat(amountDtos).size().isEqualTo(0);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_withOutAuthentication_returnsUnauthorized() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(GROCERY_LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[[-1, 0], [-4, 0]]")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken("", new ArrayList<>())))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_withNonExistentRecipe_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post(GROCERY_LIST_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[[-100, 0], [-4, 0]]")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}
