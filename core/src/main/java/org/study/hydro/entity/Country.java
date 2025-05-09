package org.study.hydro.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "countries")
public class Country implements Serializable {

    private static final long serialVersionUID = 780074541061735296L;

    @Id
    @Column(name = "id_countries")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer countryId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<UserCompany> userCompanyList;

    @ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "companyCountries")
    private List<ProductCompany> productCompanies;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "countryProduct")
    private List<Product> productList = new ArrayList<>();

    public Country() {
    }

    public Country(Integer countryId) {
        this.countryId = countryId;
    }

    public Country(String name) {
        this.name = name;
    }

    public Country(Integer countryId, String name) {
        this.countryId = countryId;
        this.name = name;
    }

    public Country(Integer countryId, String name, List<UserCompany> userCompanyList) {
        this.countryId = countryId;
        this.name = name;
        this.userCompanyList = userCompanyList;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserCompany> getUserCompanyList() {
        return userCompanyList;
    }

    public void setUserCompanyList(List<UserCompany> userCompanyList) {
        this.userCompanyList = userCompanyList;
    }

    public List<ProductCompany> getProductCompanies() {
        return productCompanies;
    }

    public void setProductCompanies(List<ProductCompany> productCompanies) {
        this.productCompanies = productCompanies;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (!Objects.equals(countryId, country.countryId)) return false;
        if (!Objects.equals(name, country.name)) return false;
        if (!Objects.equals(userCompanyList, country.userCompanyList))
            return false;
        if (!Objects.equals(productCompanies, country.productCompanies))
            return false;
        return Objects.equals(productList, country.productList);
    }

    @Override
    public int hashCode() {
        int result = countryId != null ? countryId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (userCompanyList != null ? userCompanyList.hashCode() : 0);
        result = 31 * result + (productCompanies != null ? productCompanies.hashCode() : 0);
        result = 31 * result + (productList != null ? productList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryId=" + countryId +
                ", name='" + name + '\'' +
                ", userCompanyList=" + userCompanyList +
                ", productCompanies=" + productCompanies +
                ", productList=" + productList +
                '}';
    }
}
