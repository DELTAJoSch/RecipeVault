package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.basetest.FavoriteTestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Favorite;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class FavoriteServiceTest implements FavoriteTestData {
    @Autowired
    private FavoriteService favoriteService;

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void addFavorite_whenGivenNewFavorite_createsNewFavoriteInDatabase() throws Exception {
        Favorite fav = new Favorite.FavoriteBuilder()
            .withRecipe(RECIPE)
            .withUser(USER)
            .build();

        favoriteService.addFavorite(USER.getEmail(), RECIPE.getId());
        List<Recipe> favs = favoriteService.getFavorites(USER.getEmail(), null);
        assertThat(favs)
            .extracting(Recipe::getId)
            .contains(-3L);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void deleteFavorite_whenGivenNonExistingUser_throwsNotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> favoriteService.delete("wow", -42L), "");
        assertEquals("Benutzer mit E-Mail wow konnte nicht gefunden werden", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void deleteFavorite_withValidId_removesFavorite() throws Exception {
        favoriteService.delete("testi@wow.xyz", -3L);
        List<Recipe> favs = favoriteService.getFavorites("testi@wow.xyz", null);
        assertThat(favs)
            .extracting(Recipe::getId)
            .doesNotContain(-3L);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void countFavorites_forNonExistingUser_throwsNotFoundException() throws Exception {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> favoriteService.countFavoritesOfUser("wowsie"), "");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void countFavorites_forExistingUser_returnsCorrectCount() throws Exception {
        assertThat(favoriteService.countFavoritesOfUser(USER.getEmail())).isEqualTo(3);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void getFavorites_withSmallPage_returnsCorrectNumberOfFavorites() throws Exception {
        List<Recipe> favs = favoriteService.getFavorites("testi@wow.xyz", PageRequest.of(0, 1));
        assertThat(favs).size().isEqualTo(1);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void getFavorites_withBigPage_returnsCorrectNumberOfFavorites() throws Exception {
        List<Recipe> favs = favoriteService.getFavorites(USER.getEmail(), PageRequest.of(0, 25));
        assertThat(favs).size().isEqualTo(3);
    }
}
