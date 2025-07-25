package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "product_sku_status")
public class ProductSkuStatus implements Serializable {

    private static final long serialVersionUID = 5820625521473597301L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_sku_status_id")
    private Integer productSkuStatusId;

    @Column(name = "status")
    private String status;

    public ProductSkuStatus() {
    }

    public ProductSkuStatus(Integer productSkuStatusId) {
        this.productSkuStatusId = productSkuStatusId;
    }

    public ProductSkuStatus(String status) {
        this.status = status;
    }

    public ProductSkuStatus(Integer productSkuStatusId, String status) {
        this.productSkuStatusId = productSkuStatusId;
        this.status = status;
    }

    public Integer getProductSkuStatusId() {
        return productSkuStatusId;
    }

    public void setProductSkuStatusId(Integer productSkuStatusId) {
        this.productSkuStatusId = productSkuStatusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSkuStatus that = (ProductSkuStatus) o;

        if (!Objects.equals(productSkuStatusId, that.productSkuStatusId))
            return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = productSkuStatusId != null ? productSkuStatusId.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductSkuStatus{" +
                "productSkuStatusId=" + productSkuStatusId +
                ", status='" + status + '\'' +
                '}';
    }
}
