package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * The user's most essential information. Used to track the owner of recipes and
 * wines.
 */
public class UserInfoDto {
    @NotNull
    private Long id;

    @NotNull
    private String email;

    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfoDto that = (UserInfoDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail());
    }

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

    public static final class UserInfoDtoBuilder {
        private Long id;
        private String name;
        private boolean isAdmin;

        public UserInfoDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserInfoDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public UserInfoDtoBuilder withAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
            return this;
        }

        public UserInfoDto build() {
            UserInfoDto user = new UserInfoDto();
            user.id = id;
            user.email = name;
            user.isAdmin = isAdmin;
            return user;
        }
    }
}