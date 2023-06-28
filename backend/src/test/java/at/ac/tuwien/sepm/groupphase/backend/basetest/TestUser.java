package at.ac.tuwien.sepm.groupphase.backend.basetest;

import java.util.ArrayList;
import java.util.List;

public interface TestUser {
    Long ID = 1L;
    String TEST_USER_EMAIL_VALID = "someone@example.com";

    String BASE_URI = "/api/v1";
    String USER_BASE_URI = BASE_URI + "/user";

    String ADMIN_USER = "admin@example.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "user@example.com";
    String DEFAULT_USER2 = "user2@example.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
}
