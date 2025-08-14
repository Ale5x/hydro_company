package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.Country} entity.
 * <p>
 * This DTO is used to transfer country data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class CountryDto extends RepresentationModel<CountryDto> {

    /**
     * Unique identifier of the country.
     */
    private Integer countryId;

    /**
     * Name of the country.
     */
    private String name;

    /**
     * Default constructor.
     */
    public CountryDto() {}

    /**
     * Constructs a CountryDto with specified id and name.
     *
     * @param countryId the unique identifier of the country
     * @param name      the name of the country
     */
    public CountryDto (Integer countryId, String name) {
        this.countryId = countryId;
        this.name = name;
    }


    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
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

        CountryDto that = (CountryDto) o;

        if (!Objects.equals(countryId, that.countryId)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (countryId != null ? countryId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CountryDto{" +
                "countryId=" + countryId +
                ", name='" + name + '\'' +
                '}';
    }
}
