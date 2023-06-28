package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains Data for favorite tests.
 */
public interface FavoriteTestData {

    String BASE_URI = "/api/v1";
    String FAVORITE_BASE_URI = BASE_URI + "/favorites";

    ApplicationUser USER = new ApplicationUser.ApplicationUserBuilder()
        .setEmail("testi@supi.xyz")
        .setAdmin(true)
        .setPassword("unused")
        .setId(-402L)
        .build();

    String ADMIN_USER = "testi@supi.xyz";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "testi@supi.xyz";
    List<String> DEFAULT_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
    Recipe RECIPE = new Recipe.RecipeBuilder()
        .setDescription("Wow, so good")
        .setId(-3L)
        .setName("Pizza")
        .setOwner(USER)
        .setDifficulty(Difficulty.HARD)
        .build();
}
