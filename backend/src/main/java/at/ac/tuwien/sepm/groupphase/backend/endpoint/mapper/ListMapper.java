package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ListOverviewDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.RecipeList;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper to map lists of recipelists to lists of listoverviewdto's.
 */
@Mapper
public interface ListMapper {
    List<ListOverviewDto> recipeListToListOverviewDto(List<RecipeList> recipeLists);
}
