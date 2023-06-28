package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CommentDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper that maps Comments to CommentDtos.
 */
@Mapper
public interface CommentMapper {

    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "recipeId", source = "recipe.id")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "recipeId", source = "recipe.id")
    List<CommentDto> commentToCommentDto(List<Comment> comments);
}
