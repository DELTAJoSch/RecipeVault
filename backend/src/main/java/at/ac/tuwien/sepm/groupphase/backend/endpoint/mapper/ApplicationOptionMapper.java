package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationOptionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOption;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper that maps ApplicationOptions to ApplicationOptionDtos.
 */
@Mapper
public interface ApplicationOptionMapper {
    @Named("applicationOption")
    @Mapping(source = "type", target = "type")
    ApplicationOptionDto applicationOptionToApplicationOptionDto(ApplicationOption option);

    @IterableMapping(qualifiedByName = "applicationOption")
    List<ApplicationOptionDto> applicationOptionToApplicationOptionDto(List<ApplicationOption> option);
}
