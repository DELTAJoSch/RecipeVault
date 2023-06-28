package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ingredient;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ActiveProfiles({"test"})
public class IngredientRepositoryTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findByNameContains_lookForName_findsAll() {
        var result = ingredientRepository.findByNameContaining("Test");

        Assertions.assertEquals(3, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findByNameContains_lookForSpecificName_findsSpecific() {
        var result = ingredientRepository.findByNameContaining("TestIngredient");

        Assertions.assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findByNameContains_lookForNonexistent_findsNone() {
        var result = ingredientRepository.findByNameContaining("nonexistent");

        Assertions.assertEquals(0, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findTopTenByNameContaining_lookForName_findsAllTopTen() {
        var result = ingredientRepository.findTop10ByNameContaining("Test");

        Assertions.assertEquals(3, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findTopTenByNameContaining_lookForNonexistent_findsNone() {
        var result = ingredientRepository.findTop10ByNameContaining("nonexistent");

        Assertions.assertEquals(0, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void newIngredient_insert_insertsData() {
        String name = "newIngredient_insert_insertsData";
        IngredientMatchingCategory category = IngredientMatchingCategory.VEGETABLE_GREEN;

        Ingredient ingredient = new Ingredient();
        ingredient.setCategory(category);
        ingredient.setName(name);

        var inserted = ingredientRepository.save(ingredient);

        var pulled = ingredientRepository.findByName(ingredient.getName());

        var content = pulled.get();
        Assert.notNull(content);
        Assertions.assertEquals(name, content.getName());
        Assertions.assertEquals(category, content.getCategory());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void newIngredient_insertWithExistingData_throwsError() {
        String name = "test";
        IngredientMatchingCategory category = IngredientMatchingCategory.CHEESE_CREAM;

        Ingredient ingredient = new Ingredient.IngredientBuilder()
            .setCategory(category)
            .setName(name)
            .build();

        ingredientRepository.saveAndFlush(ingredient);

        Ingredient duplicate = new Ingredient.IngredientBuilder()
            .setCategory(category)
            .setName(name)
            .build();

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            ingredientRepository.saveAndFlush(duplicate);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void find_lookForAllNull_findsAll() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = ingredientRepository.find(null, null, pageable);

        Assertions.assertEquals(4, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void find_lookForName_findsMatches() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = ingredientRepository.find("TestIng", null, pageable);

        Assertions.assertEquals(3, result.getContent().size());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void find_lookForNonexistent_findsNone() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = ingredientRepository.find(
            "nonexistent",
            null,
            pageable);

        Assertions.assertEquals(0, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void find_lookForCategory_findsMatches() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = ingredientRepository.find(null, IngredientMatchingCategory.SHELLFISH, pageable);

        Assertions.assertEquals(1, result.getContent().size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void find_lookForComplexQuery_findsMatch() {
        Pageable pageable = PageRequest.of(0, 25);
        var result = ingredientRepository.find("test", IngredientMatchingCategory.CHEESE_CREAM, pageable);

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("test ingredient one", result.getContent().get(0).getName());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void countOfFind_lookForAllNull_findsAll() {
        var result = ingredientRepository.countOfFind(null,null);

        Assertions.assertEquals(4, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void countOfFind_lookForName_findsMatches() {
        var result = ingredientRepository.countOfFind("TestIng", null);

        Assertions.assertEquals(3, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void countOfFind_lookForNonexistent_findsNone() {
        var result = ingredientRepository.countOfFind("nonexistent", null);

        Assertions.assertEquals(0, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void countOfFind_lookForCategory_findsMatches() {
        var result = ingredientRepository.countOfFind(null, IngredientMatchingCategory.SHELLFISH);

        Assertions.assertEquals(1, result);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void countOfFind_lookForComplexQuery_findsMatch() {
        var result = ingredientRepository.countOfFind("test ", IngredientMatchingCategory.CHEESE_CREAM);

        Assertions.assertEquals(1, result);
    }

}
