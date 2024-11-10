package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDto extends RepresentationModel<ProductDto> {

    private int productDtoId;
    private int count;
    private String stockKeepingUnit;
    private int flowRate;
    private int pressure;
    private int pressureMax;
    private double weight;
    private String additionalInformation;

    private ProductTypeDto productTypeDto;
    private String pathHydraulicScheme;
    private List<String> imagesPaths = new ArrayList<>();
    private List<StorageRackDto> storageRackDtoList = new ArrayList<>();
    private ProductCompanyDto productCompanyDto;
    private ProductConnectionDto productConnectionDto;
    private CountryDto countryDto;

    public int getProductDtoId() {
        return productDtoId;
    }

    public void setProductDtoId(int productDtoId) {
        this.productDtoId = productDtoId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStockKeepingUnit() {
        return stockKeepingUnit;
    }

    public void setStockKeepingUnit(String stockKeepingUnit) {
        this.stockKeepingUnit = stockKeepingUnit;
    }

    public int getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(int flowRate) {
        this.flowRate = flowRate;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(int pressureMax) {
        this.pressureMax = pressureMax;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
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

    public List<StorageRackDto> getStorageRackDtoList() {
        return storageRackDtoList;
    }

    public void setStorageRackDtoList(List<StorageRackDto> storageRackDtoList) {
        this.storageRackDtoList = storageRackDtoList;
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

    public CountryDto getCountryDto() {
        return countryDto;
    }

    public void setCountryDto(CountryDto countryDto) {
        this.countryDto = countryDto;
    }

    public ProductTypeDto getProductTypeDto() {
        return productTypeDto;
    }

    public void setProductTypeDto(ProductTypeDto productTypeDto) {
        this.productTypeDto = productTypeDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProductDto that = (ProductDto) o;

        if (productDtoId != that.productDtoId) return false;
        if (count != that.count) return false;
        if (flowRate != that.flowRate) return false;
        if (pressure != that.pressure) return false;
        if (pressureMax != that.pressureMax) return false;
        if (Double.compare(that.weight, weight) != 0) return false;
        if (!Objects.equals(stockKeepingUnit, that.stockKeepingUnit))
            return false;
        if (!Objects.equals(additionalInformation, that.additionalInformation))
            return false;
        if (!Objects.equals(productTypeDto, that.productTypeDto))
            return false;
        if (!Objects.equals(pathHydraulicScheme, that.pathHydraulicScheme))
            return false;
        if (!Objects.equals(imagesPaths, that.imagesPaths)) return false;
        if (!Objects.equals(storageRackDtoList, that.storageRackDtoList))
            return false;
        if (!Objects.equals(productCompanyDto, that.productCompanyDto))
            return false;
        if (!Objects.equals(productConnectionDto, that.productConnectionDto))
            return false;
        return Objects.equals(countryDto, that.countryDto);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + productDtoId;
        result = 31 * result + count;
        result = 31 * result + (stockKeepingUnit != null ? stockKeepingUnit.hashCode() : 0);
        result = 31 * result + flowRate;
        result = 31 * result + pressure;
        result = 31 * result + pressureMax;
        temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (additionalInformation != null ? additionalInformation.hashCode() : 0);
        result = 31 * result + (productTypeDto != null ? productTypeDto.hashCode() : 0);
        result = 31 * result + (pathHydraulicScheme != null ? pathHydraulicScheme.hashCode() : 0);
        result = 31 * result + (imagesPaths != null ? imagesPaths.hashCode() : 0);
        result = 31 * result + (storageRackDtoList != null ? storageRackDtoList.hashCode() : 0);
        result = 31 * result + (productCompanyDto != null ? productCompanyDto.hashCode() : 0);
        result = 31 * result + (productConnectionDto != null ? productConnectionDto.hashCode() : 0);
        result = 31 * result + (countryDto != null ? countryDto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "productDtoId=" + productDtoId +
                ", count=" + count +
                ", stockKeepingUnit='" + stockKeepingUnit + '\'' +
                ", flowRate=" + flowRate +
                ", pressure=" + pressure +
                ", pressureMax=" + pressureMax +
                ", weight=" + weight +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", productTypeDto=" + productTypeDto +
                ", pathHydraulicScheme='" + pathHydraulicScheme + '\'' +
                ", imagesPaths=" + imagesPaths +
                ", storageRackDtoList=" + storageRackDtoList +
                ", productCompanyDto=" + productCompanyDto +
                ", productConnectionDto=" + productConnectionDto +
                ", countryDto=" + countryDto +
                '}';
    }
}
