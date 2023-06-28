package at.ac.tuwien.sepm.groupphase.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * This entity represents a task associated with an OCR request.
 */
@Entity
@Table(name = "ocr_tasks")
public class OcrTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "step", nullable = false)
    @Enumerated(EnumType.STRING)
    private OcrStep step;

    /**
     * The filename for the initially uploaded file.
     */
    @Column(name = "name")
    @JsonIgnore
    private String name;

    /**
     * Used by the system to clean up orphaned tasks that are older than a day.
     * The cleaning task should run every day.
     */
    @Basic
    @Column(name = "creation_date", nullable = false)
    @JsonIgnore
    private java.time.ZonedDateTime creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public OcrStep getStep() {
        return step;
    }

    public void setStep(OcrStep step) {
        this.step = step;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OcrTask ocrTask = (OcrTask) o;
        return isStatus() == ocrTask.isStatus() && Objects.equals(getId(), ocrTask.getId()) && getStep() == ocrTask.getStep() && Objects.equals(getCreationDate(), ocrTask.getCreationDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), isStatus(), getStep(), getCreationDate());
    }


    public static final class OcrTaskBuilder {
        private Long id;
        private boolean status;
        private OcrStep step;
        private ZonedDateTime creationDate;
        private String name;

        public OcrTaskBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public OcrTaskBuilder setStatus(boolean status) {
            this.status = status;
            return this;
        }

        public OcrTaskBuilder setStep(OcrStep step) {
            this.step = step;
            return this;
        }

        public OcrTaskBuilder setCreationDate(ZonedDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public OcrTaskBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public OcrTask build() {
            OcrTask ocrTask = new OcrTask();
            ocrTask.setId(id);
            ocrTask.setStatus(status);
            ocrTask.setStep(step);
            ocrTask.setCreationDate(creationDate);
            ocrTask.setName(name);
            return ocrTask;
        }
    }
}
