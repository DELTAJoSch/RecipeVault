package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(NoteCompositeKey.class)
public class Note {

    @Id
    private ApplicationUser owner;

    @Id
    private Recipe recipe;

    @Column(name = "content", length = 2048)
    private String content;

    public Note(ApplicationUser owner, Recipe recipe, String content) {
        this.owner = owner;
        this.recipe = recipe;
        this.content = content;
    }

    public Note() {
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Note [owner=" + owner + ", recipe=" + recipe + ", content=" + content + "]";
    }

    public static final class NoteBuilder {

        private ApplicationUser owner;
        private Recipe recipe;
        private String content;

        public NoteBuilder setOwner(ApplicationUser owner) {
            this.owner = owner;
            return this;
        }

        public NoteBuilder setRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public NoteBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public Note build() {
            return new Note(owner, recipe, content);
        }
    }
}
