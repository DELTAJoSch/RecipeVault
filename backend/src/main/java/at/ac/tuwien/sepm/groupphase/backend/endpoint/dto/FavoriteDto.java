package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.Objects;

/**
 * This dto encapsulates a single favorite relationship.
 */
public class FavoriteDto {
    private RecipeDetailsDto recipe;
    private UserInfoDto user;

    public FavoriteDto() {
    }

    @Override
    public String toString() {
        return "FavoriteDto{"
            + "recipe=" + recipe
            + ", user=" + user
            + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteDto that = (FavoriteDto) o;
        return Objects.equals(recipe, that.recipe) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, user);
    }

    public FavoriteDto(RecipeDetailsDto recipe, UserInfoDto user) {
        this.recipe = recipe;
        this.user = user;
    }

    public RecipeDetailsDto getRecipe() {
        return recipe;
    }

    public void setRecipe(RecipeDetailsDto recipe) {
        this.recipe = recipe;
    }

    public UserInfoDto getUser() {
        return user;
    }

    public void setUser(UserInfoDto user) {
        this.user = user;
    }

    public static final class FavoriteDtoBuilder {
        private RecipeDetailsDto recipe;
        private UserInfoDto user;


        public FavoriteDtoBuilder() {
        }

        public FavoriteDtoBuilder withRecipe(RecipeDetailsDto recipe) {
            this.recipe = recipe;
            return this;
        }

        public FavoriteDtoBuilder withUser(UserInfoDto user) {
            this.user = user;
            return this;
        }

        public static FavoriteDtoBuilder anDtoBuilder() {
            return new FavoriteDtoBuilder();
        }

        public FavoriteDto build() {
            FavoriteDto favoriteDto = new FavoriteDto();
            favoriteDto.setUser(user);
            favoriteDto.setRecipe(recipe);
            return favoriteDto;
        }
    }

}
