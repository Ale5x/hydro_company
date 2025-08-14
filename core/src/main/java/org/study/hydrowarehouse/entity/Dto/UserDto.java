package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.User} entity.
 * <p>
 * This DTO is used to transfer user data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class UserDto extends RepresentationModel<UserDto> {

    /**
     * Unique identifier of the user.
     */
    private Integer userDtoId;

    /**
     * First name of the user.
     */
    private String firstName;

    /**
     * Last name of the user.
     */
    private String lastName;

    /**
     * Email of the user.
     */
    private String email;

    /**
     * Password of the user.
     */
    private String password;

    /**
     * Path to the user's photo.
     */
    private String pathPhoto;

    /**
     * Status of the user.
     */
    private String status;

    /**
     * Registration date and time of the user.
     */
    private LocalDateTime registration;

    /**
     * Collection of roles assigned to the user.
     */
    private Collection<String> role;

    /**
     * Associated user company details.
     */
    private UserCompanyDto userCompanyDto;

    /**
     * Default constructor.
     */
    public UserDto() {}

    /**
     * Constructs a UserDto with specified first name, last name, email, password, and photo path.
     *
     * @param firstName first name of the user
     * @param lastName  last name of the user
     * @param email     email of the user
     * @param password  password of the user
     * @param pathPhoto path to the user's photo
     */
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!Objects.equals(status, userDto.status)) return false;
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
        result = 31 * result + (status != null ? status.hashCode() : 0);
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
                ", status='" + status + '\'' +
                ", password='" + password + '\'' +
                ", pathPhoto='" + pathPhoto + '\'' +
                ", registration=" + registration +
                ", role=" + role +
                ", userCompanyDto=" + userCompanyDto +
                '}';
    }
}
