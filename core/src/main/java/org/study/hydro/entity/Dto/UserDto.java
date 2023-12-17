package org.study.hydro.entity.Dto;

import java.time.LocalDateTime;
import java.util.Collection;

public class UserDto {

    private int userDtoId;
    private String firstName;
    private String lastName;
    private String password;
    private String pathPhoto;
    private LocalDateTime registration;

    private Collection<String> role;
    private CompanyDto companyDto;

    public int getUserDtoId() {
        return userDtoId;
    }

    public void setUserDtoId(int userDtoId) {
        this.userDtoId = userDtoId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public LocalDateTime getRegistration() {
        return registration;
    }

    public void setRegistration(LocalDateTime registration) {
        this.registration = registration;
    }

    public Collection<String> getRole() {
        return role;
    }

    public void setRole(Collection<String> role) {
        this.role = role;
    }

    public CompanyDto getCompanyDto() {
        return companyDto;
    }

    public void setCompanyDto(CompanyDto companyDto) {
        this.companyDto = companyDto;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userDtoId=" + userDtoId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", pathPhoto='" + pathPhoto + '\'' +
                ", registration=" + registration +
                ", role=" + role +
                ", companyDto=" + companyDto +
                '}';
    }
}
