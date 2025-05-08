package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class ProductTypeDto extends RepresentationModel<ProductTypeDto> {

    private int productTypeId;
    private String name;

    public ProductTypeDto() {}

    public ProductTypeDto(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public ProductTypeDto(int productTypeId, String name) {
        this.productTypeId = productTypeId;
        this.name = name;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
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

        if (productTypeId != that.productTypeId) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + productTypeId;
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
