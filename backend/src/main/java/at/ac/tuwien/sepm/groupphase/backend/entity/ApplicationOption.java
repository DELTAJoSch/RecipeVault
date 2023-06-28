package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

/**
 * An ApplicationOption. Settable by admins.
 */
@Entity
@Table(name = "application_options")
public class ApplicationOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The name of the option. Must be unique.
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * The value of the option. If null, default value is used.
     */
    @Column(name = "option_value")
    private String value;

    /**
     * The default value of the option.
     */
    @Column(name = "default_value", nullable = false)
    private String defaultValue;

    /**
     * The type of option that is stored.
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationOptionType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApplicationOption that = (ApplicationOption) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getValue(), that.getValue()) && Objects.equals(getDefaultValue(), that.getDefaultValue()) && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getValue(), getDefaultValue(), getType());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ApplicationOptionType getType() {
        return type;
    }

    public void setType(ApplicationOptionType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Builder for ApplicationOption Entity.
     */
    public static final class ApplicationOptionBuilder {

        private Long id;
        private String name;
        private String value;
        private String defaultValue;
        private ApplicationOptionType type;

        public ApplicationOptionBuilder() {
        }

        public ApplicationOptionBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ApplicationOptionBuilder withValue(String value) {
            this.value = value;
            return this;
        }

        public ApplicationOptionBuilder withDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public ApplicationOptionBuilder withType(ApplicationOptionType type) {
            this.type = type;
            return this;
        }

        public ApplicationOptionBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public ApplicationOption build() {
            ApplicationOption applicationOption = new ApplicationOption();
            applicationOption.setId(this.id);
            applicationOption.setName(this.name);
            applicationOption.setValue(this.value);
            applicationOption.setDefaultValue(this.defaultValue);
            applicationOption.setType(this.type);
            return applicationOption;
        }
    }
}
