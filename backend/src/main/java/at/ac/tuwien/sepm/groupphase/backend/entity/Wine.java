package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.Objects;

/**
 * Wine entity. Represents a single wine type.
 */
@Entity
@Table(name = "wines", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "vinyard"})})
public class Wine {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * The name of the wine, e.g. Sauvignon Blanc.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * The description of the vine.
     */
    @Column(name = "description", length = 2048)
    private String description;
    /**
     * A link to further information about the wine.
     */
    @Column(name = "link")
    private String link;
    /**
     * The type of grape used to make the wine.
     */
    @Column(name = "grape")
    private String grape;
    /**
     * The vinyard this wine was manufactured at, e.g. Weingut Krug.
     */
    @Column(name = "vinyard", nullable = false)
    private String vinyard;
    /**
     * The category this wine belongs to.
     */
    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private WineCategory category;
    /**
     * The ideal drinking temperature of the wine.
     */
    @Column(name = "temperature", nullable = false)
    private Double temperature;
    /**
     * The country the wine was manufactured at, e.g. Austria.
     */
    @Column(name = "country")
    private String country;
    /**
     * The owner / creator of this wine.
     */
    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "owner", nullable = false)
    private ApplicationUser owner;

    public Wine() {

    }

    public Wine(String name, String vinyard, WineCategory category, Double temperature, String country, ApplicationUser owner) {
        this.name = name;
        this.vinyard = vinyard;
        this.category = category;
        this.temperature = temperature;
        this.country = country;
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wine wine = (Wine) o;
        return Objects.equals(getId(), wine.getId()) && Objects.equals(getName(), wine.getName()) && Objects.equals(getDescription(), wine.getDescription()) && Objects.equals(getLink(), wine.getLink())
            && Objects.equals(getGrape(), wine.getGrape()) && Objects.equals(getVinyard(), wine.getVinyard()) && getCategory() == wine.getCategory() && Objects.equals(getTemperature(), wine.getTemperature())
            && Objects.equals(getCountry(), wine.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getLink(), getGrape(), getVinyard(), getCategory(), getTemperature(), getCountry());
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

    public long getId() {
        return id;
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

    /**
     * Get the owner / creator of this wine, i.e. the Account which can edit the wine.
     *
     * @return Returns the associated user account.
     */
    public ApplicationUser getOwner() {
        return owner;
    }

    /**
     * Set the owner / creator of this wine, i.e. the Account which can edit the wine.
     */
    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    public static final class WineBuilder {
        private Long id;
        private String name;
        private String description;
        private String link;
        private String grape;
        private String vinyard;
        private WineCategory category;
        private Double temperature;
        private String country;
        private ApplicationUser owner;

        public WineBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public WineBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public WineBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public WineBuilder setLink(String link) {
            this.link = link;
            return this;
        }

        public WineBuilder setGrape(String grape) {
            this.grape = grape;
            return this;
        }

        public WineBuilder setVinyard(String vinyard) {
            this.vinyard = vinyard;
            return this;
        }

        public WineBuilder setCategory(WineCategory category) {
            this.category = category;
            return this;
        }

        public WineBuilder setTemperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public WineBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public WineBuilder setOwner(ApplicationUser owner) {
            this.owner = owner;
            return this;
        }

        public Wine build() {
            Wine wine = new Wine();
            wine.id = this.id;
            wine.name = this.name;
            wine.description = this.description;
            wine.link = this.link;
            wine.grape = this.grape;
            wine.vinyard = this.vinyard;
            wine.category = this.category;
            wine.temperature = this.temperature;
            wine.country = this.country;
            wine.owner = this.owner;
            return wine;
        }
    }
}
