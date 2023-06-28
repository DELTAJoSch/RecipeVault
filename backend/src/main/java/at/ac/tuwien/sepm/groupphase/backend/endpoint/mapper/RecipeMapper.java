package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeListDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.RecipeDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import at.ac.tuwien.sepm.groupphase.backend.entity.Recipe;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * This mapper maps recipes ot recipe entities.
 */
@Mapper
public interface RecipeMapper {

    /**
     * This needs to be in here because otherwise the mapper can't map the User.
     *
     * @param applicationUser The applicationUser to map
     * @return Returns a new UserInfoDto
     */
    @Mapping(target = "id", source = "applicationUser.id")
    @Mapping(target = "email", source = "applicationUser.email")
    UserInfoDto applicationUserToUserInfoDto(ApplicationUser applicationUser);

    @Named("recipeDetails")
    @Mapping(target = "recommendedCategory", source = "recipe.recommendedCategory")
    @Mapping(target = "recommendationConfidence", source = "recipe.recommendationConfidence")
    RecipeDetailsDto recipeToRecipeDetailsDto(Recipe recipe);

    @IterableMapping(qualifiedByName = "recipeDetails")
    List<RecipeDetailsDto> recipeToRecipeDetailsDto(List<Recipe> recipe);

    @Named("recipeList")
    RecipeListDto recipeToRecipeListDto(Recipe recipe);

    @IterableMapping(qualifiedByName = "recipeList")
    List<RecipeListDto> recipeToRecipeListDto(List<Recipe> recipe);



}





