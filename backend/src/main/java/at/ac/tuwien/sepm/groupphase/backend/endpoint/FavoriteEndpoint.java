package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.RecipeMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ObjectAlreadyExistsException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Endpoint for Favorites.
 */
@RestController
@RequestMapping(value = "/api/v1/favorites")
@Validated
public class FavoriteEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final RecipeMapper recipeMapper;

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteEndpoint(RecipeMapper recipeMapper, FavoriteService favoriteService) {
        this.recipeMapper = recipeMapper;
        this.favoriteService = favoriteService;
    }

    /**
     * Get all favorites with in the page limit.
     *
     * @param page           The page to look for. Default = 0
     * @param size           The size of the page. Default = 25
     * @param authentication The user triggering the request
     * @return Returns a list of favorites at the requested page
     */
    @Secured("ROLE_USER")
    @GetMapping()
    @Operation(summary = "Get all favorites by page and size", security = @SecurityRequirement(name = "apiKey"))
    public ResponseEntity<List<RecipeListDto>> getFavorites(Authentication authentication,
                                                            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero(message = "Seitenzahl darf nicht negativ sein") int page,
                                                            @RequestParam(required = false, defaultValue = "25") @Positive(message = "Seitengröße muss positiv sein!") int size) throws ValidationException {
        LOGGER.info("GET /api/v1/favorites");
        Pageable pageable = PageRequest.of(page, size);
        LOGGER.info("Page: " + page + " Size: " + size);
        var count = favoriteService.countFavoritesOfUser(authentication.getName());

        List<RecipeListDto> recipes = recipeMapper.recipeToRecipeListDto(favoriteService.getFavorites(authentication.getName(), pageable));
        for (RecipeListDto r : recipes) {
            r.setFavorite(true);
        }

        return ResponseEntity.ok()
            .header("X-Total-Count", String.valueOf(count))
            .body(recipes);
    }


    /**
     * Add a new recipe to a user's favorites.
     *
     * @param id             of recipe to add
     * @param authentication The user triggering the request
     * @throws ValidationException          if one of the ids is null
     * @throws ObjectAlreadyExistsException if the recipe is already marked as favorite
     */
    @Secured("ROLE_USER")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add recipe to favorites", security = @SecurityRequirement(name = "apiKey"))
    public void addToFavorites(@RequestParam(value = "id") Long id, Authentication authentication) throws ValidationException, ObjectAlreadyExistsException {
        LOGGER.info("POST /api/v1/favorites/{}", id);

        favoriteService.addFavorite(authentication.getName(), id);

    }


    /**
     * Delete a recipe from a user's favorites.
     *
     * @param recipeId       id of recipe, that is to be removed from favorites
     * @param authentication The user triggering the request
     */
    @Secured("ROLE_USER")
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete the recipe from favorites", security = @SecurityRequirement(name = "apiKey"))
    public void delete(@RequestParam(value = "recipeId") Long recipeId, Authentication authentication) {
        LOGGER.info("DELETE /api/v1/favorites/{}", recipeId);
        favoriteService.delete(authentication.getName(), recipeId);
    }

    /**
     * Get the number of favorites for a user in the database.
     *
     * @return Returns the number of favorites
     */
    @Secured("ROLE_USER")
    @GetMapping("/number")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the count of all favorites for a user", security = @SecurityRequirement(name = "apiKey"))
    public long count(Authentication authentication) {
        LOGGER.info("GET /api/v1/favorites/number/");
        return favoriteService.countFavoritesOfUser(authentication.getName());
    }


    /**
     * Returns if the given recipe is included in the user's favorite.
     *
     * @return true, if recipe is in favorites
     */
    @Secured("ROLE_USER")
    @GetMapping("/status/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get if the recipe is in the user's favorites", security = @SecurityRequirement(name = "apiKey"))
    public boolean status(Authentication authentication, @PathVariable Long recipeId) {
        LOGGER.info("GET /api/v1/favorites/status/{}", recipeId);
        return favoriteService.getStatus(authentication.getName(), recipeId);
    }

}
