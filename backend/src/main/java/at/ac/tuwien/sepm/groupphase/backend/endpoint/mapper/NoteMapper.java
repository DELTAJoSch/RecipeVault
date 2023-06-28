package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NoteDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to map notes to NoteDtos.
 */
@Mapper
public interface NoteMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "recipeId", source = "recipe.id")
    NoteDto noteToNoteDto(Note note);
}
