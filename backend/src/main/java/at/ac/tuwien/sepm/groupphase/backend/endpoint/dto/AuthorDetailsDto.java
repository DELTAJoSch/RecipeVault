package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This DTO contains all information about a specific author.
 */
public class AuthorDetailsDto {
    @NotNull(message = "darf nicht null sein")
    private Long id;

    @NotBlank(message = "Ein Autor Vorname kann nicht leer sein")
    @NotNull(message = "Ein Autor braucht einen Vornamen")
    @Size(max = 255, message = "Ein Autorenvorname ist maximal 255 Zeichen lang")
    private String firstname;


    @NotBlank(message = "Ein Autor Nachname kann nicht leer sein")
    @NotNull(message = "Ein Autor braucht einen Nachnamen")
    @Size(max = 255, message = "Ein Autorennachname ist maximal 255 Zeichen lang")
    private String lastname;

    @Size(max = 4048, message = "Eine Autorenbeschreibung ist maximal 4048 Zeichen lang")
    private String description;

    private Long imageId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public String toString() {
        return "AuthorDetailsDto {"
            + "firstname: " + firstname
            + "lastname: " + lastname
            + "}";
    }

    public static final class AuthorDetailsDtoBuilder {
        private Long id;
        private String firstname;
        private String lastname;
        private String description;
        private Long imageId;

        public AuthorDetailsDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AuthorDetailsDtoBuilder withFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public AuthorDetailsDtoBuilder withLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public AuthorDetailsDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public AuthorDetailsDtoBuilder withImageId(Long imageId) {
            this.imageId = imageId;
            return this;
        }

        public AuthorDetailsDto build() {
            AuthorDetailsDto author = new AuthorDetailsDto();
            author.id = this.id;
            author.firstname = this.firstname;
            author.lastname = this.lastname;
            author.description = this.description;
            author.imageId = this.imageId;
            return author;
        }
    }
}
