package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Data for Ingredient tests.
 */
public interface IngredientTestData {
    String NAME = "TestIngredient";

    String BASE_URI = "/api/v1";
    String INGREDIENT_MANAGEMENT_BASE_URI = BASE_URI + "/ingredient";

    String ADMIN_USER = "test@12121262.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };

    String DEFAULT_USER = "test3@12121262.com";
    List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };

}