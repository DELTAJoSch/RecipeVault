package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * This dto is used to transfer a single comment.
 */
public class CommentDto {

    private Long creatorId;
    @NotNull
    private Long recipeId;
    private LocalDateTime dateTime;

    @Size(min = 1, max = 2047, message = "comment length has to be shorter than 2048")
    @NotNull
    @NotBlank
    private String content;

    public CommentDto() {
    }

    public CommentDto(@NotNull Long creatorId, @NotNull Long recipeId, LocalDateTime dateTime, @NotNull @NotBlank String content) {
        this.creatorId = creatorId;
        this.recipeId = recipeId;
        this.dateTime = dateTime;
        this.content = content;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NoteDto [ownerId=" + creatorId + ", recipeId=" + recipeId + ", date=" + dateTime.toString() + ", content=" + content + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 13;
        int result = 1;
        result = prime * result + ((creatorId == null) ? 0 : creatorId.hashCode());
        result = prime * result + ((recipeId == null) ? 0 : recipeId.hashCode());
        result = prime * result + (((dateTime == null)) ? 0 : dateTime.hashCode());
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
        CommentDto other = (CommentDto) obj;
        if (creatorId == null) {
            if (other.creatorId != null) {
                return false;
            }
        } else if (!creatorId.equals(other.creatorId)) {
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
        if (dateTime == null) {
            return other.dateTime == null;
        } else {
            return dateTime.equals(other.dateTime);
        }
    }

    public static final class CommentDtoBuilder {
        private Long creatorId;
        private Long recipeId;
        private LocalDateTime dateTime;
        private String content;

        public CommentDtoBuilder setCreatorId(Long creatorId) {
            this.creatorId = creatorId;
            return this;
        }

        public CommentDtoBuilder setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
            return this;
        }

        public CommentDtoBuilder setDate(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public CommentDtoBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public CommentDto build() {
            return new CommentDto(creatorId, recipeId, dateTime, content);
        }
    }

}
