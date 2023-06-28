package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This dto contains information to update a user's account data.
 */
public class UserUpdateDto {


    private Long id;


    @NotBlank(message = "darf nicht leer sein")
    @Email(message = "muss valide email sein")
    @NotNull(message = "muss angegeben werden")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String email;

    @Size(max = 255, min = 8, message = "darf nicht kürzer als 8 Zeichen und nicht länger als 255 Zeichen sein")
    private String password;

    private Boolean isAdmin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public static final class UserUpdateDtoBuilder {
        private Long id;
        private String email;
        private String password;
        private Boolean isAdmin;

        public UserUpdateDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserUpdateDtoBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserUpdateDtoBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserUpdateDtoBuilder withIsAdmin(Boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public UserUpdateDto build() {
            UserUpdateDto userUpdateDto = new UserUpdateDto();
            userUpdateDto.setId(id);
            userUpdateDto.setEmail(email);
            userUpdateDto.setPassword(password);
            userUpdateDto.setAdmin(isAdmin);
            return userUpdateDto;
        }
    }
}
