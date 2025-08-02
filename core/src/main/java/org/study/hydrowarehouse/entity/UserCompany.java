package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


/**
 * Represents a company to which users belong.
 * <p>
 * Mapped to the {@code user_companies} table in the database, this entity stores company details such as name and
 * address, maintains a one-to-many relationship with {@link User} entities representing its employees, and associates
 * with {@link Country} to specify the company's location.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
@Entity
@Table(name = "user_companies")
public class UserCompany implements Serializable {

    private static final long serialVersionUID = -8077864123660426289L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_company_id")
    private Integer userCompanyId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "userCompany", fetch = FetchType.LAZY)
    private List<User> users;

    @ManyToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;

    public UserCompany() {
    }

    public UserCompany(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public UserCompany(Integer userCompanyId, String name, String address, Country country) {
        this.userCompanyId = userCompanyId;
        this.name = name;
        this.address = address;
        this.country = country;
    }

    public UserCompany(Integer userCompanyId, String name, String address) {
        this.userCompanyId = userCompanyId;
        this.name = name;
        this.address = address;
    }

    public UserCompany(Integer userCompanyId, String name, String address, List<User> users) {
        this.userCompanyId = userCompanyId;
        this.name = name;
        this.address = address;
        this.users = users;
    }

    public Integer getUserCompanyId() {
        return userCompanyId;
    }

    public void setUserCompanyId(Integer userCompanyId) {
        this.userCompanyId = userCompanyId;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserCompany that = (UserCompany) o;

        if (!Objects.equals(userCompanyId, that.userCompanyId))
            return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(address, that.address)) return false;
        if (!Objects.equals(users, that.users)) return false;
        return Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        int result = userCompanyId != null ? userCompanyId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserCompany{" +
                "userCompanyId=" + userCompanyId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", users=" + users +
                ", country=" + country +
                '}';
    }
}
