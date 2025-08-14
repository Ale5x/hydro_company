package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.Product} entity.
 * <p>
 * This DTO is used to transfer product data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class ProductDto extends RepresentationModel<ProductDto> {

    /**
     * Unique identifier of the product.
     */
    private Integer productDtoId;

    /**
     * Quantity of the product.
     */
    private Integer count;

    /**
     * Flow rate specification of the product.
     */
    private Integer flowRate;

    /**
     * Operating pressure of the product.
     */
    private Integer pressure;

    /**
     * Maximum allowable pressure of the product.
     */
    private Integer pressureMax;

    /**
     * Weight of the product.
     */
    private Double weight;

    /**
     * Additional information or notes about the product.
     */
    private String additionalInformation;

    /**
     * Associated product type details.
     */
    private ProductTypeDto productTypeDto;

    /**
     * Path to the hydraulic scheme file.
     */
    private String pathHydraulicScheme;

    /**
     * List of paths to product images.
     */
    private List<String> imagesPaths = new ArrayList<>();

    /**
     * List of associated product SKU details.
     */
    private List<ProductSkuDto> productSkuDtos = new ArrayList<>();

    /**
     * Associated product company details.
     */
    private ProductCompanyDto productCompanyDto;

    /**
     * Associated product connection details.
     */
    private ProductConnectionDto productConnectionDto;

    /**
     * Default constructor.
     */
    public ProductDto(){}

    /**
     * Constructs a ProductDto with the specified id.
     *
     * @param id unique identifier of the product
     */
    public ProductDto(Integer id) {
        this.productDtoId = id;
    }

    public Integer getProductDtoId() {
        return productDtoId;
    }

    public void setProductDtoId(Integer productDtoId) {
        this.productDtoId = productDtoId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(Integer flowRate) {
        this.flowRate = flowRate;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(Integer pressureMax) {
        this.pressureMax = pressureMax;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getPathHydraulicScheme() {
        return pathHydraulicScheme;
    }

    public void setPathHydraulicScheme(String pathHydraulicScheme) {
        this.pathHydraulicScheme = pathHydraulicScheme;
    }

    public List<String> getImagesPaths() {
        return imagesPaths;
    }

    public void setImagesPaths(List<String> imagesPaths) {
        this.imagesPaths = imagesPaths;
    }

    public ProductCompanyDto getProductCompanyDto() {
        return productCompanyDto;
    }

    public void setProductCompanyDto(ProductCompanyDto productCompanyDto) {
        this.productCompanyDto = productCompanyDto;
    }

    public ProductConnectionDto getProductConnectionDto() {
        return productConnectionDto;
    }

    public void setProductConnectionDto(ProductConnectionDto productConnectionDto) {
        this.productConnectionDto = productConnectionDto;
    }

    public ProductTypeDto getProductTypeDto() {
        return productTypeDto;
    }

    public void setProductTypeDto(ProductTypeDto productTypeDto) {
        this.productTypeDto = productTypeDto;
    }

    public List<ProductSkuDto> getProductSkuDtos() {
        return productSkuDtos;
    }

    public void setProductSkuDtos(List<ProductSkuDto> productSkuDtos) {
        this.productSkuDtos = productSkuDtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProductDto that = (ProductDto) o;

        if (!Objects.equals(productDtoId, that.productDtoId)) return false;
        if (!Objects.equals(count, that.count)) return false;
        if (!Objects.equals(flowRate, that.flowRate)) return false;
        if (!Objects.equals(pressure, that.pressure)) return false;
        if (!Objects.equals(pressureMax, that.pressureMax)) return false;
        if (!Objects.equals(weight, that.weight)) return false;
        if (!Objects.equals(additionalInformation, that.additionalInformation))
            return false;
        if (!Objects.equals(productTypeDto, that.productTypeDto))
            return false;
        if (!Objects.equals(pathHydraulicScheme, that.pathHydraulicScheme))
            return false;
        if (!Objects.equals(imagesPaths, that.imagesPaths)) return false;
        if (!Objects.equals(productCompanyDto, that.productCompanyDto))
            return false;
        return Objects.equals(productConnectionDto, that.productConnectionDto);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productDtoId != null ? productDtoId.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (flowRate != null ? flowRate.hashCode() : 0);
        result = 31 * result + (pressure != null ? pressure.hashCode() : 0);
        result = 31 * result + (pressureMax != null ? pressureMax.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (additionalInformation != null ? additionalInformation.hashCode() : 0);
        result = 31 * result + (productTypeDto != null ? productTypeDto.hashCode() : 0);
        result = 31 * result + (pathHydraulicScheme != null ? pathHydraulicScheme.hashCode() : 0);
        result = 31 * result + (imagesPaths != null ? imagesPaths.hashCode() : 0);
        result = 31 * result + (productCompanyDto != null ? productCompanyDto.hashCode() : 0);
        result = 31 * result + (productConnectionDto != null ? productConnectionDto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "productDtoId=" + productDtoId +
                ", count=" + count +
                ", flowRate=" + flowRate +
                ", pressure=" + pressure +
                ", pressureMax=" + pressureMax +
                ", weight=" + weight +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", productTypeDto=" + productTypeDto +
                ", pathHydraulicScheme='" + pathHydraulicScheme + '\'' +
                ", imagesPaths=" + imagesPaths +
                ", productCompanyDto=" + productCompanyDto +
                ", productConnectionDto=" + productConnectionDto +
                ", skus=" + productSkuDtos +
                '}';
    }
}
