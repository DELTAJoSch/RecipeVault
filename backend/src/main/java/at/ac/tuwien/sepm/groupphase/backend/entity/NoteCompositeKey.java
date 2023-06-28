package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class NoteCompositeKey implements Serializable {

    public NoteCompositeKey() {
    }

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner", nullable = false)
    private ApplicationUser owner;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "recipe", nullable = false)
    private Recipe recipe;

    public NoteCompositeKey(ApplicationUser owner, Recipe recipe) {
        this.owner = owner;
        this.recipe = recipe;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        result = prime * result + ((recipe == null) ? 0 : recipe.hashCode());
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
        NoteCompositeKey other = (NoteCompositeKey) obj;
        if (owner == null) {
            if (other.owner != null) {
                return false;
            }
        } else if (!owner.equals(other.owner)) {
            return false;
        }
        if (recipe == null) {
            if (other.recipe != null) {
                return false;
            }
        } else if (!recipe.equals(other.recipe)) {
            return false;
        }
        return true;
    }

    public static class NotePrimaryKeyBuilder {

        private ApplicationUser owner;
        private Recipe recipe;

        public NotePrimaryKeyBuilder setOwner(ApplicationUser owner) {
            this.owner = owner;
            return this;
        }

        public NotePrimaryKeyBuilder setRecipe(Recipe recipe) {
            this.recipe = recipe;
            return this;
        }

        public NoteCompositeKey build() {
            return new NoteCompositeKey(owner, recipe);
        }
    }

}
