package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;
import java.util.Objects;

/**
 * Author Entity.
 */
@Entity
@Table(name = "authors", uniqueConstraints = {@UniqueConstraint(columnNames = {"firstname", "lastname"})})
public class Author {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * The firstname of the author.
     */
    @Column(name = "firstname", nullable = false)
    private String firstname;

    /**
     * The lastname of the author.
     */
    @Column(name = "lastname", nullable = false)
    private String lastname;

    /**
     * The description of the author.
     */
    @Column(name = "description", length = 4048)
    private String description;

    /**
     * Recipe was written/created by this author.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author", cascade = CascadeType.REMOVE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Recipe> recipes;


    /**
     * Image of the author.
     */
    @Column(name = "imageId")
    private Long imageId;

    public Author() {
    }

    public Author(String firstname, String lastname, String description) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
    }



    public Author(String firstname, String lastname, Long imageId, String description) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
        this.imageId = imageId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Author author = (Author) o;
        return Objects.equals(getId(), author.getId())
            && Objects.equals(getFirstname(), author.getFirstname())
            && Objects.equals(getLastname(), author.getLastname())
            && Objects.equals(getDescription(), author.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstname(), getLastname(), getDescription());
    }

    public Long getId() {
        return id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public static final class AuthorBuilder {
        private Long id;
        private String firstname;
        private String lastname;
        private String description;
        private Long imageId;

        public Author.AuthorBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public Author.AuthorBuilder setFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Author.AuthorBuilder setLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Author.AuthorBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Author.AuthorBuilder setImageId(Long imageId) {
            this.imageId = imageId;
            return this;
        }

        public Author build() {
            Author author = new Author();
            author.id = this.id;
            author.firstname = this.firstname;
            author.lastname = this.lastname;
            author.description = this.description;
            author.imageId = this.imageId;
            return author;
        }
    }
}
