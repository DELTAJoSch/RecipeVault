package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@IdClass(CommentPrimaryKey.class)
@Table(name = "comments")
public class Comment {

    /**
     * ID of the comment creator.
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "creator")
    private ApplicationUser creator;

    /**
     * ID of the recipe.
      */
    @Id
    @ManyToOne
    @JoinColumn(name = "recipe")
    private Recipe recipe;

    /**
     * Date and time of when the comment was posted.
     */
    @Id
    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    /**
     * Content of the comment.
     */
    @Column(name = "content", length = 2048)
    private String content;

    public Comment(ApplicationUser creator, Recipe recipe, LocalDateTime dateTime, String content) {
        this.creator = creator;
        this.recipe = recipe;
        this.dateTime = dateTime;
        this.content = content;
    }

    public Comment() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment that = (Comment) o;
        return Objects.equals(getCreator(), that.getCreator()) && Objects.equals(getRecipe(), that.getRecipe()) && Objects.equals(getDateTime(), that.getDateTime())
            && Objects.equals(getContent(), that.getContent());
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

    public void setDateTime(LocalDateTime date) {
        this.dateTime = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment [user=" + creator + ", recipe=" + recipe + ", date=" + dateTime.toString() + ", content=" + content + "]";
    }

    public static final class CommentBuilder {

        private ApplicationUser creator;
        private Recipe recipe;
        private LocalDateTime dateTime;
        private String content;

        public CommentBuilder setCreator(ApplicationUser creator) {
            this.creator = creator;
            return this;
        }

        public CommentBuilder setRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public CommentBuilder setDate(LocalDateTime dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public CommentBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public Comment build() {
            return new Comment(creator, recipe, dateTime, content);
        }
    }
}