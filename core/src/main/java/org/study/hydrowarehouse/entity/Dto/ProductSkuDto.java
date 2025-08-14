package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.ProductSku} entity.
 * <p>
 * This DTO is used to transfer product SKU data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class ProductSkuDto extends RepresentationModel<ProductSkuDto> {

    /**
     * Unique identifier of the product SKU.
     */
    private Integer productSkuDtoId;

    /**
     * Code of the product SKU.
     */
    private String code;

    /**
     * Country associated with the product SKU.
     */
    private CountryDto countryDto;

    /**
     * Shelf where the product SKU is stored.
     */
    private ShelfDto shelfDto;

    /**
     * Status of the product SKU.
     */
    private ProductSkuStatusDto status;

    /**
     * Associated product details.
     */
    private ProductDto productDto;

    public Integer getProductSkuDtoId() {
        return productSkuDtoId;
    }

    public void setProductSkuDtoId(Integer productSkuDtoId) {
        this.productSkuDtoId = productSkuDtoId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CountryDto getCountryDto() {
        return countryDto;
    }

    public void setCountryDto(CountryDto countryDto) {
        this.countryDto = countryDto;
    }

    public ShelfDto getShelfDto() {
        return shelfDto;
    }

    public void setShelfDto(ShelfDto shelfDto) {
        this.shelfDto = shelfDto;
    }

    public ProductSkuStatusDto getStatus() {
        return status;
    }

    public void setStatus(ProductSkuStatusDto status) {
        this.status = status;
    }

    public ProductDto getProductDto() {
        return productDto;
    }

    public void setProductDto(ProductDto productDto) {
        this.productDto = productDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductSkuDto that = (ProductSkuDto) o;

        if (!Objects.equals(productSkuDtoId, that.productSkuDtoId))
            return false;
        if (!Objects.equals(code, that.code)) return false;
        if (!Objects.equals(countryDto, that.countryDto)) return false;
        if (!Objects.equals(shelfDto, that.shelfDto)) return false;
        if (!Objects.equals(status, that.status)) return false;
        return Objects.equals(productDto, that.productDto);
    }

    @Override
    public int hashCode() {
        int result = productSkuDtoId != null ? productSkuDtoId.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (countryDto != null ? countryDto.hashCode() : 0);
        result = 31 * result + (shelfDto != null ? shelfDto.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (productDto != null ? productDto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductSkuDto{" +
                "productSkuDtoId=" + productSkuDtoId +
                ", code='" + code + '\'' +
                ", countryDto=" + countryDto +
                ", shelfDto=" + shelfDto +
                ", status=" + status +
                ", productDto=" + productDto +
                '}';
    }
}
