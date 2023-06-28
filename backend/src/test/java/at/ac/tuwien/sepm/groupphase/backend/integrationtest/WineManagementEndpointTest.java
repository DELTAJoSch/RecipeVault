package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.WineTestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.WineMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.WineRepository;
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
public class WineManagementEndpointTest implements WineTestData {
    private final Wine WINE = new Wine.WineBuilder()
        .setCategory(WineCategory.DESSERT)
        .setName(NAME)
        .setVinyard(VINYARD)
        .setTemperature(TEMP)
        .setOwner(USER)
        .build();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WineRepository wineRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WineMapper wineMapper;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findAll_whenGivenNoPage_findsAllWines() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(5, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findAll_whenGivenPage_findsCorrectWine() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(5, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findById_whenGivenValidId_findsCorrectWine() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        WineDetailsDto wineDetailsDtos = objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto.class);

        assertEquals("test wine one", wineDetailsDtos.getName());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findById_whenGivenInvalidId_throwsNotFoundException() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/-1000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void delete_whenGivenCorrectIdAndOwner_deletesWine() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(WINE_MANAGEMENT_BASE_URI + "/-5")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Wine> wines = wineRepository.findAll();

        assertEquals(4, wines.size());
        assertEquals(0, wines.stream().filter(wine -> wine.getName().equals("admin deleteable wine")).toList().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void delete_whenGivenCorrectIdAndAdmin_deletesWine() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(WINE_MANAGEMENT_BASE_URI + "/-5")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Wine> wines = wineRepository.findAll();

        assertEquals(4, wines.size());
        assertEquals(0, wines.stream().filter(wine -> wine.getName().equals("admin deleteable wine")).toList().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void delete_whenGivenIncorrectId_returnsNotFound() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(WINE_MANAGEMENT_BASE_URI + "/-1000")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void delete_whenGivenCorrectIdAndWrongUser_returnsNotAllowed() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(delete(WINE_MANAGEMENT_BASE_URI + "/-1")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(DEFAULT_USER, DEFAULT_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void create_whenCalledWithNewWine_createsWineInDatabase() throws Exception {
        String name = "NewWine";

        WineCreateDto wineCreateDto = new WineCreateDto.WineCreateDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName(name)
            .withTemperature(42.00)
            .withVinyard(VINYARD)
            .build();
        String body = objectMapper.writeValueAsString(wineCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(WINE_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        var read = wineRepository.findByNameContaining(name);

        assertEquals(1, read.size());
        assertEquals(read.get(0).getName(), name);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void create_whenCalledWithWineThatAlreadyExists_ReturnsUnprocessableEntity() throws Exception {
        String name = "test wine two";

        WineCreateDto wineCreateDto = new WineCreateDto.WineCreateDtoBuilder()
            .withCategory(WineCategory.SPARKLING)
            .withName(name)
            .withTemperature(10.00)
            .withVinyard("test vinyard one")
            .build();
        String body = objectMapper.writeValueAsString(wineCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(WINE_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void create_whenCalledWithWineThatViolatesNaming_throwsValidationException() throws Exception {
        String name = "        ";

        WineCreateDto wineCreateDto = new WineCreateDto.WineCreateDtoBuilder()
            .withCategory(WineCategory.SPARKLING)
            .withName(name)
            .withTemperature(10.00)
            .withVinyard("test vinyard one")
            .build();
        String body = objectMapper.writeValueAsString(wineCreateDto);

        MvcResult mvcResult = this.mockMvc.perform(post(WINE_MANAGEMENT_BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void update_whenCalledWithUpdatedWine_updatesWineInDatabase() throws Exception {
        String name = "NewWine";

        WineDetailsDto wineDetailsDto = new WineDetailsDto.WineDetailsDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName(name)
            .withTemperature(42.00)
            .withVinyard(VINYARD)
            .withId(-1L)
            .withOwner(
                new UserInfoDto.UserInfoDtoBuilder()
                    .withId(-42L)
                    .withName("test@12122392.xyz")
                    .build()
            )
            .build();
        String body = objectMapper.writeValueAsString(wineDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(WINE_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        var read = wineRepository.findByNameContaining(name);

        assertEquals(1, read.size());
        assertEquals(read.get(0).getName(), name);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void update_whenCalledWithWineThatAlreadyExists_ReturnsUnprocessableEntity() throws Exception {
        String name = "test wine two";

        WineDetailsDto wineDetailsDto = new WineDetailsDto.WineDetailsDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName(name)
            .withTemperature(10.00)
            .withVinyard("test vinyard one")
            .withId(-1L)
            .withOwner(
                new UserInfoDto.UserInfoDtoBuilder()
                    .withId(-42L)
                    .withName("test@12122392.xyz")
                    .build()
            )
            .build();
        String body = objectMapper.writeValueAsString(wineDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(WINE_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void update_whenCalledWithWineThatViolatesNaming_throwsValidationException() throws Exception {
        WineDetailsDto wineDetailsDto = new WineDetailsDto.WineDetailsDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName("  ")
            .withTemperature(10.00)
            .withVinyard("test vinyard one")
            .withId(-1L)
            .withOwner(
                new UserInfoDto.UserInfoDtoBuilder()
                    .withId(-42L)
                    .withName("test@12122392.xyz")
                    .build()
            )
            .build();

        String body = objectMapper.writeValueAsString(wineDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(WINE_MANAGEMENT_BASE_URI + "/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void update_whenCalledWithWineThatDoesNotExist_throwsNotFoundException() throws Exception {
        WineDetailsDto wineDetailsDto = new WineDetailsDto.WineDetailsDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName("aasasas")
            .withTemperature(10.00)
            .withVinyard("test vinyard one")
            .withId(-1000L)
            .withOwner(
                new UserInfoDto.UserInfoDtoBuilder()
                    .withId(-42L)
                    .withName("test@12122392.xyz")
                    .build()
            )
            .build();

        String body = objectMapper.writeValueAsString(wineDetailsDto);

        MvcResult mvcResult = this.mockMvc.perform(post(WINE_MANAGEMENT_BASE_URI + "/-1000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findBy_whenCalledWithNoParameters_ReturnsAllWines() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("5", response.getHeaderValue("X-Total-Count"));

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(5, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findBy_whenCalledWithName_ReturnsCorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "st wine")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("4", response.getHeaderValue("X-Total-Count"));

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(4, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findBy_whenCalledWithVinyard_ReturnsCorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("vinyard", "vinyard o")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("3", response.getHeaderValue("X-Total-Count"));

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(3, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findBy_whenCalledWithCountry_ReturnsCorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("country", "AT")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("0", response.getHeaderValue("X-Total-Count"));

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(0, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findBy_whenCalledWithCategory_ReturnsCorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("category", "SPARKLING")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("3", response.getHeaderValue("X-Total-Count"));

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(3, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findBy_whenCalledWithComplexParameters_ReturnsCorrect() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "tw")
                .param("vinyard", "one")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("1", response.getHeaderValue("X-Total-Count"));

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(1, wineDetailsDtos.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findBy_whenCalledWithInvalidParameter_ReturnsNoWines() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(WINE_MANAGEMENT_BASE_URI + "/search")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "OTTOKAR")
                .param("vinyard", "AAAAAAAA")
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("0", response.getHeaderValue("X-Total-Count"));

        List<WineDetailsDto> wineDetailsDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            WineDetailsDto[].class));

        assertEquals(0, wineDetailsDtos.size());
    }


}
