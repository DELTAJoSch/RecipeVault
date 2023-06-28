package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;


/**
 * dto to create new author.
 */
public class AuthorCreateDto {

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


    public AuthorCreateDto() {
    }

    public AuthorCreateDto(String firstname, String lastname, String description) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.description = description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorCreateDto that = (AuthorCreateDto) o;
        return Objects.equals(getFirstname(), that.getFirstname())
            && Objects.equals(getLastname(), that.getLastname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname());
    }

    public static final class AuthorCreateDtoBuilder {
        private final AuthorCreateDto dto = new AuthorCreateDto();

        public AuthorCreateDto.AuthorCreateDtoBuilder withFirstname(String firstname) {
            dto.firstname = firstname;
            return this;
        }

        public AuthorCreateDto.AuthorCreateDtoBuilder withLastname(String lastname) {
            dto.lastname = lastname;
            return this;
        }

        public AuthorCreateDto.AuthorCreateDtoBuilder withDescription(String description) {
            dto.description = description;
            return this;
        }

        public AuthorCreateDto.AuthorCreateDtoBuilder withImageId(Long imageId) {
            dto.imageId = imageId;
            return this;
        }

        public AuthorCreateDto build() {
            return dto;
        }
    }
}
