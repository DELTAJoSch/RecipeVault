package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.WineTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.WineMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class WineMappingTest implements WineTestData {

    private final Wine WINE = new Wine.WineBuilder()
        .setCategory(WineCategory.DESSERT)
        .setName(NAME)
        .setVinyard(VINYARD)
        .setId(ID)
        .setTemperature(TEMP)
        .setOwner(USER)
        .build();
    @Autowired
    private WineMapper wineMapper;

    @Test
    public void givenNothing_whenMappingWineToWineDetailsDto_thenEntityHasAllProperties() {
        WineDetailsDto wineDetailsDto = this.wineMapper.wineToWineDetailsDto(WINE);

        UserInfoDto userInfoDto = new UserInfoDto.UserInfoDtoBuilder()
            .withId(USER.getId())
            .withName(USER.getEmail())
            .build();

        assertAll(
            () -> assertEquals(ID, wineDetailsDto.getId()),
            () -> assertEquals(NAME, wineDetailsDto.getName()),
            () -> assertEquals(TEMP, wineDetailsDto.getTemperature()),
            () -> assertEquals(VINYARD, wineDetailsDto.getVinyard()),
            () -> assertEquals(userInfoDto, wineDetailsDto.getOwner()),
            () -> assertEquals(WineCategory.DESSERT, wineDetailsDto.getCategory())
        );
    }

    @Test
    public void givenNothing_whenMapListWithTwoWineEntitiesToDetailsDto_thenGetListWithSizeTwoAndAllProperties() {
        List<Wine> wines = new ArrayList<>();
        wines.add(WINE);
        wines.add(WINE);

        UserInfoDto userInfoDto = new UserInfoDto.UserInfoDtoBuilder()
            .withId(USER.getId())
            .withName(USER.getEmail())
            .build();

        List<WineDetailsDto> wineDetailsDtos = this.wineMapper.wineToWineDetailsDto(wines);
        assertEquals(2, wineDetailsDtos.size());

        WineDetailsDto wineDetailsDto = wineDetailsDtos.get(0);
        assertAll(
            () -> assertEquals(ID, wineDetailsDto.getId()),
            () -> assertEquals(NAME, wineDetailsDto.getName()),
            () -> assertEquals(TEMP, wineDetailsDto.getTemperature()),
            () -> assertEquals(VINYARD, wineDetailsDto.getVinyard()),
            () -> assertEquals(userInfoDto, wineDetailsDto.getOwner()),
            () -> assertEquals(WineCategory.DESSERT, wineDetailsDto.getCategory())
        );
    }

}
