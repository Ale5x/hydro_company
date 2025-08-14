package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.ProductSkuStatus} entity.
 * <p>
 * This DTO is used to transfer product SKU status data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class ProductSkuStatusDto extends RepresentationModel<ProductSkuStatusDto> {

    /**
     * Unique identifier of the product SKU status.
     */
    private Integer productSkuStatusDtoId;

    /**
     * Status description of the product SKU.
     */
    private String status;

    /**
     * Default constructor.
     */
    public ProductSkuStatusDto() {
    }

    /**
     * Constructs a ProductSkuStatusDto with the specified status.
     *
     * @param status status description of the product SKU
     */
    public ProductSkuStatusDto(String status) {
        this.status = status;
    }

    public Integer getProductSkuStatusDtoId() {
        return productSkuStatusDtoId;
    }

    public ProductSkuStatusDto(Integer productSkuStatusDtoId, String status) {
        this.productSkuStatusDtoId = productSkuStatusDtoId;
        this.status = status;
    }

    public void setProductSkuStatusDtoId(Integer productSkuStatusDtoId) {
        this.productSkuStatusDtoId = productSkuStatusDtoId;
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
        if (!super.equals(o)) return false;

        ProductSkuStatusDto that = (ProductSkuStatusDto) o;

        if (!Objects.equals(productSkuStatusDtoId, that.productSkuStatusDtoId))
            return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productSkuStatusDtoId != null ? productSkuStatusDtoId.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductSkuStatusDto{" +
                "productSkuStatusDtoId=" + productSkuStatusDtoId +
                ", status='" + status + '\'' +
                '}';
    }
}
