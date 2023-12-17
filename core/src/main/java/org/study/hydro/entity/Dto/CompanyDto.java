package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

public class CompanyDto extends RepresentationModel<CompanyDto> {

    private int companyDtoId;
    private String name;

    private String address;

    public CompanyDto() {
    }

    public CompanyDto(int companyDtoId, String name, String address) {
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

    @Override
    public String toString() {
        return "CompanyDto{" +
                "companyDtoId=" + companyDtoId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
