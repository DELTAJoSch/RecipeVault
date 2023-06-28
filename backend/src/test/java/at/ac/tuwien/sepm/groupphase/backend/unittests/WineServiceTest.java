package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.WineTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.WineRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.WineService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WineServiceTest implements WineTestData {
    private final Wine WINE = new Wine.WineBuilder()
        .setCategory(WineCategory.DESSERT)
        .setName(NAME)
        .setVinyard(VINYARD)
        .setTemperature(TEMP)
        .setOwner(USER)
        .build();
    private final String USER_DETAILS = "test@12122392.xyz";
    private UserInfoDto ADMIN_USER_INFO = new UserInfoDto.UserInfoDtoBuilder()
        .withId(-42L)
        .withName("test@12122392.xyz")
        .build();
    private UserInfoDto USER_INFO = new UserInfoDto.UserInfoDtoBuilder()
        .withId(-44L)
        .withName("test.three@12122392.xyz")
        .build();
    @Autowired
    private WineService wineService;

    @Autowired
    private WineRepository wineRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void createWine_whenGivenNewWine_createsNewWineInDatabase() throws Exception {
        WineCreateDto createDto = new WineCreateDto.WineCreateDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName(NAME)
            .withTemperature(TEMP)
            .withVinyard(VINYARD)
            .build();

        wineService.createWine(createDto, USER_DETAILS);

        var read = wineRepository.findByNameContaining(NAME);

        assertEquals(read.size(), 1);

        var wine = read.get(0);
        assertEquals(wine.getCategory(), WineCategory.DESSERT);
        assertEquals(wine.getVinyard(), VINYARD);
        assertEquals(wine.getName(), NAME);
        assertEquals(wine.getTemperature(), TEMP);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void getWines_whenGivenPage_findsWineInPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 3);

        var result = wineService.getWines(pageable);

        assertEquals(result.size(), 3);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void createWine_whenGivenWineThatAlreadyExists_throwsObjectAlreadyExistsException() throws Exception {
        WineCreateDto createDto = new WineCreateDto.WineCreateDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName("test wine one")
            .withTemperature(10.0)
            .withVinyard("test vinyard one")
            .build();

        Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
            wineService.createWine(createDto, USER_DETAILS);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void getWineById_whenGivenValidId_findsCorrectWine() throws Exception {
        var result = wineService.getWineById(-1L);

        assertEquals(result.getName(), "test wine one");
        assertEquals(result.getVinyard(), "test vinyard one");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void getWineById_whenGivenInvalidId_throwsNotFoundException() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> {
            wineService.getWineById(-1000L);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void deleteWine_whenGivenValidId_deletesCorrectWine() throws Exception {
        wineService.delete(-1L, USER_DETAILS);

        var read = wineRepository.findByNameContaining("test wine one");

        assertEquals(read.size(), 0);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void deleteWine_whenGivenInvalidId_throwsNotFoundException() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> {
            wineService.delete(-1000L, USER_DETAILS);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void deleteWine_whenGivenInvalidUser_throwsUserPermissionException() throws Exception {
        String user = "test.two@12122392.xyz";

        Assertions.assertThrows(UserPermissionException.class, () -> {
            wineService.delete(-1L, user);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void deleteWine_whenDeletedByAdmin_deletesCorrectWine() throws Exception {
        wineService.delete(-5L, USER_DETAILS);

        var read = wineRepository.findByNameContaining("admin deleteable wine");

        assertEquals(read.size(), 0);
    }


    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void updateWine_whenGivenCorrectNewData_updatesNewWineInDatabase() throws Exception {
        WineDetailsDto updateDto = new WineDetailsDto.WineDetailsDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName(NAME)
            .withTemperature(TEMP)
            .withVinyard(VINYARD)
            .withOwner(ADMIN_USER_INFO)
            .withId(-1L)
            .build();

        wineService.updateWine(updateDto, USER_DETAILS);

        var read = wineRepository.findByNameContaining(NAME);

        assertEquals(read.size(), 1);

        var wine = read.get(0);
        assertEquals(wine.getCategory(), WineCategory.DESSERT);
        assertEquals(wine.getVinyard(), VINYARD);
        assertEquals(wine.getName(), NAME);
        assertEquals(wine.getTemperature(), TEMP);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void updateWine_givenWineThatAlreadyExists_throwsObjectAlreadyExistsException() throws Exception {
        WineDetailsDto updateDto = new WineDetailsDto.WineDetailsDtoBuilder()
            .withCategory(WineCategory.DESSERT)
            .withName("test wine one")
            .withTemperature(10.0)
            .withVinyard("test vinyard one")
            .withOwner(ADMIN_USER_INFO)
            .withId(-2L)
            .build();

        Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
            wineService.updateWine(updateDto, USER_DETAILS);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void countAll_WhenCalled_ReturnsCorrectCount() throws Exception {
        long count = wineService.countAll();

        assertEquals(5, count);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findWines_WhenCalledWithNoParameters_ReturnsCorrectWines() throws Exception {
        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setCategory(null)
            .setName(null)
            .setVinyard(null)
            .setCountry(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = wineService.findWines(searchDto, pageable);

        assertEquals(5, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findWines_WhenCalledWithName_ReturnsCorrectWines() throws Exception {
        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setCategory(null)
            .setName("st wine")
            .setVinyard(null)
            .setCountry(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = wineService.findWines(searchDto, pageable);

        assertEquals(4, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findWines_WhenCalledWithVinyard_ReturnsCorrectWines() throws Exception {
        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setCategory(null)
            .setName(null)
            .setVinyard("vinyard o")
            .setCountry(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = wineService.findWines(searchDto, pageable);

        assertEquals(3, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findWines_WhenCalledWithCountry_ReturnsCorrectWines() throws Exception {
        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setCategory(null)
            .setName(null)
            .setVinyard(null)
            .setCountry("AT")
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = wineService.findWines(searchDto, pageable);

        assertEquals(0, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findWines_WhenCalledWithCategory_ReturnsCorrectWines() throws Exception {
        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setCategory(WineCategory.SPARKLING)
            .setName(null)
            .setVinyard(null)
            .setCountry(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = wineService.findWines(searchDto, pageable);

        assertEquals(3, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findWines_WhenCalledWithComplexParameters_ReturnsCorrectWines() throws Exception {
        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setCategory(WineCategory.SPARKLING)
            .setName("tw")
            .setVinyard("one")
            .setCountry(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = wineService.findWines(searchDto, pageable);

        assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/WineRepositoryTests.sql"})
    public void findWines_WhenCalledWithInvalidParameters_ReturnsNoWines() throws Exception {
        WineSearchDto searchDto = new WineSearchDto.WineSearchDtoBuilder()
            .setCategory(null)
            .setName("OTTOKAR")
            .setVinyard("AAAAAAA")
            .setCountry(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = wineService.findWines(searchDto, pageable);

        assertEquals(0, result.size());
    }
}
