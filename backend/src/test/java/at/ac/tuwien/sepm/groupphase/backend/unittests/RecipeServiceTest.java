package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.RecipeTestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.repository.RecipeRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import jakarta.transaction.Transactional;
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
import org.springframework.transaction.UnexpectedRollbackException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class RecipeServiceTest implements RecipeTestData {
    private final Recipe RECIPE = new Recipe.RecipeBuilder()
        .setDifficulty(Difficulty.EASY)
        .setName(NAME)
        //.setOwner(USER)
        .build();
    private final String USER_DETAILS = "test@12122392.xyz";
    private UserInfoDto ADMIN_USER_INFO = new UserInfoDto.UserInfoDtoBuilder()
        .withId(-42L)
        .withName("test@12122392.xyz")
        .build();
    private UserInfoDto USER_INFO = new UserInfoDto.UserInfoDtoBuilder()
        .withId(-44L)
        .withName("test.three@12122392.xyz")
        .build();
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void createRecipe_whenGivenNewRecipeWithoutIngredients_createsNewRecipeInDatabaseAndNoRecommendation() throws Exception {
        RecipeCreateDto createDto = new RecipeCreateDto.RecipeCreateDtoBuilder()
            .setDifficulty(DIFFICULTY)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .build();

        recipeService.createRecipe(createDto, USER_DETAILS);

        var read = recipeRepository.findByNameContaining(NAME);

        assertEquals(read.size(), 1);

        var recipe = read.get(0);
        assertEquals(recipe.getDifficulty(), Difficulty.EASY);
        assertEquals(recipe.getName(), NAME);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    @Transactional
    public void createRecipe_whenGivenNewRecipeWithIngredients_generatesWineRecommendationAndNewRecipeInDatabase() throws Exception {
        RecipeCreateDto createDto = new RecipeCreateDto.RecipeCreateDtoBuilder()
            .setDifficulty(DIFFICULTY)
            .setName(NAME)
            .setDescription(DESCRIPTION)
            .setIngredients(AMOUNT_DTOS)
            .build();

        recipeService.createRecipe(createDto, USER_DETAILS);

        var read = recipeRepository.findByNameContaining(NAME);

        assertEquals(read.size(), 1);

        var recipe = read.get(0);
        assertNotNull(recipe.getRecommendedCategory());
        assertNotNull(recipe.getRecommendationConfidence());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void getRecipes_whenGivenPage_findsRecipeInPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 3);

        var result = recipeService.getRecipes(pageable);

        assertEquals(result.size(), 3);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void createRecipe_whenGivenRecipeThatAlreadyExists_throwsUnexpectedRollbackException() throws Exception {
        RecipeCreateDto createDto = new RecipeCreateDto.RecipeCreateDtoBuilder()
            .setDifficulty(Difficulty.EASY)
            .setName("test recipe one")
            .setDescription("test description one")
            .build();

        Assertions.assertThrows(UnexpectedRollbackException.class, () -> {
            recipeService.createRecipe(createDto, USER_DETAILS);
        });
    }





    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void getRecipeById_whenGivenValidId_findsCorrectRecipe() throws Exception {
        var result = recipeService.getRecipeById(-1L);

        assertEquals(result.getName(), "test recipe one");
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void getRecipeById_whenGivenInvalidId_throwsNotFoundException() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> {
            recipeService.getRecipeById(-1000L);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void deleteRecipe_whenGivenValidId_deletesCorrectRecipe() throws Exception {
        recipeService.delete(-1L, USER_DETAILS);

        var read = recipeRepository.findByNameContaining("test recipe one");

        assertEquals(read.size(), 0);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void deleteRecipe_whenGivenInvalidId_throwsNotFoundException() throws Exception {
        Assertions.assertThrows(NotFoundException.class, () -> {
            recipeService.delete(-1000L, USER_DETAILS);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void deleteRecipe_whenGivenInvalidUser_throwsUserPermissionException() throws Exception {
        String user = "test.two@12122392.xyz";

        Assertions.assertThrows(UserPermissionException.class, () -> {
            recipeService.delete(-1L, user);
        });
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void deleteRecipe_whenDeletedByAdmin_deletesCorrectRecipe() throws Exception {
        recipeService.delete(-5L, USER_DETAILS);

        var read = recipeRepository.findByNameContaining("admin deleteable recipe");

        assertEquals(read.size(), 0);
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void updateRecipe_whenGivenCorrectNewDataWithoutIngredients_updatesNewRecipeInDatabaseAndSetsRecommendationNull() throws Exception {
        RecipeDetailsDto updateDto = new RecipeDetailsDto.RecipeDetailsDtoBuilder()
            .withDifficulty(Difficulty.EASY)
            .withName(NAME)
            .withOwner(ADMIN_USER_INFO)
            .withId(-1L)
            .withDescription(DESCRIPTION)
            .build();

        recipeService.updateRecipe(updateDto, USER_DETAILS);

        var read = recipeRepository.findByNameContaining(NAME);

        assertEquals(read.size(), 1);

        var recipe = read.get(0);
        assertEquals(recipe.getDifficulty(), Difficulty.EASY);
        assertEquals(recipe.getDescription(), DESCRIPTION);
        assertEquals(recipe.getName(), NAME);
        assertNull(recipe.getRecommendationConfidence());
        assertNull(recipe.getRecommendedCategory());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void updateRecipe_whenGivenCorrectNewDataWithIngredients_updatesNewRecipeInDatabase() throws Exception {
        RecipeDetailsDto updateDto = new RecipeDetailsDto.RecipeDetailsDtoBuilder()
            .withDifficulty(Difficulty.EASY)
            .withName(NAME)
            .withOwner(ADMIN_USER_INFO)
            .withId(-1L)
            .withDescription(DESCRIPTION)
            .withIngredients(AMOUNT_DTOS)
            .build();

        recipeService.updateRecipe(updateDto, USER_DETAILS);

        var read = recipeRepository.findByNameContaining(NAME);

        assertEquals(read.size(), 1);

        var recipe = read.get(0);
        assertEquals(recipe.getDifficulty(), Difficulty.EASY);
        assertEquals(recipe.getDescription(), DESCRIPTION);
        assertEquals(recipe.getName(), NAME);
        assertNotNull(recipe.getRecommendedCategory());
        assertNotNull(recipe.getRecommendationConfidence());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void updateRecipe_givenRecipeThatAlreadyExists_throwsUnexpectedRollbackException() throws Exception {
        RecipeDetailsDto updateDto = new RecipeDetailsDto.RecipeDetailsDtoBuilder()
            .withDifficulty(Difficulty.EASY)
            .withName("test recipe one")
            .withDescription("test description one")
            .withOwner(ADMIN_USER_INFO)
            .withId(-2L)
            .build();

        Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
            recipeService.updateRecipe(updateDto, USER_DETAILS);
        });
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void countAll_WhenCalled_ReturnsCorrectCount() throws Exception {
        long count = recipeService.countAll();

        assertEquals(5, count);
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findRecipes_WhenCalledWithNoParameters_ReturnsCorrectRecipes() throws Exception {
        RecipeSearchDto searchDto = new RecipeSearchDto.RecipeSearchDtoBuilder()
            .setName(null)
            .setDifficulty(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = recipeService.findRecipes(searchDto, pageable);

        assertEquals(5, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findRecipes_WhenCalledWithName_ReturnsCorrectRecipes() throws Exception {
        RecipeSearchDto searchDto = new RecipeSearchDto.RecipeSearchDtoBuilder()
            .setDifficulty(null)
            .setName("st recipe")
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = recipeService.findRecipes(searchDto, pageable);

        assertEquals(4, result.size());
    }


    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findRecipes_WhenCalledWithCategory_ReturnsCorrectRecipes() throws Exception {
        RecipeSearchDto searchDto = new RecipeSearchDto.RecipeSearchDtoBuilder()
            .setDifficulty(Difficulty.EASY)
            .setName(null)
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = recipeService.findRecipes(searchDto, pageable);

        assertEquals(4, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findRecipes_WhenCalledWithComplexParameters_ReturnsCorrectRecipes() throws Exception {
        RecipeSearchDto searchDto = new RecipeSearchDto.RecipeSearchDtoBuilder()
            .setDifficulty(Difficulty.EASY)
            .setName("st recipe")
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = recipeService.findRecipes(searchDto, pageable);

        assertEquals(4, result.size());
    }

    @Test
    @Sql(scripts = {"classpath:/sql/RecipeRepositoryTests.sql"})
    public void findRecipes_WhenCalledWithInvalidParameters_ReturnsNoRecipes() throws Exception {
        RecipeSearchDto searchDto = new RecipeSearchDto.RecipeSearchDtoBuilder()
            .setDifficulty(null)
            .setName("AAAAAAAAAAAAA")
            .build();

        Pageable pageable = PageRequest.of(0, 25);

        var result = recipeService.findRecipes(searchDto, pageable);


        assertEquals(0, result.size());
    }

}
