package org.study.hydro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "companies")
public class Company implements Serializable {


    private static final long serialVersionUID = -2899199795875787129L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_companies")
    private int companyId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<User> user;

    public Company() {
    }

    public Company(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Company(int companyId, String name, String address) {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
    }

    public Company(int companyId, String name, String address, List<User> user) {
        this.companyId = companyId;
        this.name = name;
        this.address = address;
        this.user = user;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        if (companyId != company.companyId) return false;
        if (!Objects.equals(name, company.name)) return false;
        if (!Objects.equals(address, company.address)) return false;
        return Objects.equals(user, company.user);
    }

    @Override
    public int hashCode() {
        int result = companyId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Company{" +
                "companyId=" + companyId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", user=" + user +
                '}';
    }
}
