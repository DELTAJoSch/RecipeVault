package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.basetest.GroceryListTestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Amount;
import at.ac.tuwien.sepm.groupphase.backend.entity.AmountUnit;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.GroceryListService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class GroceryListServiceTest implements GroceryListTestData {
    @Autowired
    GroceryListService service;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    IngredientRepository ingredientRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_withZeroPortions_returnsEmptyList() {
        Long[][] portionsPerRecipe = new Long[2][2];
        portionsPerRecipe[0] = new Long[]{-1L, 0L};
        portionsPerRecipe[1] = new Long[]{-4L, 0L};
        List<Amount> groceryList = service.generateGroceryList(USER.getEmail(), portionsPerRecipe);
        assertThat(groceryList).size().isEqualTo(0);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_returnsCorrectAmounts() {
        Long[][] portionsPerRecipe = new Long[2][2];
        portionsPerRecipe[0] = new Long[]{-1L, 1L};
        portionsPerRecipe[1] = new Long[]{-4L, 2L};
        List<Amount> groceryList = service.generateGroceryList(USER.getEmail(), portionsPerRecipe);
        assertThat(groceryList).size().isEqualTo(5);
        assertThat(groceryList).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-1L, 100.0, AmountUnit.KG));
        assertThat(groceryList).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-2L, 340.0, AmountUnit.G));
        assertThat(groceryList).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-3L, 100.0, AmountUnit.PIECE));
        assertThat(groceryList).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-3L, 20.0, AmountUnit.KG));
        assertThat(groceryList).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-4L, 100.0, AmountUnit.CUP));
    }

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_convertsGToKg_whenMoreThan1000G() {
        Long[][] portionsPerRecipe = new Long[1][2];
        portionsPerRecipe[0] = new Long[]{-1L, 100L};
        List<Amount> groceryList = service.generateGroceryList(USER.getEmail(), portionsPerRecipe);
        assertThat(groceryList).extracting("ingredient.id", "amount", "unit").contains(new Tuple(-2L, 10.0, AmountUnit.KG));
    }


    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_givenNonExistentUser_throwsNotFound () {
        Long[][] portionsPerRecipe = new Long[1][2];
        portionsPerRecipe[0] = new Long[]{-1L, 100L};
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.generateGroceryList("idonot@exist.com", portionsPerRecipe), "");
        assertEquals("Benutzer mit E-Mail idonot@exist.com konnte nicht gefunden werden", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/GroceryListTests.sql"})
    public void generateGroceryList_givenNonExistentRecipe_throwsNotFound () {
        Long[][] portionsPerRecipe = new Long[1][2];
        portionsPerRecipe[0] = new Long[]{-100L, 100L};
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.generateGroceryList(USER.getEmail(), portionsPerRecipe), "");
        assertEquals("Rezept konnte nicht gefunden werden", exception.getMessage());
    }


}
