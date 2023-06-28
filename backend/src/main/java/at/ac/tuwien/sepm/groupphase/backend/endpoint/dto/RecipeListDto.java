package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;

import java.util.Objects;

/**
 * This dto contains short information for displaying only cursory content of a recipe.
 */
public class RecipeListDto {
    private Long id;
    private String name;
    private Difficulty difficulty;
    private String shortDescription;
    private Boolean favorite;
    private UserInfoDto owner;

    private Long imageId;

    public RecipeListDto() {
    }

    public RecipeListDto(Long id, String name, Difficulty difficulty, String shortDescription, Boolean favorite, UserInfoDto owner, Long imageId) {
        this.id = id;
        this.name = name;
        this.difficulty = difficulty;
        this.shortDescription = shortDescription;
        this.favorite = favorite;
        this.owner = owner;
        this.imageId = imageId;
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public UserInfoDto getOwner() {
        return owner;
    }

    public void setOwner(UserInfoDto owner) {
        this.owner = owner;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RecipeListDto that = (RecipeListDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && difficulty == that.difficulty
            && Objects.equals(shortDescription, that.shortDescription) && Objects.equals(favorite, that.favorite) && Objects.equals(owner, that.owner) && Objects.equals(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, difficulty, shortDescription, favorite, owner, imageId);
    }

    @Override
    public String toString() {
        return "RecipeListDto{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", difficulty=" + difficulty
            + ", shortDescription='" + shortDescription + '\''
            + ", favorite=" + favorite
            + ", owner=" + owner
            + ", imageId=" + imageId
            + '}';
    }

}
