package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * DTO for sign up of a new user, creation of new account.
 */
public class UserSignUpDto {
   
    @NotBlank(message = "darf nicht leer sein")
    @Email(message = "muss valide email sein")
    @NotNull(message = "muss angegeben werden")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String email;

    @NotNull(message = "muss angegeben werden")
    @NotEmpty(message = "darf nicht leer sein")
    @Size(max = 255, min = 8, message = "darf nicht kürzer als 8 Zeichen und nicht länger als 255 Zeichen sein")
    private String password;



    public UserSignUpDto() {

    }

    public UserSignUpDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSignUpDto that = (UserSignUpDto) o;
        return Objects.equals(getEmail(), that.getEmail());
    }

    public int hashCode() {
        return Objects.hash(getEmail());
    }

    public static class UserSignUpDtoBuilder {
        private final UserSignUpDto dto = new UserSignUpDto();

        public UserSignUpDto.UserSignUpDtoBuilder withEmail(String email) {
            dto.email = email;
            return this;
        }

        public UserSignUpDto.UserSignUpDtoBuilder withPassword(String password) {
            dto.password = password;
            return this;
        }

        public UserSignUpDto build() {
            return dto;
        }
    }
}
