package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.ProductType} entity.
 * <p>
 * This DTO is used to transfer product type data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class ProductTypeDto extends RepresentationModel<ProductTypeDto> {

    /**
     * Unique identifier of the product type.
     */
    private Integer productTypeId;

    /**
     * Name of the product type.
     */
    private String name;

    /**
     * Default constructor.
     */
    public ProductTypeDto() {}

    /**
     * Constructs a ProductTypeDto with the specified id.
     *
     * @param productTypeId unique identifier of the product type
     */
    public ProductTypeDto(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    /**
     * Constructs a ProductTypeDto with the specified id and name.
     *
     * @param productTypeId unique identifier of the product type
     * @param name          name of the product type
     */
    public ProductTypeDto(Integer productTypeId, String name) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProductTypeDto that = (ProductTypeDto) o;

        if (!Objects.equals(productTypeId, that.productTypeId))
            return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productTypeId != null ? productTypeId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductTypeDto{" +
                "productTypeId=" + productTypeId +
                ", name='" + name + '\'' +
                '}';
    }
}
