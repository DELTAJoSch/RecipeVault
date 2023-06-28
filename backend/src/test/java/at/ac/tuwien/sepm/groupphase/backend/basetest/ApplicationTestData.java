package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOptionType;

import java.util.ArrayList;
import java.util.List;


public interface ApplicationTestData {
    Long ID = 100L;
    String NAME = "TEST OPTION XYZ";
    String VALUE = "FALSE";
    String DEFAULT = "TRUE";
    ApplicationOptionType TYPE = ApplicationOptionType.BOOLEAN;

    ApplicationOption OPTION = new ApplicationOption.ApplicationOptionBuilder()
        .withId(ID)
        .withName(NAME)
        .withValue(VALUE)
        .withDefaultValue(DEFAULT)
        .withType(TYPE)
        .build();

    ApplicationOptionDto OPTION_DTO = new ApplicationOptionDto.ApplicationOptionBuilder()
        .withId(ID)
        .withName(NAME)
        .withValue(VALUE)
        .withDefaultValue(DEFAULT)
        .withOptionType(TYPE)
        .build();

    String BASE_URI = "/api/v1";
    String APPLICATION_OPTION_BASE_URI = BASE_URI + "/options";

    String ADMIN_USER = "test@12122392.xyz";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
}
