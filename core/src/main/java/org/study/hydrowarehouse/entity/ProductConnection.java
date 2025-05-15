package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products_connection")
public class ProductConnection implements Serializable {

    private static final long serialVersionUID = -4249187559410945137L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_products_connection")
    private Integer productConnectionId;

    @Column(name = "size")
    private String size;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productType")
    private List<Product> products;

    public ProductConnection() {
    }

    public ProductConnection(String size) {
        this.size = size;
    }

    public ProductConnection(Integer productConnectionId, String size) {
        this.productConnectionId = productConnectionId;
        this.size = size;
    }

    public Integer getProductConnectionId() {
        return productConnectionId;
    }

    public void setProductConnectionId(Integer productConnectionId) {
        this.productConnectionId = productConnectionId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductConnection that = (ProductConnection) o;

        if (!Objects.equals(productConnectionId, that.productConnectionId))
            return false;
        if (!Objects.equals(size, that.size)) return false;
        return Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        int result = productConnectionId != null ? productConnectionId.hashCode() : 0;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (products != null ? products.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductConnection{" +
                "productConnectionId=" + productConnectionId +
                ", size='" + size + '\'' +
                ", products=" + products +
                '}';
    }
}
