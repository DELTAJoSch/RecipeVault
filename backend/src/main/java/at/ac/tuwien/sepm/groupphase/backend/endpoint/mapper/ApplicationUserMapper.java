package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Map a ApplicationUser to related DTOs.
 */
@Mapper
public interface ApplicationUserMapper {

    @Mapping(target = "id", source = "applicationUser.id")
    @Mapping(target = "email", source = "applicationUser.email")
    @Named("userInfo")
    UserInfoDto applicationUserToUserInfoDto(ApplicationUser applicationUser);

    @IterableMapping(qualifiedByName = "userInfo")
    List<UserInfoDto> applicationUserToUserInfoDto(List<ApplicationUser> applicationUser);

    @Mapping(target = "id", source = "applicationUser.id")
    @Mapping(target = "email", source = "applicationUser.email")
    UserInfoDto applicationUserToUserDto(ApplicationUser applicationUser);
}
