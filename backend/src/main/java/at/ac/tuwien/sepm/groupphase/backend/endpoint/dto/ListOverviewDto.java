package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

/**
 * This DTO contains information for a list entry.
 */
public class ListOverviewDto {
    @NotNull
    private String name;

    @NotNull
    private UserInfoDto user;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserInfoDto getUser() {
        return user;
    }

    public void setUser(UserInfoDto user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ListDto{"
            + "name='" + name + '\''
            + ", user=" + user
            + '}';
    }

    public static final class ListDtoBuilder {
        private String name;
        private UserInfoDto user;

        public ListDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ListDtoBuilder withUser(UserInfoDto user) {
            this.user = user;
            return this;
        }

        public ListOverviewDto build() {
            var dto = new ListOverviewDto();
            dto.name = this.name;
            dto.user = this.user;

            return dto;
        }

    }
}
