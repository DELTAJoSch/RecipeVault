package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.OcrTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Repository for OcrTasks.
 */
public interface OcrTaskRepository extends JpaRepository<OcrTask, Long> {
    List<OcrTask> findByCreationDateLessThanEqual(ZonedDateTime creationDate);
}
