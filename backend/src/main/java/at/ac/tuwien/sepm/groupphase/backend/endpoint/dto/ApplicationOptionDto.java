package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationOptionType;
import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Size;

/**
 * Dto to send ApplicationOptions.
 */
public class ApplicationOptionDto {
    @NonNull
    @Size(min = 1, max = 255, message = "name length has to be shorter than 255")
    private String name;

    private String defaultValue;

    private String value;

    private Long id;

    private ApplicationOptionType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApplicationOptionType getType() {
        return type;
    }

    public void setType(ApplicationOptionType type) {
        this.type = type;
    }

    public static final class ApplicationOptionBuilder {
        private String name;
        private String defaultValue;
        private String value;
        private Long id;
        private ApplicationOptionType type;

        public ApplicationOptionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ApplicationOptionBuilder withDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public ApplicationOptionBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        public ApplicationOptionBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ApplicationOptionBuilder withOptionType(ApplicationOptionType optionType) {
            this.type = optionType;
            return this;
        }

        public ApplicationOptionDto build() {
            var dto = new ApplicationOptionDto();
            dto.setDefaultValue(defaultValue);
            dto.setId(id);
            dto.setType(type);
            dto.setName(name);
            dto.setValue(value);
            return dto;
        }
    }

}
