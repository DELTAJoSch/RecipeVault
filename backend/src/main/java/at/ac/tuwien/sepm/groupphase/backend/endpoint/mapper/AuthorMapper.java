package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthorDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Author;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

/**
 * This mapper maps authors entities to authors.
 */
@Mapper
public interface AuthorMapper {
    @Named("authorDetails")
    AuthorDetailsDto authorToAuthorDetailsDto(Author author);

    @IterableMapping(qualifiedByName = "authorDetails")
    List<AuthorDetailsDto> authorToAuthorDetailsDto(List<Author> author);
}
