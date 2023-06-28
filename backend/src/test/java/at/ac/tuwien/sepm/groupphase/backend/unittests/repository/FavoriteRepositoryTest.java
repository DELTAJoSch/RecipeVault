package at.ac.tuwien.sepm.groupphase.backend.unittests.repository;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Favorite;
import at.ac.tuwien.sepm.groupphase.backend.repository.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.FavoriteTestData.RECIPE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.FavoriteTestData.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test"})
@DataJpaTest
public class FavoriteRepositoryTest implements TestData {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void addValidFavorite_AddsFavoriteToTable() {
        assertThat(favoriteRepository.findAll()).size().isEqualTo(5);
        Optional<Favorite> fav = favoriteRepository.findByUserIdAndRecipeId(-402L, -3L);
        assertThat(fav).isEmpty();
        favoriteRepository.saveAndFlush(new Favorite(USER, RECIPE));
        assertThat(favoriteRepository.findAll()).size().isEqualTo(6);
        fav = favoriteRepository.findByUserIdAndRecipeId(-402L, -3L);
        assertThat(fav).isNotEmpty();
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void deleteInvalidFavorite_DeletesNothing() {
        assertThat(favoriteRepository.findAll()).size().isEqualTo(5);
        favoriteRepository.delete(new Favorite(USER, RECIPE));
        assertThat(favoriteRepository.findAll()).size().isEqualTo(5);
    }


    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void findExistingFavorite_ByUserId_And_RecipeId() {
        Optional<Favorite> fav = favoriteRepository.findByUserIdAndRecipeId(-402L, -1L);
        assertThat(fav).isNotEmpty();
        assertThat(fav.get().getRecipe().getId()).isEqualTo(-1L);
        assertThat(fav.get().getUser().getId()).isEqualTo(-402L);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/FavoriteTest.sql"})
    public void findNonExistingFavorite_ByUserId_And_RecipeId() {
        Optional<Favorite> fav = favoriteRepository.findByUserIdAndRecipeId(-402L, -100L);
        assertThat(fav).isEmpty();
    }

}
