package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.Difficulty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a search for a recipe.
 */
public class RecipeSearchDto {
    
    @NotNull(message = "muss gesetzt sein")
    @Size(max = 255, message = " darf nicht länger als 256 Zeichen sein")
    private String name;

    private Difficulty difficulty;

    public String getName() {
        return name;
    }

    /**
     * Sets the name. This parameter should be contained in the name of results.
     *
     * @param name If set to null, this parameter is ignored in the search
     */
    public void setName(String name) {
        this.name = name;
    }


    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Sets the difficulty.
     *
     * @param difficulty If set to null, this parameter is ignored in the search
     */
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }


    public static final class RecipeSearchDtoBuilder {

        private String name;
        private Difficulty difficulty;

        public RecipeSearchDtoBuilder() {
            // default constructor
        }

        public RecipeSearchDtoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RecipeSearchDtoBuilder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public RecipeSearchDto build() {
            var dto = new RecipeSearchDto();
            dto.name = name;
            dto.difficulty = difficulty;

            return dto;
        }
    }


}


