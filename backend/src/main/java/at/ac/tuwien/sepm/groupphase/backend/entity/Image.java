package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 2048)
    private String name;

    public Image(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Image() {
    }

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

    @Override
    public String toString() {
        return "Image [id=" + id + ", name=" + name + "]";
    }

    public static final class ImageBuilder {

        private Long id;
        private String name;

        public ImageBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ImageBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Image build() {
            return new Image(id, name);
        }
    }
}
