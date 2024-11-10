package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class UserCompanyDto extends RepresentationModel<UserCompanyDto> {

    private int companyDtoId;
    private String name;

    private String address;

    private CountryDto countryDto;


    public UserCompanyDto() {
    }

    public UserCompanyDto(int companyDtoId, String name, String address) {
        this.companyDtoId = companyDtoId;
        this.name = name;
        this.address = address;
    }

    public int getCompanyDtoId() {
        return companyDtoId;
    }

    public void setCompanyDtoId(int companyDtoId) {
        this.companyDtoId = companyDtoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CountryDto getCountryDto() {
        return countryDto;
    }

    public void setCountryDto(CountryDto countryDto) {
        this.countryDto = countryDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserCompanyDto that = (UserCompanyDto) o;

        if (companyDtoId != that.companyDtoId) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(address, that.address)) return false;
        return Objects.equals(countryDto, that.countryDto);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + companyDtoId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (countryDto != null ? countryDto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserCompanyDto{" +
                "companyDtoId=" + companyDtoId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", countryDto=" + countryDto +
                '}';
    }
}
