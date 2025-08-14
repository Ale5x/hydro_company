package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.UserCompany} entity.
 * <p>
 * This DTO is used to transfer user company data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class UserCompanyDto extends RepresentationModel<UserCompanyDto> {

    /**
     * Unique identifier of the user company.
     */
    private Integer companyDtoId;

    /**
     * Name of the user company.
     */
    private String name;

    /**
     * Address of the user company.
     */
    private String address;

    /**
     * Country associated with the user company.
     */
    private CountryDto countryDto;

    /**
     * Default constructor.
     */
    public UserCompanyDto() {
    }

    /**
     * Constructs a UserCompanyDto with the specified id, name, and address.
     *
     * @param companyDtoId unique identifier of the user company
     * @param name         name of the user company
     * @param address      address of the user company
     */
    public UserCompanyDto(Integer companyDtoId, String name, String address) {
        this.companyDtoId = companyDtoId;
        this.name = name;
        this.address = address;
    }

    public Integer getCompanyDtoId() {
        return companyDtoId;
    }

    public void setCompanyDtoId(Integer companyDtoId) {
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

        if (!Objects.equals(companyDtoId, that.companyDtoId)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(address, that.address)) return false;
        return Objects.equals(countryDto, that.countryDto);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (companyDtoId != null ? companyDtoId.hashCode() : 0);
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
