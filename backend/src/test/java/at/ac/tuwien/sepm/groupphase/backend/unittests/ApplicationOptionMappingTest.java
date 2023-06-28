package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationOptionMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOptionType;
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
public class ApplicationOptionMappingTest {
    private final ApplicationOption OPTION = new ApplicationOption.ApplicationOptionBuilder()
        .withName("TEST")
        .withType(ApplicationOptionType.BOOLEAN)
        .withId(42L)
        .withValue("TRUE")
        .withDefaultValue("FALSE")
        .build();
    @Autowired
    private ApplicationOptionMapper applicationOptionMapper;

    @Test
    public void givenNothing_whenMappingApplicationOptionsToApplicationOptionsDto_thenEntityHasAllProperties() {
        ApplicationOptionDto applicationOptionDto = this.applicationOptionMapper.applicationOptionToApplicationOptionDto(OPTION);

        assertAll(
            () -> assertEquals(42L, applicationOptionDto.getId()),
            () -> assertEquals("TEST", applicationOptionDto.getName()),
            () -> assertEquals("TRUE", applicationOptionDto.getValue()),
            () -> assertEquals("FALSE", applicationOptionDto.getDefaultValue()),
            () -> assertEquals(ApplicationOptionType.BOOLEAN, applicationOptionDto.getType())
        );
    }

    @Test
    public void givenNothing_whenMapListWithTwoWineEntitiesToDetailsDto_thenGetListWithSizeTwoAndAllProperties() {
        List<ApplicationOption> options = new ArrayList<>();
        options.add(OPTION);
        options.add(OPTION);


        List<ApplicationOptionDto> optionDtos = this.applicationOptionMapper.applicationOptionToApplicationOptionDto(options);
        assertEquals(2, optionDtos.size());

        ApplicationOptionDto applicationOptionDto = optionDtos.get(0);

        assertAll(
            () -> assertEquals(42L, applicationOptionDto.getId()),
            () -> assertEquals("TEST", applicationOptionDto.getName()),
            () -> assertEquals("TRUE", applicationOptionDto.getValue()),
            () -> assertEquals("FALSE", applicationOptionDto.getDefaultValue()),
            () -> assertEquals(ApplicationOptionType.BOOLEAN, applicationOptionDto.getType())
        );
    }
}
