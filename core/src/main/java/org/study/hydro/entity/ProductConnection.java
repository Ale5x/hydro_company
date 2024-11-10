package org.study.hydro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products_connection")
public class ProductConnection implements Serializable {

    private static final long serialVersionUID = -9132594259339316020L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_products_connection")
    private int productConnectionId;

    @Column(name = "size")
    private String size;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productType")
    private List<Product> products;

    public ProductConnection() {
    }

    public ProductConnection(String size) {
        this.size = size;
    }

    public ProductConnection(int productConnectionId, String size) {
        this.productConnectionId = productConnectionId;
        this.size = size;
    }

    public int getProductConnectionId() {
        return productConnectionId;
    }

    public void setProductConnectionId(int productConnectionId) {
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

        if (productConnectionId != that.productConnectionId) return false;
        if (!Objects.equals(size, that.size)) return false;
        return Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        int result = productConnectionId;
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
