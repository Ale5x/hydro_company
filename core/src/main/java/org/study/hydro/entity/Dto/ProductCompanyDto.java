package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

public class ProductCompanyDto extends RepresentationModel<ProductCompanyDto> {

    private Integer productCompanyDtoId;
    private String name;
    private Integer countryId;
    private List<CountryDto> countries;

    public ProductCompanyDto() {}

    public ProductCompanyDto(Integer productCompanyDtoId) {
        this.productCompanyDtoId = productCompanyDtoId;
    }

    public ProductCompanyDto(Integer productCompanyDtoId, String name) {
        this.productCompanyDtoId = productCompanyDtoId;
        this.name = name;
    }
    public Integer getProductCompanyDtoId() {
        return productCompanyDtoId;
    }

    public void setProductCompanyDtoId(Integer productCompanyDtoId) {
        this.productCompanyDtoId = productCompanyDtoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public List<CountryDto> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDto> countries) {
        this.countries = countries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ProductCompanyDto that = (ProductCompanyDto) o;

        if (!Objects.equals(productCompanyDtoId, that.productCompanyDtoId))
            return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(countryId, that.countryId)) return false;
        return Objects.equals(countries, that.countries);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productCompanyDtoId != null ? productCompanyDtoId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        result = 31 * result + (countries != null ? countries.hashCode() : 0);
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
