package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ApplicationUserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserMappingTest {

    private ApplicationUserMapper mapper = Mappers.getMapper(ApplicationUserMapper.class);

    @Test
    public void applicationUserToUserInfoDto_shouldMapCorrectly() {
        ApplicationUser user = new ApplicationUser.ApplicationUserBuilder()
            .setId(1L)
            .setEmail("user@example.com")
            .setAdmin(false)
            .build();

        UserInfoDto dto = mapper.applicationUserToUserInfoDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getAdmin(), dto.isAdmin());
    }

    @Test
    public void applicationUserToUserDto_shouldMapCorrectly() {
        ApplicationUser user = new ApplicationUser.ApplicationUserBuilder()
            .setId(1L)
            .setEmail("user@example.com")
            .setAdmin(false)
            .build();

        UserInfoDto dto = mapper.applicationUserToUserDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getAdmin(), dto.isAdmin());
    }

    @Test
    public void applicationUserToUserInfoDto_withList_shouldMapCorrectly() {
        ApplicationUser user1 = new ApplicationUser.ApplicationUserBuilder()
            .setId(1L)
            .setEmail("user1@example.com")
            .setAdmin(false)
            .build();

        ApplicationUser user2 = new ApplicationUser.ApplicationUserBuilder()
            .setId(2L)
            .setEmail("user2@example.com")
            .setAdmin(true)
            .build();

        List<ApplicationUser> users = Arrays.asList(user1, user2);

        List<UserInfoDto> dtos = mapper.applicationUserToUserInfoDto(users);

        assertEquals(2, dtos.size());

        UserInfoDto dto1 = dtos.get(0);
        assertEquals(user1.getId(), dto1.getId());
        assertEquals(user1.getEmail(), dto1.getEmail());
        assertEquals(user1.getAdmin(), dto1.isAdmin());

        UserInfoDto dto2 = dtos.get(1);
        assertEquals(user2.getId(), dto2.getId());
        assertEquals(user2.getEmail(), dto2.getEmail());
        assertEquals(user2.getAdmin(), dto2.isAdmin());
    }
}

