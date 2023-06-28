package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.IngredientTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.IngredientSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.IngredientMatchingCategory;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.IngredientRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.IngredientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class IngredientServiceTest implements IngredientTestData {

    private final String USER_DETAILS = "test@12121262.com";
    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void searchIngredient_givenExistingName_findsCorrectIngredientsInDatabase() throws Exception {
        IngredientSearchDto searchDto = new IngredientSearchDto("Test", IngredientMatchingCategory.CHEESE_CREAM);

        var read = ingredientService.search(searchDto);

        assertEquals(read.size(), 3);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void createIngredient_givenNewIngredient_createsNewIngredientInDatabase() throws Exception {
        var name = "Ingredient - the sequel";

        IngredientCreateDto createDto = new IngredientCreateDto.IngredientCreateDtoBuilder()
            .withCategory(IngredientMatchingCategory.CHEESE_CREAM)
            .withName(name)
            .build();

        ingredientService.createIngredient(createDto);

        var read = ingredientRepository.findByNameContaining(name);

        assertEquals(read.size(), 1);

        var ingredient = read.get(0);
        assertEquals(ingredient.getCategory(), IngredientMatchingCategory.CHEESE_CREAM);
        assertEquals(ingredient.getName(), name);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void createIngredient_whenGivenIngredientThatAlreadyExists_throwsObjectAlreadyExistsException() {
        IngredientCreateDto createDto = new IngredientCreateDto.IngredientCreateDtoBuilder()
            .withCategory(IngredientMatchingCategory.CHEESE_CREAM)
            .withName("test ingredient one")
            .build();

        Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
            ingredientService.createIngredient(createDto);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void deleteIngredient_givenValidId_deletesCorrectIngredient() throws Exception {
        ingredientService.delete(-1L, USER_DETAILS);

        var read = ingredientRepository.findByNameContaining("test ingredient one");

        assertEquals(read.size(), 0);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void deleteIngredient_givenInvalidId_throwsNotFoundException() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> {
            ingredientService.delete(-1000L, USER_DETAILS);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void deleteIngredient_givenInvalidUser_throwsUserPermissionException() throws Exception {
        String user = "test2@12121262.com";

        Assertions.assertThrows(UserPermissionException.class, () -> {
            ingredientService.delete(-1L, user);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void updateIngredient_givenCorrectNewData_updatesNewIngredientInDatabase() throws Exception {
        IngredientDetailsDto updateDto = new IngredientDetailsDto.IngredientDetailsDtoBuilder()
            .withCategory(IngredientMatchingCategory.HERBS_AROMATIC)
            .withName("Gewürz")
            .withId(-3L)
            .build();

        ingredientService.updateIngredient(updateDto, USER_DETAILS);

        var read = ingredientRepository.findByNameContaining("Gewürz");

        assertEquals(read.size(), 1);

        var ing = read.get(0);
        assertEquals(ing.getCategory(), IngredientMatchingCategory.HERBS_AROMATIC);
        assertEquals(ing.getName(), "Gewürz");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void updateIngredient_givenIngredientThatAlreadyExists_throwsObjectAlreadyExistsException() throws Exception {
        IngredientDetailsDto updateDto = new IngredientDetailsDto.IngredientDetailsDtoBuilder()
            .withCategory(IngredientMatchingCategory.SHELLFISH)
            .withName("TestIngredient")
            .withId(-4L)
            .build();

        Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
            ingredientService.updateIngredient(updateDto, USER_DETAILS);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findIngredients_WhenCalledWithNoParameters_ReturnsCorrectIngredients() throws Exception {
        IngredientSearchDto searchDto = new IngredientSearchDto.IngredientSearchDtoBuilder()
            .setCategory(null)
            .setName(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = ingredientService.findIngredients(searchDto, pageable);

        assertEquals(4, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findIngredients_WhenCalledWithName_ReturnsCorrectIngredients() throws Exception {
        IngredientSearchDto searchDto = new IngredientSearchDto.IngredientSearchDtoBuilder()
            .setCategory(null)
            .setName("st ing")
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = ingredientService.findIngredients(searchDto, pageable);

        assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findIngredients_WhenCalledWithCategory_ReturnsCorrectIngredients() throws Exception {
        IngredientSearchDto searchDto = new IngredientSearchDto.IngredientSearchDtoBuilder()
            .setCategory(IngredientMatchingCategory.CHEESE_CREAM)
            .setName(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = ingredientService.findIngredients(searchDto, pageable);

        assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findIngredients_WhenCalledWithComplexParameters_ReturnsCorrectIngredients() throws Exception {
        IngredientSearchDto searchDto = new IngredientSearchDto.IngredientSearchDtoBuilder()
            .setCategory(IngredientMatchingCategory.SHELLFISH)
            .setName("Test")
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = ingredientService.findIngredients(searchDto, pageable);

        assertEquals(1, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/IngredientRepositoryTests.sql"})
    public void findIngredients_WhenCalledWithInvalidParameters_ReturnsNoIngredients() throws Exception {
        IngredientSearchDto searchDto = new IngredientSearchDto.IngredientSearchDtoBuilder()
            .setCategory(null)
            .setName("NONEXISTENT")
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = ingredientService.findIngredients(searchDto, pageable);

        assertEquals(0, result.size());
    }
}
