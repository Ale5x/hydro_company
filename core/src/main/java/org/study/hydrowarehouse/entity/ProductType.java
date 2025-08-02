package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a type or category of products.
 * <p>
 * Mapped to the {@code products_type} table in the database, this entity stores the classification of products and
 * maintains a one-to-many relationship with {@link Product} to group products by their type.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
@Table(name = "products_type")
@Entity
public class ProductType implements Serializable {

    private static final long serialVersionUID = 3047606515890435961L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_id")
    private Integer productTypeId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productType")
    private List<Product> products;

    public ProductType() {
    }

    public ProductType(String name) {
        this.name = name;
    }

    public ProductType(Integer productTypeId, String name) {
        this.productTypeId = productTypeId;
        this.name = name;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        ProductType that = (ProductType) o;

        if (!Objects.equals(productTypeId, that.productTypeId))
            return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        int result = productTypeId != null ? productTypeId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (products != null ? products.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "productTypeId=" + productTypeId +
                ", name='" + name + '\'' +
                ", products=" + products +
                '}';
    }
}
