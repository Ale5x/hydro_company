package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class ProductTypeDto extends RepresentationModel<ProductTypeDto> {

    private Integer productTypeId;
    private String name;

    public ProductTypeDto() {}

    public ProductTypeDto(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

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
