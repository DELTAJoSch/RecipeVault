package at.ac.tuwien.sepm.groupphase.backend.tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import at.ac.tuwien.sepm.groupphase.backend.service.OcrService;

/**
 * Cleaner for OcrTasks.
 */
@Component
public class OcrCleanupTask {

    @Autowired
    private OcrService ocrService;

    /**
     * Clean up all abandoned OCR tasks that are older than one day.
     */
    @Scheduled(cron = "0 0 0 * * *") // Runs every day at midnight!
    public void cleanUpOrphanedOcrTasks() {
        ocrService.clean();
    }
}
