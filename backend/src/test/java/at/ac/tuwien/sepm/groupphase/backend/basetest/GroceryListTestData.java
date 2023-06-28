package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.ArrayList;
import java.util.List;

public interface GroceryListTestData {
    String BASE_URI = "/api/v1";
    String GROCERY_LIST_BASE_URI = BASE_URI + "/grocerylist";

    ApplicationUser USER = new ApplicationUser.ApplicationUserBuilder()
        .setEmail("admin@example.com")
        .setAdmin(true)
        .setPassword("unused")
        .setId(-42L)
        .build();


    String ADMIN_USER = "admin@example.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "user@example.com";
    List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
}
