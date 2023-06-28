package at.ac.tuwien.sepm.groupphase.backend.unittests.service;

import at.ac.tuwien.sepm.groupphase.backend.basetest.ListTestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class ListServiceTest implements ListTestData {
    @Autowired
    ListService listService;

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getRecipesOfList_withBigPage_returnsCorrectNumberOfRecipes() {
        Pageable page = PageRequest.of(0, 100);
        assertThat(listService.getRecipesOfList(USER.getEmail(), "list", page)).size().isEqualTo(2);
    }


    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getRecipesOfList_givenNonExistentList_throwsNotFound() {
        Pageable page = PageRequest.of(0, 100);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> listService.getRecipesOfList(USER.getEmail(), "my wonderful list 3", page), "");
        assertEquals("Liste konnte nicht gefunden werden.", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getRecipesOfList_withSmallPage_returnsCorrectNumberOfRecipes() {
        Pageable page = PageRequest.of(0, 1);
        assertThat(listService.getRecipesOfList(USER.getEmail(), "list", page)).size().isEqualTo(1);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void createNewList_givenValidData_createsList() throws Exception {
        listService.createList(USER.getEmail(), "new and cool list");
        List<RecipeList> lists = listService.getListsOfUser(USER.getEmail());
        assertThat(lists)
            .extracting(RecipeList::getName)
            .contains("new and cool list");
    }


    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void createNewList_withAlreadyUsedName_throwsObjectAlreadyExistsException() {
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class, () -> listService.createList(USER.getEmail(), "list"), "");
        assertEquals("Eine Liste mit diesem Namen existiert bereits.", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void addToList_whenRecipeIsAlreadyInList_throwsObjectAlreadyExistsException() {
        ObjectAlreadyExistsException exception = assertThrows(ObjectAlreadyExistsException.class, () -> listService.addToList(USER.getEmail(), -1L, "list"), "");
        assertEquals("Dieses Rezept ist bereits in dieser Liste vorhanden.", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void addToList_givenValidData_addsRecipeToList() throws ObjectAlreadyExistsException {
        listService.addToList(USER.getEmail(), -3L, "list");
        Pageable page = PageRequest.of(0, 100);
        List<Recipe> recipes = listService.getRecipesOfList(USER.getEmail(), "list", page);
        assertThat(recipes)
            .extracting(Recipe::getId)
            .contains(-3L);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void getListsOfUser_returnsCorrectLists() throws ObjectAlreadyExistsException {
        List<RecipeList> lists = listService.getListsOfUser("user@example.com");
        assertThat(lists).size().isEqualTo(2);
        assertThat(lists)
            .extracting(RecipeList::getName)
            .contains("list");
        assertThat(lists)
            .extracting(RecipeList::getName)
            .contains("my wonderful list 2");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void countRecipesOfList_givenNonExistentList_throwsNotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> listService.countRecipesOfList(USER.getEmail(), "bad nonexistent list"), "");
        assertEquals("Liste konnte nicht gefunden werden.", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void countRecipesOfList_givenValidData_returnsCorrectNumber() {
        assertThat(listService.countRecipesOfList(USER.getEmail(), "list")).isEqualTo(2);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void deleteRecipeFromList_removesRecipeFromList() {
        listService.deleteRecipeFromList(USER.getEmail(), -1L, "list");
        Pageable page = PageRequest.of(0, 100);
        assertThat(listService.getRecipesOfList(USER.getEmail(), "list", page)).extracting(Recipe::getId)
            .doesNotContain(-1L);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void deleteRecipeFromList_givenNonExistentRecipe_throwsNotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> listService.deleteRecipeFromList(USER.getEmail(), -100L, "list"), "");
        assertEquals("Rezept konnte nicht gefunden werden.", exception.getMessage());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void deleteList_givenValidList_deletesList() {
        listService.deleteList(USER.getEmail(), "list");
        assertThat(listService.getListsOfUser(USER.getEmail())).extracting(RecipeList::getName).doesNotContain("list");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/ListTests.sql"})
    public void deleteList_givenNonExistentList_throwsNotFoundException() {
        NotFoundException exception = assertThrows(NotFoundException.class, () -> listService.deleteList(USER.getEmail(), "liiiiiist"), "");
        assertEquals("Liste konnte nicht gefunden werden.", exception.getMessage());
    }
}
