package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles({"test"})
public class RecipeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void newRecipe_insert_insertsData() throws Exception {
        String name = "newRecipe_insert_insertsData";
        String description = "DESC";
        Difficulty difficulty = Difficulty.EASY;

        var owner = userRepository.findByEmail("test@12122392.xyz");

        Recipe insert = new Recipe();
        insert.setDifficulty(difficulty);
        insert.setName(name);
        insert.setDescription(description);
        insert.setOwner(owner);


        var inserted = recipeRepository.save(insert);

        var pulled = recipeRepository.findById(insert.getId());

        var content = pulled.get();
        Assert.notNull(content);
        Assertions.assertEquals(name, content.getName());
        Assertions.assertEquals(description, content.getDescription());
        Assertions.assertEquals(difficulty, content.getDifficulty());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void newRecipe_insertWithExistingData_throwsError() {
        String name = "newRecipe_insert_insertsData";
        String description = "DESC";
        String shortDescription = "SHORTDESC";
        Difficulty difficulty = Difficulty.EASY;

        var owner = userRepository.findByEmail("test@12122392.xyz");

        Recipe insert = new Recipe();
        insert.setDifficulty(difficulty);
        insert.setName(name);
        insert.setDescription(description);
        insert.setShortDescription(shortDescription);
        insert.setOwner(owner);
        recipeRepository.saveAndFlush(insert);

        Recipe thrower = new Recipe();
        thrower.setDifficulty(difficulty);
        thrower.setName(name);
        thrower.setShortDescription(shortDescription);
        thrower.setDescription(description);
        thrower.setOwner(owner);

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            recipeRepository.saveAndFlush(thrower);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findByDifficulty_findsAll_withCorrectDifficulty() {
        var result = recipeRepository.findByDifficulty(Difficulty.EASY);

        Assertions.assertEquals(4, result.size());

        var name = result.stream().map(w -> w.getName()).toList();
        assertThat(name).contains("test recipe one", "test recipe two");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findByDifficulty_findNonExistent_findsNone() {
        var result = recipeRepository.findByDifficulty(Difficulty.HARD);

        Assertions.assertEquals(0, result.size());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findAll_does_findAll() {
        var result = recipeRepository.findAll();

        Assertions.assertEquals(5, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findAll_Paged_findsAllInPage() {
        Pageable pageRequest = PageRequest.of(0, 2);

        var result = recipeRepository.findAll(pageRequest);

        Assertions.assertEquals(2, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findByNameContains_lookForName_findsAll() {
        var result = recipeRepository.findByNameContaining("test");

        Assertions.assertEquals(4, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findByNameContains_lookForSpecificName_findsSpecific() {
        var result = recipeRepository.findByNameContaining("test recipe two");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findByNameContains_lookForNonexistant_findsNone() {
        var result = recipeRepository.findByNameContaining("test recipe two does not exist?");

        Assertions.assertEquals(0, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void find_lookForAllNull_findsAll() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = recipeRepository.find(null, null, pageable);

        Assertions.assertEquals(5, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void find_lookForName_findsMatches() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = recipeRepository.find("test", null, pageable);

        Assertions.assertEquals(4, result.getContent().size());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void find_lookForNonexistant_findsNone() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = recipeRepository.find(
            "test recipe two does not exist?",
            null, pageable);

        Assertions.assertEquals(0, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void find_lookForDifficulty_findsMatches() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = recipeRepository.find(null, Difficulty.EASY, pageable);

        Assertions.assertEquals(4, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void find_lookForComplexQuery_findsMatch() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = recipeRepository.find("test recipe", Difficulty.EASY, pageable);

        Assertions.assertEquals(4, result.getContent().size());
        Assertions.assertEquals("test recipe four", result.getContent().get(0).getName());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void countOfFind_lookForAllNull_findsAll() {
        var result = recipeRepository.countOfFind(null, null);

        Assertions.assertEquals(5, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void countOfFind_lookForName_findsMatches() {
        var result = recipeRepository.countOfFind("test ", null);

        Assertions.assertEquals(4, result);
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void countOfFind_lookForNonexistant_findsNone() {
        var result = recipeRepository.countOfFind("test recipe two does not exist?", null);

        Assertions.assertEquals(0, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void countOfFind_lookForDifficulty_findsMatches() {
        var result = recipeRepository.countOfFind(null, Difficulty.EASY);

        Assertions.assertEquals(4, result);
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void countOfFind_lookForComplexQuery_findsMatch() {
        var result = recipeRepository.countOfFind("test recipe one", Difficulty.EASY);

        Assertions.assertEquals(1, result);
    }
}

