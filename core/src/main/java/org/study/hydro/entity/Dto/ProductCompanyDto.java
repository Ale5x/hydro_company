package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class ProductCompanyDto extends RepresentationModel<ProductCompanyDto> {

    private int productCompanyDtoId;
    private String name;

    public ProductCompanyDto() {}

    public ProductCompanyDto(int productCompanyDtoId, String name) {
        this.productCompanyDtoId = productCompanyDtoId;
        this.name = name;
    }
    public int getProductCompanyDtoId() {
        return productCompanyDtoId;
    }

    public void setProductCompanyDtoId(int productCompanyDtoId) {
        this.productCompanyDtoId = productCompanyDtoId;
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

        ProductCompanyDto that = (ProductCompanyDto) o;

        if (productCompanyDtoId != that.productCompanyDtoId) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = productCompanyDtoId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductCompanyDto{" +
                "productCompanyDtoId=" + productCompanyDtoId +
                ", name='" + name + '\'' +
                '}';
    }
}
