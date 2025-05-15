package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Table(name = "product_companies")
@Entity
public class ProductCompany implements Serializable {

    private static final long serialVersionUID = -3206315018093822278L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product_companies")
    private Integer productCompanyId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "productCompany")
    private List<Product> productList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "countries_has_product_companies",
            joinColumns = {@JoinColumn(name = "id_product_companies")},
            inverseJoinColumns = {@JoinColumn(name = "id_countries")}
    )
    private Set<Country> companyCountries;

    public ProductCompany() {
    }

    public ProductCompany(Integer productCompanyId, String name) {
        this.productCompanyId = productCompanyId;
        this.name = name;
    }

    public ProductCompany(String name) {
        this.name = name;
    }

    public Integer getProductCompanyId() {
        return productCompanyId;
    }

    public void setProductCompanyId(Integer productCompanyId) {
        this.productCompanyId = productCompanyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Set<Country> getCompanyCountries() {
        return companyCountries;
    }

    public void setCompanyCountries(Set<Country> companyCountries) {
        this.companyCountries = companyCountries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductCompany that = (ProductCompany) o;

        if (!Objects.equals(productCompanyId, that.productCompanyId))
            return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(productList, that.productList)) return false;
        return Objects.equals(companyCountries, that.companyCountries);
    }

    @Override
    public int hashCode() {
        int result = productCompanyId != null ? productCompanyId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (productList != null ? productList.hashCode() : 0);
        result = 31 * result + (companyCountries != null ? companyCountries.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductCompany{" +
                "productCompanyId=" + productCompanyId +
                ", name='" + name + '\'' +
                ", productList=" + productList +
                ", companyCountries=" + companyCountries +
                '}';
    }
}