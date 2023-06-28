package at.ac.tuwien.sepm.groupphase.backend.entity;


import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

import java.util.List;

import jakarta.persistence.Column;

import java.util.Objects;

@Entity
@Table(name = "users")
public class ApplicationUser {

    /**
     * The id of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The email of the user.
     */
    @Column(name = "email", nullable = false, unique = true, length = 265)
    private String email;

    /**
     * The password of the user.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Specifies whether user is admin or not.
     */
    @Column(name = "admin", nullable = false)
    private Boolean admin = false;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Recipe> ownedRecipes;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Wine> ownedWines;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(name = "favorites",
        joinColumns = @JoinColumn(name = "userId"),
        inverseJoinColumns = @JoinColumn(name = "recipeId"))
    private List<Recipe> favorite;

    /**
     * Notes were written by this user.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Note> ownedNotes;

    /**
     * Comments were written by this user.
     */
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL)
    private List<Comment> createdComments;

    /**
     * This user created lists.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RecipeList> lists;

    public ApplicationUser() {
    }

    public ApplicationUser(String email, String password, Boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getAdmin(), that.getAdmin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getPassword(), getAdmin());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public static final class ApplicationUserBuilder {
        private Long id;
        private String email;
        private String password;
        private Boolean admin = false;

        public ApplicationUserBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ApplicationUserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public ApplicationUserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public ApplicationUserBuilder setAdmin(Boolean admin) {
            this.admin = admin;
            return this;
        }

        public ApplicationUser build() {
            ApplicationUser user = new ApplicationUser();
            user.id = this.id;
            user.email = this.email;
            user.password = this.password;
            user.admin = this.admin;
            return user;
        }

    }

}

