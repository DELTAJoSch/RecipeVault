package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import jakarta.validation.constraints.NotNull;

/**
 * This dto contains the data necessary to transform an image sent previously.
 */
public class ImageTransformDto {
    @NotNull
    Integer topLeftX;
    @NotNull
    Integer topLeftY;

    @NotNull
    Integer topRightX;
    @NotNull
    Integer topRightY;

    @NotNull
    Integer bottomRightX;
    @NotNull
    Integer bottomRightY;

    @NotNull
    Integer bottomLeftX;
    @NotNull
    Integer bottomLeftY;

    public Integer getTopLeftX() {
        return topLeftX;
    }

    public void setTopLeftX(Integer topLeftX) {
        this.topLeftX = topLeftX;
    }

    public Integer getTopLeftY() {
        return topLeftY;
    }

    public void setTopLeftY(Integer topLeftY) {
        this.topLeftY = topLeftY;
    }

    public Integer getTopRightX() {
        return topRightX;
    }

    public void setTopRightX(Integer topRightX) {
        this.topRightX = topRightX;
    }

    public Integer getTopRightY() {
        return topRightY;
    }

    public void setTopRightY(Integer topRightY) {
        this.topRightY = topRightY;
    }

    public Integer getBottomRightX() {
        return bottomRightX;
    }

    public void setBottomRightX(Integer bottomRightX) {
        this.bottomRightX = bottomRightX;
    }

    public Integer getBottomRightY() {
        return bottomRightY;
    }

    public void setBottomRightY(Integer bottomRightY) {
        this.bottomRightY = bottomRightY;
    }

    public Integer getBottomLeftX() {
        return bottomLeftX;
    }

    public void setBottomLeftX(Integer bottomLeftX) {
        this.bottomLeftX = bottomLeftX;
    }

    public Integer getBottomLeftY() {
        return bottomLeftY;
    }

    public void setBottomLeftY(Integer bottomLeftY) {
        this.bottomLeftY = bottomLeftY;
    }


    public static class Builder {
        private Integer topLeftX;
        private Integer topLeftY;
        private Integer topRightX;
        private Integer topRightY;
        private Integer bottomRightX;
        private Integer bottomRightY;
        private Integer bottomLeftX;
        private Integer bottomLeftY;

        public Builder topLeftX(Integer topLeftX) {
            this.topLeftX = topLeftX;
            return this;
        }

        public Builder topLeftY(Integer topLeftY) {
            this.topLeftY = topLeftY;
            return this;
        }

        public Builder topRightX(Integer topRightX) {
            this.topRightX = topRightX;
            return this;
        }

        public Builder topRightY(Integer topRightY) {
            this.topRightY = topRightY;
            return this;
        }

        public Builder bottomRightX(Integer bottomRightX) {
            this.bottomRightX = bottomRightX;
            return this;
        }

        public Builder bottomRightY(Integer bottomRightY) {
            this.bottomRightY = bottomRightY;
            return this;
        }

        public Builder bottomLeftX(Integer bottomLeftX) {
            this.bottomLeftX = bottomLeftX;
            return this;
        }

        public Builder bottomLeftY(Integer bottomLeftY) {
            this.bottomLeftY = bottomLeftY;
            return this;
        }

        public ImageTransformDto build() {
            var dto = new ImageTransformDto();
            dto.bottomLeftX = this.bottomLeftX;
            dto.bottomLeftY = this.bottomLeftY;
            dto.bottomRightX = this.bottomRightX;
            dto.bottomRightY = this.bottomRightY;
            dto.topLeftX = this.topLeftX;
            dto.topLeftY = this.topLeftY;
            dto.topRightX = this.topRightX;
            dto.topRightY = this.topRightY;

            return dto;
        }
    }
}
