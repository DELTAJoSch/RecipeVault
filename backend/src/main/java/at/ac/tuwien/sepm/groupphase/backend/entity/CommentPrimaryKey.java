package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Primary Key for the table "Comments", it includes a user id, a recipe id and a localDateTime.
 */
@Embeddable
public class CommentPrimaryKey implements Serializable {

    public CommentPrimaryKey() {
    }

    @JoinColumn(name = "creator", nullable = false)
    private ApplicationUser creator;

    @JoinColumn(name = "recipe", nullable = false)
    private Recipe recipe;

    @JoinColumn(name = "dateTime", nullable = false)
    private LocalDateTime dateTime;

    public CommentPrimaryKey(ApplicationUser creator, Recipe recipe, LocalDateTime dateTime) {
        this.creator = creator;
        this.recipe = recipe;
        this.dateTime = dateTime;
    }

    public ApplicationUser getCreator() {
        return creator;
    }

    public void setCreator(ApplicationUser user) {
        this.creator = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public int hashCode() {
        final int prime = 13;
        int result = 1;
        result = prime * result + ((creator == null) ? 0 : creator.hashCode());
        result = prime * result + ((recipe == null) ? 0 : recipe.hashCode());
        result = prime * result + (((dateTime == null)) ? 0 : dateTime.hashCode());
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
        CommentPrimaryKey other = (CommentPrimaryKey) obj;
        if (creator == null) {
            if (other.creator != null) {
                return false;
            }
        } else if (!creator.equals(other.creator)) {
            return false;
        }
        if (recipe == null) {
            if (other.recipe != null) {
                return false;
            }
        } else if (!recipe.equals(other.recipe)) {
            return false;
        }
        if (dateTime == null) {
            return other.dateTime == null;
        } else {
            return dateTime.equals(other.dateTime);
        }
    }

    public static class CommentPrimaryKeyBuilder {

        private ApplicationUser creator;
        private Recipe recipe;
        private LocalDateTime dateTime;

        public CommentPrimaryKeyBuilder setCreator(ApplicationUser creator) {
            this.creator = creator;
            return this;
        }

        public CommentPrimaryKeyBuilder setRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public CommentPrimaryKeyBuilder setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public CommentPrimaryKey build() {
            return new CommentPrimaryKey(creator, recipe, dateTime);
        }
    }

}

