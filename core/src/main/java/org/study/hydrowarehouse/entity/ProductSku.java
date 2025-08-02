package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Stock Keeping Unit (SKU) for a specific product.
 * <p>
 * Mapped to the {@code product_sku} table in the database, this entity contains SKU-specific details such as its code,
 * associated {@link Product}, origin {@link Country}, current {@link ProductSkuStatus}, and storage location
 * {@link Shelf}. It serves as the primary unit for tracking individual product instances in the warehouse.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
@Entity
@Table(name = "product_sku")
public class ProductSku implements Serializable {

    private static final long serialVersionUID = -2389655509207074943L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_sku_id")
    private Integer productSkuId;

    @Column(name = "sku_code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private ProductSkuStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shelf_id", nullable = false)
    private Shelf shelf;

    public Integer getProductSkuId() {
        return productSkuId;
    }

    public void setProductSkuId(Integer productSkuId) {
        this.productSkuId = productSkuId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public ProductSkuStatus getStatus() {
        return status;
    }

    public void setStatus(ProductSkuStatus status) {
        this.status = status;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSku that = (ProductSku) o;

        if (!Objects.equals(productSkuId, that.productSkuId)) return false;
        if (!Objects.equals(code, that.code)) return false;
        if (!Objects.equals(product, that.product)) return false;
        if (!Objects.equals(country, that.country)) return false;
        if (!Objects.equals(status, that.status)) return false;
        return Objects.equals(shelf, that.shelf);
    }

    @Override
    public int hashCode() {
        int result = productSkuId != null ? productSkuId.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (shelf != null ? shelf.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductSku{" +
                "productSkuId=" + productSkuId +
                ", code='" + code + '\'' +
                ", product=" + product +
                ", country=" + country +
                ", status=" + status +
                ", shelf=" + shelf +
                '}';
    }
}
