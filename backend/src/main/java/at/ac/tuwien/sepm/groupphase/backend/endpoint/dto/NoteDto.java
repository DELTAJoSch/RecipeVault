package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This dto holds the content of a users note for a given recipe.
 */
public class NoteDto {

    private Long ownerId;
    @NotNull
    private Long recipeId;

    @Size(min = 1, max = 2047, message = "message length has to be shorter than 2048")
    @NotNull
    @NotBlank
    private String content;

    public NoteDto() {
    }

    public NoteDto(@NotNull Long ownerId, @NotNull Long recipeId, @NotNull @NotBlank String content) {
        this.ownerId = ownerId;
        this.recipeId = recipeId;
        this.content = content;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NoteDto [ownerId=" + ownerId + ", RecipeId=" + recipeId + ", content=" + content + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
        result = prime * result + ((recipeId == null) ? 0 : recipeId.hashCode());
        result = prime * result + ((content == null) ? 0 : content.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        NoteDto other = (NoteDto) obj;
        if (ownerId == null) {
            if (other.ownerId != null) {
                return false;
            }
        } else if (!ownerId.equals(other.ownerId)) {
            return false;
        }
        if (recipeId == null) {
            if (other.recipeId != null) {
                return false;
            }
        } else if (!recipeId.equals(other.recipeId)) {
            return false;
        }
        if (content == null) {
            if (other.content != null) {
                return false;
            }
        } else if (!content.equals(other.content)) {
            return false;
        }
        return true;
    }

    public static final class NoteDtoBuilder {
        private Long ownerId;
        private Long recipeId;
        private String content;

        public NoteDtoBuilder setOwnerId(Long ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public NoteDtoBuilder setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public NoteDtoBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public NoteDto build() {
            return new NoteDto(ownerId, recipeId, content);
        }
    }

}
