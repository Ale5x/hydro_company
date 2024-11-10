package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class ProductConnectionDto extends RepresentationModel<ProductConnectionDto> {

    private int productConnectionId;
    private String size;

    public ProductConnectionDto() {}

    public ProductConnectionDto(int productConnectionId, String size) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductConnectionDto that = (ProductConnectionDto) o;

        if (productConnectionId != that.productConnectionId) return false;
        return Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        int result = productConnectionId;
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
