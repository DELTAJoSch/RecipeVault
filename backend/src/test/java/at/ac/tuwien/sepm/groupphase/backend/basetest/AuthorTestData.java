package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Data for Author tests.
 */
public interface AuthorTestData {
    Long ID_1 = 1L;
    String FIRSTNAME = "Gordon";
    String LASTNAME = "Ramsay";
    String DESCRIPTION = "testdescription";

    String BASE_URI = "/api/v1";
    String AUTHOR_MANAGEMENT_BASE_URI = BASE_URI + "/author";
    ApplicationUser USER = new ApplicationUser.ApplicationUserBuilder()
        .setEmail("admin@test.xo")
        .setAdmin(true)
        .setPassword("test")
        .setId(-42L)
        .build();

    String ADMIN_USER = "admin@test.xo";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "user@test.xo";
    List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
}
