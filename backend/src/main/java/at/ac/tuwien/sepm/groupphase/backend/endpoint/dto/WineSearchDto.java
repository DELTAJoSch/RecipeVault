package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.entity.WineCategory;
import jakarta.validation.constraints.Size;

/**
 * Represents a search for a wine.
 */
public class WineSearchDto {

    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String name;

    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String vinyard;

    private WineCategory category;

    @Size(max = 255, message = "darf nicht länger als 255 Zeichen sein")
    private String country;

    public String getName() {
        return name;
    }

    /**
     * Sets the name. This parameter should be contained in the name of results.
     *
     * @param name If set to null, this parameter is ignored in the search
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getVinyard() {
        return vinyard;
    }

    /**
     * Sets the vinyard. This parameter should be contained in the vinyard of results.
     *
     * @param vinyard If set to null, this parameter is ignored in the search
     */
    public void setVinyard(String vinyard) {
        this.vinyard = vinyard;
    }

    public WineCategory getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category If set to null, this parameter is ignored in the search
     */
    public void setCategory(WineCategory category) {
        this.category = category;
    }

    public String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param country If set to null, this parameter is ignored in the search
     */
    public void setCountry(String country) {
        this.country = country;
    }

    public static final class WineSearchDtoBuilder {

        private String name;
        private String vinyard;
        private WineCategory category;
        private String country;

        public WineSearchDtoBuilder() {
            // default constructor
        }

        public WineSearchDtoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public WineSearchDtoBuilder setVinyard(String vinyard) {
            this.vinyard = vinyard;
            return this;
        }

        public WineSearchDtoBuilder setCategory(WineCategory category) {
            this.category = category;
            return this;
        }

        public WineSearchDtoBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public WineSearchDto build() {
            var dto = new WineSearchDto();
            dto.country = country;
            dto.name = name;
            dto.vinyard = vinyard;
            dto.category = category;

            return dto;
        }
    }


}
