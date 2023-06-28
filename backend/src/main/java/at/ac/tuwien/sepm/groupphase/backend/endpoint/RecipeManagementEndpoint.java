package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeCreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NoteDto;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.NoteMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UserPermissionException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.FavoriteService;
import at.ac.tuwien.sepm.groupphase.backend.service.NoteService;
import at.ac.tuwien.sepm.groupphase.backend.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recipe")
@Validated
public class RecipeManagementEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RecipeService recipeService;
    private final RecipeMapper recipeMapper;
    private final NoteService noteService;
    private final FavoriteService favoriteService;
    private final NoteMapper noteMapper;

    @Autowired
    public RecipeManagementEndpoint(RecipeService recipeService, NoteService noteService, RecipeMapper recipeMapper, NoteMapper noteMapper, FavoriteService favoriteService) {
        this.recipeService = recipeService;
        this.noteService = noteService;
        this.favoriteService = favoriteService;
        this.recipeMapper = recipeMapper;
        this.noteMapper = noteMapper;
    }

    /**
     * Find a specific recipe by its id.
     *
     * @param id The id to look for
     * @return Returns the recipe with the specified id
     */
    @Secured("ROLE_USER")
    @GetMapping("/{id}")
    @Operation(summary = "Get a recipe by its id", security = @SecurityRequirement(name = "apiKey"))
    @Transactional
    public RecipeDetailsDto find(@PathVariable Long id, Authentication authentication) {
        LOGGER.info("GET /api/v1/recipe/{}", id); // Logs the request information
        RecipeDetailsDto recipeDetailsDto = recipeMapper.recipeToRecipeDetailsDto(recipeService.getRecipeById(id));

        Boolean isFavorite = favoriteService.getStatus(authentication.getName(), id);
        recipeDetailsDto.setFavorite(isFavorite);
        try {
            NoteDto note = noteMapper
                .noteToNoteDto(noteService.findNote(authentication.getName(), id, authentication.getName()));
            recipeDetailsDto.setNote(note);
        } catch (NotFoundException e) {
            recipeDetailsDto.setNote(null);
        }

        return recipeDetailsDto;
    }

    /**
     * Find all recipes matching the search parameters within the page limit.
     *
     * @param page           The page to look for. Default = 0
     * @param size           The size of the page. Default = 25
     * @param difficulty     The difficulty to search for
     * @param name           The name to search for
     * @param authentication the issuing user
     * @return Returns a list of recipes matching the search porameters at the requested page
     */
    @Secured("ROLE_USER")
    @GetMapping("/search")
    @Operation(summary = "Get recipes matching search parameters by page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<RecipeListDto>> findBy(
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size,
        @RequestParam(required = false) @Size(max = 255) String name,
        @RequestParam(required = false) Difficulty difficulty,
        Authentication authentication)
        throws ValidationException {
        LOGGER.info("GET /api/v1/recipe/search?name={}", name); // Logs the request information

        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: " + page + " Size: " + size);

        RecipeSearchDto searchDto = new RecipeSearchDto.RecipeSearchDtoBuilder()
            .setName(name)
            .setDifficulty(difficulty)
            .build();

        var count = recipeService.countFindBy(searchDto);
        List<RecipeListDto> recipes = recipeMapper.recipeToRecipeListDto(recipeService.findRecipes(searchDto, pageable));
        if (authentication.getName() != null) {
            for (RecipeListDto r : recipes) {
                r.setFavorite(favoriteService.getStatus(authentication.getName(), r.getId()));
            }
        }

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(recipes);
    }

    /**
     * Update a recipe.
     *
     * @param recipeDetailsDto The new recipe details
     * @param authentication   The logged-in user
     * @return Returns the updated recipe if successful
     * @throws UserPermissionException thrown, if the user triggering the request does not have the correct permissions
     * @throws ValidationException     Thrown if the updated data is invalid
     */
    @Secured("ROLE_USER")
    @PostMapping("/{id}")
    @Operation(summary = "Update a recipe by its id", security = @SecurityRequirement(name = "apiKey"))
    public RecipeDetailsDto update(@Valid @RequestBody RecipeDetailsDto recipeDetailsDto, Authentication authentication)
        throws UserPermissionException,
        ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/recipe {}", recipeDetailsDto); // Logs the request information
        var result = recipeService.updateRecipe(recipeDetailsDto, authentication.getName());
        return recipeMapper.recipeToRecipeDetailsDto(result);
    }

    /**
     * Create a new recipe.
     *
     * @param recipeCreateDto The new recipe's data
     * @param authentication  The user triggering the request
     * @throws ValidationException Thrown if the data is invalid
     */
    @Secured("ROLE_USER")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new recipe", security = @SecurityRequirement(name = "apiKey"))
    public void create(@Valid @RequestBody RecipeCreateDto recipeCreateDto, Authentication authentication)
        throws ValidationException,
        ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/recipe", recipeCreateDto);
        recipeService.createRecipe(recipeCreateDto, authentication.getName());
    }

    /**
     * Find all recipes withing the page limit.
     *
     * @param page The page to look for. Default = 0
     * @param size The size of the page. Default = 25
     * @return Returns a list of recipes at the requested page
     */
    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get all recipes by page and size", security = @SecurityRequirement(name = "apiKey"))
    @Transactional
    public List<RecipeListDto> findAll(
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size) {
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: " + page + " Size: " + size);

        return recipeMapper.recipeToRecipeListDto(recipeService.getRecipes(pageable));
    }

    /**
     * Delete the specified recipes.
     *
     * @param id             The id of the recipes to delete
     * @param authentication The user triggering the request
     * @throws UserPermissionException Thrown if the user does not have the correct permissions for the recipe
     */
    @Secured("ROLE_USER")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete the recipes specified by the id", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@PathVariable Long id, Authentication authentication) throws UserPermissionException {
        LOGGER.info("DELETE /api/v1/recipe {}", id);
        recipeService.delete(id, authentication.getName());
    }
}


