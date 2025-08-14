package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.UserStatus} entity.
 * <p>
 * This DTO is used to transfer user status data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class UserStatusDto extends RepresentationModel<UserStatusDto> {

    /**
     * Unique identifier of the user status.
     */
    private Long userStatusIdDto;

    /**
     * Status description of the user.
     */
    private String status;

    public Long getUserStatusIdDto() {
        return userStatusIdDto;
    }

    public void setUserStatusIdDto(Long userStatusIdDto) {
        this.userStatusIdDto = userStatusIdDto;
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

        UserStatusDto that = (UserStatusDto) o;

        if (!Objects.equals(userStatusIdDto, that.userStatusIdDto))
            return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userStatusIdDto != null ? userStatusIdDto.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserStatusDto{" +
                "userStatusIdDto=" + userStatusIdDto +
                ", status='" + status + '\'' +
                '}';
    }
}
