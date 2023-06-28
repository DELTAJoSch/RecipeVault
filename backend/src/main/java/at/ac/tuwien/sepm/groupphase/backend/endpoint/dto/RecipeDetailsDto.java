package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * This dto contains data for viewing and handling an existing recipe.
 */
public class RecipeDetailsDto {
    @NotNull(message = "darf nicht null sein")
    private Long id;

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

    private NoteDto note;
    private Boolean favorite;
    
    @NotNull(message = "Rezept braucht einen Besitzer")
    private UserInfoDto owner;

    @NotNull(message = "darf nicht null sein")
    private Difficulty difficulty;
    
    private Double recommendationConfidence;
    private WineCategory recommendedCategory;
    private List<AmountDto> ingredients;
    private AuthorDetailsDto author;
    private Long imageId;

    public Double getRecommendationConfidence() {
        return recommendationConfidence;
    }

    public void setRecommendationConfidence(Double recommendationConfidence) {
        this.recommendationConfidence = recommendationConfidence;
    }

    public WineCategory getRecommendedCategory() {
        return recommendedCategory;
    }

    public void setRecommendedCategory(WineCategory recommendedCategory) {
        this.recommendedCategory = recommendedCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public NoteDto getNote() {
        return note;
    }

    public void setNote(NoteDto note) {
        this.note = note;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
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
                + "difficulty: " + difficulty
                + "short description: " + shortDescription
                + "note: " + note
                + "}";
    }

    public static final class RecipeDetailsDtoBuilder {
        private Long id;
        private String name;
        private String shortDescription;
        private String description;
        private NoteDto note;
        private Boolean favorite;
        private UserInfoDto owner;
        private Difficulty difficulty;
        private List<AmountDto> ingredients;
        private Double recommendationConfidence;
        private WineCategory recommendedCategory;
        private AuthorDetailsDto author;
        private Long imageId;

        public RecipeDetailsDtoBuilder withRecommendationConfidence(Double confidence) {
            this.recommendationConfidence = confidence;
            return this;
        }

        public RecipeDetailsDtoBuilder withRecommendedCategory(WineCategory category) {
            this.recommendedCategory = category;
            return this;
        }

        public RecipeDetailsDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public RecipeDetailsDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeDetailsDtoBuilder withNote(NoteDto note) {
            this.note = note;
            return this;
        }

        public RecipeDetailsDtoBuilder withFavorite(Boolean favorite) {
            this.favorite = favorite;
            return this;
        }

        public RecipeDetailsDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeDetailsDtoBuilder withShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
            return this;
        }

        public RecipeDetailsDtoBuilder withOwner(UserInfoDto owner) {
            this.owner = owner;
            return this;
        }

        public RecipeDetailsDtoBuilder withDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public RecipeDetailsDtoBuilder withIngredients(List<AmountDto> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeDetailsDtoBuilder withAuthor(AuthorDetailsDto author) {
            this.author = author;
            return this;
        }

        public RecipeDetailsDtoBuilder withImageId(Long id) {
            this.imageId = imageId;
            return this;
        }

        public RecipeDetailsDto build() {
            RecipeDetailsDto recipe = new RecipeDetailsDto();
            recipe.id = this.id;
            recipe.recommendationConfidence = this.recommendationConfidence;
            recipe.recommendedCategory = this.recommendedCategory;
            recipe.name = this.name;
            recipe.description = this.description;
            recipe.shortDescription = this.shortDescription;
            recipe.owner = this.owner;
            recipe.note = this.note;
            recipe.favorite = this.favorite;
            recipe.difficulty = this.difficulty;
            recipe.ingredients = this.ingredients;
            recipe.author = this.author;
            recipe.imageId = this.imageId;
            return recipe;
        }
    }

}

