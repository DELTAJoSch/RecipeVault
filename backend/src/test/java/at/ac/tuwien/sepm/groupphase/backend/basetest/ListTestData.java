package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public interface ListTestData {

    String BASE_URI = "/api/v1";
    String LIST_BASE_URI = BASE_URI + "/list";

    ApplicationUser USER = new ApplicationUser.ApplicationUserBuilder()
        .setEmail("admin@example.com")
        .setAdmin(true)
        .setPassword("unused")
        .setId(-42L)
        .build();

    Recipe RECIPE = new Recipe.RecipeBuilder()
        .setId(-1L)
        .setDescription("test description one")
        .setDifficulty(Difficulty.EASY)
        .setName("test recipe one")
        .setOwner(USER)
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
