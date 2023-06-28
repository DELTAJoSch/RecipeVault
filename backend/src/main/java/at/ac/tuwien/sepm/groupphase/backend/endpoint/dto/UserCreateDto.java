package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * dto to create a new user.
 */
public class UserCreateDto {

    @NotBlank(message = "darf nicht leer sein")
    @Email(message = "muss valide email sein")
    @NotNull(message = "muss angegeben werden")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String email;

    @NotBlank(message = "darf nicht leer sein")
    @Size(max = 255, min = 8, message = "darf nicht kürzer als 8 Zeichen und nicht länger als 255 Zeichen sein")
    private String password;

    private boolean isAdmin;

    public UserCreateDto() {
    }

    public UserCreateDto(String email, String password, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserCreateDto that = (UserCreateDto) o;
        return Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

    public static final class UserCreateDtoBuilder {
        private final UserCreateDto dto = new UserCreateDto();

        public UserCreateDtoBuilder withEmail(String email) {
            dto.email = email;
            return this;
        }

        public UserCreateDtoBuilder withPassword(String password) {
            dto.password = password;
            return this;
        }

        public UserCreateDtoBuilder withAdmin(boolean isAdmin) {
            dto.isAdmin = isAdmin;
            return this;
        }

        public UserCreateDto build() {
            return dto;
        }
    }

}
