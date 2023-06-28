package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.IdClass;
import jakarta.persistence.Entity;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


import java.util.List;

@Entity
@IdClass(RecipeListCompositeKey.class)
@Table(name = "lists", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "name"})})


public class RecipeList {
    /**
     * The ID of the user.
     */
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private ApplicationUser user;

    /**
     * The name of the list.
     */
    @Id
    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "userId"),
        @JoinColumn(name = "name")
    })
    private List<Recipe> recipes;


    public RecipeList() {
    }

    public RecipeList(ApplicationUser user, List<Recipe> recipe, String name) {
        this.user = user;
        this.recipes = recipe;
        this.name = name;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public List<Recipe> getRecipe() {
        return recipes;
    }

    public void setRecipe(List<Recipe> recipe) {
        this.recipes = recipe;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class RecipeListBuilder {
        private List<Recipe> recipe;
        private ApplicationUser user;

        private String name;

        public RecipeListBuilder() {
        }

        public RecipeListBuilder withRecipe(List<Recipe> recipe) {
            this.recipe = recipe;
            return this;
        }

        public RecipeListBuilder withUser(ApplicationUser user) {
            this.user = user;
            return this;
        }

        public RecipeListBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public RecipeList build() {
            RecipeList list = new RecipeList();
            list.user = this.user;
            list.recipes = this.recipe;
            list.name = this.name;
            return list;
        }

    }

}
