package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ListOverviewDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.AmountMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ListMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.FavoriteService;
import at.ac.tuwien.sepm.groupphase.backend.service.ListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotBlank;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/list")
@Validated
public class ListEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ListService listService;
    private final AmountMapper amountMapper;
    private final ListMapper listMapper;

    private final FavoriteService favoriteService;

    private final RecipeMapper recipeMapper;

    @Autowired
    public ListEndpoint(ListService listService, AmountMapper amountMapper, ListMapper listMapper, RecipeMapper recipeMapper, FavoriteService favoriteService) {
        this.listService = listService;
        this.amountMapper = amountMapper;
        this.listMapper = listMapper;
        this.recipeMapper = recipeMapper;
        this.favoriteService = favoriteService;
    }

    /**
     * Get all lists of a user.
     *
     * @param authentication issuing user
     * @return list of recipe lists
     */
    @Secured("ROLE_USER")
    @GetMapping()
    @Operation(summary = "Get all lists of user", security = @SecurityRequirement(name = "apiKey"))
    public List<ListOverviewDto> getLists(Authentication authentication) {
        LOGGER.info("GET /api/v1/list");
        return this.listMapper.recipeListToListOverviewDto(this.listService.getListsOfUser(authentication.getName()));
    }

    /**
     * Get all recipes of a list.
     *
     * @param authentication issuing user
     * @return list of recipes
     */
    @Secured("ROLE_USER")
    @GetMapping("/{name}")
    @Operation(summary = "Get all recipes of a list", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<RecipeListDto>> getRecipes(
        @PathVariable String name, Authentication authentication,
        @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
        @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size) {
        LOGGER.info("GET /api/v1/list/{}", name);
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: " + page + " Size: " + size);
        var count = listService.countRecipesOfList(authentication.getName(), name);

        List<RecipeListDto> recipes = recipeMapper.recipeToRecipeListDto(listService.getRecipesOfList(authentication.getName(), name, pageable));
        for (RecipeListDto r : recipes) {
            r.setFavorite(favoriteService.getStatus(authentication.getName(), r.getId()));
        }

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(recipes);
    }


    /**
     * Remove a recipe from a user's list.
     *
     * @param recipeId       id of recipe, that is to be removed from the list
     * @param authentication The user triggering the request
     */
    @Secured("ROLE_USER")
    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a recipe from a list", security = @SecurityRequirement(name = "apiKey"))
    public void removeRecipeFromList(@RequestParam(value = "recipeId") Long recipeId, 
        @PathVariable String name, Authentication authentication) {
        LOGGER.info("DELETE /api/v1/list?name={}?recipeId={}", name, recipeId);
        listService.deleteRecipeFromList(authentication.getName(), recipeId, name);
    }

    /**
     * Get the number of recipes in a list.
     *
     * @return Returns the number of list entries
     */
    @Secured("ROLE_USER")
    @GetMapping("/number")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the count of all recipes of a list for a user", security = @SecurityRequirement(name = "apiKey"))
    public long count(
        @RequestParam(value = "name") 
        @NotBlank(message = "Listenname darf nicht leer sein")
        @Size(max = 255, message = "Listenname darf nicht länger als 255 Zeichen sein")
        String name, Authentication authentication) {
        LOGGER.info("GET /api/v1/list/number?name={}", name);
        return listService.countRecipesOfList(authentication.getName(), name);
    }

    /**
     * Create a new list for a user.
     *
     * @param name           of list
     * @param authentication issuing user
     * @throws ObjectAlreadyExistsException if the user already has a list with this name
     * @throws ValidationException          if the name is empty
     */
    @Secured("ROLE_USER")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create a new List", security = @SecurityRequirement(name = "apiKey"))
    public void createList(
        @RequestParam(value = "name") 
        @NotBlank(message = "Listenname darf nicht leer sein")
        @Size(max = 255, message = "Listenname darf nicht länger als 255 Zeichen sein")
        String name,
        Authentication authentication) throws ObjectAlreadyExistsException, ValidationException {
        LOGGER.info("POST /api/v1/list?name={}", name);
        listService.createList(authentication.getName(), name);
    }

    /**
     * Delete a list from a user.
     *
     * @param name           of list
     * @param authentication the issuing user
     */
    @Secured("ROLE_USER")
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a list", security = @SecurityRequirement(name = "apiKey"))
    public void deleteList(
        @RequestParam(value = "name") 
        @NotBlank(message = "Listenname darf nicht leer sein")
        @Size(max = 255, message = "Listenname darf nicht länger als 255 Zeichen sein")
        String name, Authentication authentication) {
        LOGGER.info("DELETE /api/v1/list?name={}", name);
        listService.deleteList(authentication.getName(), name);
    }

    /**
     * Add a recipe to an existing list of a user.
     *
     * @param name           of list
     * @param recipeId       id or recipe
     * @param authentication the issuing user
     * @throws ObjectAlreadyExistsException if recipe is already in the list
     */
    @Secured("ROLE_USER")
    @PostMapping("/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add a recipe to a list", security = @SecurityRequirement(name = "apiKey"))
    public void addToList(@PathVariable String name, @RequestParam(value = "recipeId") Long recipeId, Authentication authentication) throws ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/list/{}?recipeId={}", name, recipeId);
        listService.addToList(authentication.getName(), recipeId, name);
    }

}
