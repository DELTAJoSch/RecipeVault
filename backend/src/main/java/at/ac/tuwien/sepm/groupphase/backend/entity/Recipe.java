package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "recipes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "description"})})
public class Recipe {


    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "shortDescription")
    private String shortDescription;

    @Column(name = "description", length = 256 * 8)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "owner", nullable = false)
    private ApplicationUser owner;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Amount> ingredient;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommended_category")
    private WineCategory recommendedCategory;

    @Column(name = "recommendation_confidence")
    private Double recommendationConfidence;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Note> notes;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "recipes")
    private List<RecipeList> recipeLists;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Favorite> favorites;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "recipe", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @Column(name = "imageId")
    private Long imageId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author")
    private Author author;

    public Recipe() {
    }

    public Recipe(String name, String shortDescription, String description, ApplicationUser owner, Difficulty difficulty, List<Amount> ingredient) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.owner = owner;
        this.difficulty = difficulty;
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return Objects.equals(getId(), recipe.getId()) && Objects.equals(getName(), recipe.getName())
            && Objects.equals(getDescription(), recipe.getDescription())
            && Objects.equals(getShortDescription(), recipe.getShortDescription())
            && getDifficulty() == recipe.getDifficulty() && getRecommendedCategory() == recipe.getRecommendedCategory()
            && Objects.equals(getRecommendationConfidence(), recipe.getRecommendationConfidence());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getShortDescription(), getDescription(), getDifficulty(), getRecommendedCategory(), getRecommendationConfidence());
    }

    public List<RecipeList> getRecipeLists() {
        return recipeLists;
    }

    public void setRecipeLists(List<RecipeList> recipeLists) {
        this.recipeLists = recipeLists;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public List<Amount> getIngredient() {
        return ingredient;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIngredient(Amount amount) {
        this.ingredient.add(amount);
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long image) {
        this.imageId = image;
    }

    /**
     * Get the owner / creator of this recipe, i.e. the Account which can edit the recipe.
     *
     * @return Returns the associated user account.
     */
    public ApplicationUser getOwner() {
        return owner;
    }

    /**
     * Set the owner / creator of this recipe, i.e. the Account which can edit the recipe.
     */
    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<Amount> getIngredients() {
        return ingredient;
    }

    public void setIngredients(List<Amount> ingredient) {
        this.ingredient = ingredient;
    }

    public Long getId() {
        return id;
    }

    public WineCategory getRecommendedCategory() {
        return recommendedCategory;
    }

    public void setRecommendedCategory(WineCategory recommendedCategory) {
        this.recommendedCategory = recommendedCategory;
    }

    public Double getRecommendationConfidence() {
        return recommendationConfidence;
    }

    public void setRecommendationConfidence(Double recommendationConfidence) {
        this.recommendationConfidence = recommendationConfidence;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public static final class RecipeBuilder {
        private Long id;
        private String name;
        private String shortDescription;
        private String description;
        private ApplicationUser owner;
        private Difficulty difficulty;
        private List<Amount> ingredients;
        private WineCategory recommendedCategory;
        private Double recommendationConfidence;
        private Long imageId;

        public RecipeBuilder setRecommendedCategory(WineCategory recommendedCategory) {
            this.recommendedCategory = recommendedCategory;
            return this;
        }

        public RecipeBuilder setConfidence(Double confidence) {
            this.recommendationConfidence = confidence;
            return this;
        }

        public RecipeBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public RecipeBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RecipeBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RecipeBuilder setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
            return this;
        }

        public RecipeBuilder setOwner(ApplicationUser owner) {
            this.owner = owner;
            return this;
        }

        public RecipeBuilder setDifficulty(Difficulty difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public RecipeBuilder setIngredients(List<Amount> ingredients) {
            this.ingredients = ingredients;
            return this;
        }

        public RecipeBuilder setImageId(Long id) {
            this.imageId = imageId;
            return this;
        }

        public Recipe build() {
            Recipe recipe = new Recipe();
            recipe.id = this.id;
            recipe.name = this.name;
            recipe.shortDescription = this.shortDescription;
            recipe.description = this.description;
            recipe.owner = this.owner;
            recipe.difficulty = this.difficulty;
            recipe.ingredient = this.ingredients;
            recipe.recommendedCategory = this.recommendedCategory;
            recipe.recommendationConfidence = this.recommendationConfidence;
            recipe.imageId = this.imageId;
            return recipe;
        }
    }
}
