package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

public class UserDto extends RepresentationModel<UserDto> {

    private Integer userDtoId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String pathPhoto;
    private LocalDateTime registration;

    private Collection<String> role;
    private UserCompanyDto userCompanyDto;

    public UserDto() {}

    public UserDto(String firstName, String lastName, String email, String password,
                String pathPhoto) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.pathPhoto = pathPhoto;
        this.email = email;
    }

    public Integer getUserDtoId() {
        return userDtoId;
    }

    public void setUserDtoId(Integer userDtoId) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UserCompanyDto getUserCompanyDto() {
        return userCompanyDto;
    }

    public void setUserCompanyDto(UserCompanyDto userCompanyDto) {
        this.userCompanyDto = userCompanyDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UserDto userDto = (UserDto) o;

        if (!Objects.equals(userDtoId, userDto.userDtoId)) return false;
        if (!Objects.equals(firstName, userDto.firstName)) return false;
        if (!Objects.equals(lastName, userDto.lastName)) return false;
        if (!Objects.equals(email, userDto.email)) return false;
        if (!Objects.equals(password, userDto.password)) return false;
        if (!Objects.equals(pathPhoto, userDto.pathPhoto)) return false;
        if (!Objects.equals(registration, userDto.registration))
            return false;
        if (!Objects.equals(role, userDto.role)) return false;
        return Objects.equals(userCompanyDto, userDto.userCompanyDto);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userDtoId != null ? userDtoId.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (pathPhoto != null ? pathPhoto.hashCode() : 0);
        result = 31 * result + (registration != null ? registration.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (userCompanyDto != null ? userCompanyDto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userDtoId=" + userDtoId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", pathPhoto='" + pathPhoto + '\'' +
                ", registration=" + registration +
                ", role=" + role +
                ", userCompanyDto=" + userCompanyDto +
                '}';
    }
}
