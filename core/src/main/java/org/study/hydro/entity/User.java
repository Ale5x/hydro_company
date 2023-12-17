package org.study.hydro.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements Serializable {


    private static final long serialVersionUID = -1593922590585255673L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    private int userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password")
    private String password;

    @Column(name = "path")
    private String pathPhoto;

    @Column(name = "registration")
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime registration;

    @ManyToOne  //(optional=false, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_roles")
    private Role role;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_companies")
    private Company company;

    public User() {
    }

    public User(int userId, String firstName, String lastName, String password,
                String pathPhoto, LocalDateTime registration, Role role, Company company) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.pathPhoto = pathPhoto;
        this.registration = registration;
        this.role = role;
        this.company = company;
    }

    public User(String firstName, String lastName, String password, String pathPhoto,
                LocalDateTime registration, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.pathPhoto = pathPhoto;
        this.registration = registration;
        this.role = role;
    }

    public User(String firstName, String lastName, String password, String pathPhoto,
                LocalDateTime registration, Role role, Company company) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.pathPhoto = pathPhoto;
        this.registration = registration;
        this.role = role;
        this.company = company;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (userId != user.userId) return false;
        if (!Objects.equals(firstName, user.firstName)) return false;
        if (!Objects.equals(lastName, user.lastName)) return false;
        if (!Objects.equals(password, user.password)) return false;
        if (!Objects.equals(pathPhoto, user.pathPhoto)) return false;
        if (!Objects.equals(registration, user.registration)) return false;
        if (role != user.role) return false;
        return Objects.equals(company, user.company);
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (pathPhoto != null ? pathPhoto.hashCode() : 0);
        result = 31 * result + (registration != null ? registration.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", path='" + pathPhoto + '\'' +
                ", registration=" + registration +
                ", role=" + role +
                ", company=" + company +
                '}';
    }
}
