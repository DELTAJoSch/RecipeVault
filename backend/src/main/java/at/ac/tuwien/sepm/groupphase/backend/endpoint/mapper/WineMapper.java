package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserInfoDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.WineDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Wine;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * This mapper maps wines ot wine entities.
 */
@Mapper
public interface WineMapper {
    /**
     * This needs to be in here because otherwise the mapper can't map the User.
     *
     * @param applicationUser The applicationUser to map
     * @return Returns a new UserInfoDto
     */
    @Mapping(target = "id", source = "applicationUser.id")
    @Mapping(target = "email", source = "applicationUser.email")
    UserInfoDto applicationUserToUserInfoDto(ApplicationUser applicationUser);

    @Named("wineDetails")
    WineDetailsDto wineToWineDetailsDto(Wine wine);

    @IterableMapping(qualifiedByName = "wineDetails")
    List<WineDetailsDto> wineToWineDetailsDto(List<Wine> wine);

}
