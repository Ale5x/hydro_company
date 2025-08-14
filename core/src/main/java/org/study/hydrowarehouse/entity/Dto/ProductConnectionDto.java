package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.ProductConnection} entity.
 * <p>
 * This DTO is used to transfer product connection data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class ProductConnectionDto extends RepresentationModel<ProductConnectionDto> {

    /**
     * Unique identifier of the product connection.
     */
    private Integer productConnectionId;

    /**
     * Size specification of the product connection.
     */
    private String size;

    /**
     * Default constructor.
     */
    public ProductConnectionDto() {}

    /**
     * Constructs a ProductConnectionDto with the specified id and size.
     *
     * @param productConnectionId unique identifier of the product connection
     * @param size                size specification of the product connection
     */
    public ProductConnectionDto(Integer productConnectionId, String size) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProductConnectionDto that = (ProductConnectionDto) o;

        if (!Objects.equals(productConnectionId, that.productConnectionId))
            return false;
        return Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productConnectionId != null ? productConnectionId.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductConnectionDto{" +
                "productConnectionId=" + productConnectionId +
                ", size='" + size + '\'' +
                '}';
    }
}
