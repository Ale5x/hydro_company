package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the status of a user account.
 * <p>
 * Mapped to the {@code user_statuses} table in the database, this entity defines various states a user account can
 * have, such as active, inactive, or suspended, which are used to control user access and behavior within the system.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
@Entity
@Table(name = "user_statuses")
public class UserStatus implements Serializable {

    private static final long serialVersionUID = -2641116756767715650L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_status_id")
    private Long userStatusId;

    @Column(name = "status")
    private String status;

    public UserStatus() {}

    public UserStatus(String status) {
        this.status = status;
    }

    public UserStatus(Long userStatusId, String status) {
        this.userStatusId = userStatusId;
        this.status = status;
    }

    public Long getUserStatusId() { return userStatusId; }
    public void UserStatusId (Long id) { this.userStatusId = userStatusId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserStatus that = (UserStatus) o;

        if (!Objects.equals(userStatusId, that.userStatusId)) return false;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        int result = userStatusId != null ? userStatusId.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
                "userStatusId=" + userStatusId +
                ", status='" + status + '\'' +
                '}';
    }
}
