package org.study.hydro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "pictures")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1618295118872891045L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pictures")
    private Integer pictureId;

    @Column(name = "path")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_products")
    private Product product;

    public Picture() {
    }

    public Picture(String path) {
        this.path = path;
    }

    public Picture(String path, Product product) {
        this.path = path;
        this.product = product;
    }

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Picture picture = (Picture) o;

        if (!Objects.equals(pictureId, picture.pictureId)) return false;
        if (!Objects.equals(path, picture.path)) return false;
        return Objects.equals(product, picture.product);
    }

    @Override
    public int hashCode() {
        int result = pictureId != null ? pictureId.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "pictureId=" + pictureId +
                ", path='" + path + '\'' +
                ", product=" + product +
                '}';
    }
}
