package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * This dto contains data for the creation of a single recipe.
 */
public class RecipeCreateDto {

    @NotNull(message = "darf nicht null sein")
    @NotBlank(message = "darf nicht leer sein")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String name;

    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String shortDescription;

    @NotNull(message = "darf nicht null sein")
    @NotBlank(message = "darf nicht leer sein")
    @Size(max = 2048, message = "darf nicht länger als 2048 Zeichen sein")
    private String description;

    private UserInfoDto owner;

    @NotNull(message = "darf nicht null sein")
    private Difficulty difficulty;

    @NotNull(message = "darf nicht null sein")
    private List<@NotNull(message = "darf nicht null sein")  @Valid AmountDto> ingredients;
    private AuthorDetailsDto author;

    private Long imageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserInfoDto getOwner() {
        return owner;
    }

    public void setOwner(UserInfoDto owner) {
        this.owner = owner;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<AmountDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<AmountDto> ingredients) {
        this.ingredients = ingredients;
    }

    public AuthorDetailsDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDetailsDto author) {
        this.author = author;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public String toString() {
        return "RecipeDetailsDto {"
            + "name: " + name
            + "owner: " + owner
            + "difficulty" + difficulty
            + "}";
    }

    public static final class RecipeCreateDtoBuilder {
        private String name;
        private String shortDescription;
        private String description;
        private UserInfoDto owner;
        private Difficulty difficulty;
        private List<AmountDto> ingredients;
        private AuthorDetailsDto author;
        private Long imageId;

        public RecipeCreateDtoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RecipeCreateDtoBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeCreateDtoBuilder setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
            return this;
        }

        public RecipeCreateDtoBuilder setOwner(UserInfoDto owner) {
            this.owner = owner;
            return this;
        }

        public RecipeCreateDtoBuilder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public RecipeCreateDtoBuilder setIngredients(List<AmountDto> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeCreateDtoBuilder withAuthor(AuthorDetailsDto author) {
            this.author = author;
            return this;
        }

        public RecipeCreateDtoBuilder withImageId(Long imageId) {
            this.imageId = imageId;
            return this;
        }

        public RecipeCreateDto build() {
            RecipeCreateDto recipe = new RecipeCreateDto();
            recipe.name = this.name;
            recipe.shortDescription = this.shortDescription;
            recipe.description = this.description;
            recipe.owner = this.owner;
            recipe.difficulty = this.difficulty;
            recipe.ingredients = this.ingredients;
            recipe.author = this.author;
            recipe.imageId = this.imageId;
            return recipe;
        }
    }

}

