package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OcrTaskDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.OcrTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps OcrTask Entities to OcrTaskDtos.
 */
@Mapper
public interface OcrTaskMapper {
    @Mapping(target = "id", source = "ocrTask.id")
    @Mapping(target = "step", source = "ocrTask.step")
    @Mapping(target = "status", source = "ocrTask.status")
    OcrTaskDto ocrTaskToOcrTaskDto(OcrTask ocrTask);
}
