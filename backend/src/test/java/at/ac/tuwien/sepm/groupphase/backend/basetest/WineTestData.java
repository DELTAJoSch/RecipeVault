package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Data for Wine tests.
 */
public interface WineTestData {
    Long ID = 1L;
    String NAME = "TestWINE";
    String VINYARD = "vinyard XYZ";
    Double TEMP = 42.0;

    String BASE_URI = "/api/v1";
    String WINE_MANAGEMENT_BASE_URI = BASE_URI + "/wine";

    ApplicationUser USER = new ApplicationUser.ApplicationUserBuilder()
        .setEmail("test@12122392.xyz")
        .setAdmin(true)
        .setPassword("test")
        .setId(-42L)
        .build();

    String ADMIN_USER = "test@12122392.xyz";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "test.three@12122392.xyz";
    List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
}
