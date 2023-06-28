package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import org.hibernate.validator.constraints.Range;

import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * This DTO contains all information about a specific wine.
 */
public class WineDetailsDto {
    @NotNull(message = "darf nicht null sein")
    private Long id;

    @NotBlank(message = "darf nicht leer sein")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String name;

    @NotBlank(message = "darf nicht leer sein")
    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String vinyard;

    @Size(max = 2048, message = "darf nicht länger als 2048 Zeichen sein")
    private String description;

    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String link;

    private String grape;
    
    @NotNull(message = "darf nicht null sein")
    private WineCategory category;
    
    @NotNull(message = "Temperature must be specified")
    @Range(min = -40, max = 100, message = "muss zwischen -40 und 100 sein")
    private Double temperature;
    
    private String country;
    
    @NotNull(message = "Wine must have an owner")
    private UserInfoDto owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVinyard() {
        return vinyard;
    }

    public void setVinyard(String vinyard) {
        this.vinyard = vinyard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGrape() {
        return grape;
    }

    public void setGrape(String grape) {
        this.grape = grape;
    }

    public WineCategory getCategory() {
        return category;
    }

    public void setCategory(WineCategory category) {
        this.category = category;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public UserInfoDto getOwner() {
        return owner;
    }

    public void setOwner(UserInfoDto owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "WineDetailsDto {"
            + "name: " + name
            + "owner: " + owner
            + "category" + category
            + "}";
    }

    public static final class WineDetailsDtoBuilder {
        private Long id;
        private String name;
        private String vinyard;
        private String description;
        private String link;
        private String grape;
        private WineCategory category;
        private Double temperature;
        private String country;
        private UserInfoDto owner;

        public WineDetailsDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public WineDetailsDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public WineDetailsDtoBuilder withVinyard(String vinyard) {
            this.vinyard = vinyard;
            return this;
        }

        public WineDetailsDtoBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public WineDetailsDtoBuilder withLink(String link) {
            this.link = link;
            return this;
        }

        public WineDetailsDtoBuilder withGrape(String grape) {
            this.grape = grape;
            return this;
        }

        public WineDetailsDtoBuilder withCategory(WineCategory category) {
            this.category = category;
            return this;
        }

        public WineDetailsDtoBuilder withTemperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public WineDetailsDtoBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public WineDetailsDtoBuilder withOwner(UserInfoDto owner) {
            this.owner = owner;
            return this;
        }

        public WineDetailsDto build() {
            WineDetailsDto wine = new WineDetailsDto();
            wine.id = this.id;
            wine.name = this.name;
            wine.vinyard = this.vinyard;
            wine.description = this.description;
            wine.link = this.link;
            wine.grape = this.grape;
            wine.category = this.category;
            wine.temperature = this.temperature;
            wine.country = this.country;
            wine.owner = this.owner;
            return wine;
        }
    }

}
