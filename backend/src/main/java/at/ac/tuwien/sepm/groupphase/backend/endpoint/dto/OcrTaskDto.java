package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.OcrStep;

/**
 * This dto represents an ocr task.
 */
public class OcrTaskDto {
    private Long id;

    private boolean status;

    private OcrStep step;

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

    public static final class Builder {
        private Long id;
        private boolean status;
        private OcrStep step;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder status(boolean status) {
            this.status = status;
            return this;
        }

        public Builder step(OcrStep step) {
            this.step = step;
            return this;
        }

        public OcrTaskDto build() {
            var dto = new OcrTaskDto();
            dto.status = this.status;
            dto.id = this.id;
            dto.step = this.step;

            return dto;
        }
    }
}
