package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.ProductCompany} entity.
 * <p>
 * This DTO is used to transfer product company data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class ProductCompanyDto extends RepresentationModel<ProductCompanyDto> {

    /**
     * Unique identifier of the product company.
     */
    private Integer productCompanyDtoId;

    /**
     * Name of the product company.
     */
    private String name;

    /**
     * List of countries associated with the product company.
     */
    private List<CountryDto> countries;

    /**
     * Default constructor.
     */
    public ProductCompanyDto() {}

    /**
     * Constructs a ProductCompanyDto with the specified id.
     *
     * @param productCompanyDtoId unique identifier of the product company
     */
    public ProductCompanyDto(Integer productCompanyDtoId) {
        this.productCompanyDtoId = productCompanyDtoId;
    }

    /**
     * Constructs a ProductCompanyDto with the specified id and name.
     *
     * @param productCompanyDtoId unique identifier of the product company
     * @param name                name of the product company
     */
    public ProductCompanyDto(Integer productCompanyDtoId, String name) {
        this.productCompanyDtoId = productCompanyDtoId;
        this.name = name;
    }

    /**
     * Constructs a ProductCompanyDto with the specified id, name, and associated countries.
     *
     * @param productCompanyDtoId unique identifier of the product company
     * @param name                name of the product company
     * @param countries           list of associated countries
     */
    public ProductCompanyDto(Integer productCompanyDtoId, String name, List<CountryDto> countries) {
        this.productCompanyDtoId = productCompanyDtoId;
        this.name = name;
        this.countries = countries;
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
        return Objects.equals(countries, that.countries);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (productCompanyDtoId != null ? productCompanyDtoId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (countries != null ? countries.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductCompanyDto{" +
                "productCompanyDtoId=" + productCompanyDtoId +
                ", name='" + name + '\'' +
                ", countries=" + countries +
                '}';
    }
}
