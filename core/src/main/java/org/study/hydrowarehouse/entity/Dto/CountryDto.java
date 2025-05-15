package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class CountryDto extends RepresentationModel<CountryDto> {

    private Integer countryId;
    private String name;

    public CountryDto() {}

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
