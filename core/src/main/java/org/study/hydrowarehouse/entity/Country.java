package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a country entry in the system.
 * <p>
 * Mapped to the {@code countries} table in the database, this entity stores the basic information about a country and
 * can be referenced by other entities such as {@link UserCompany} and {@link ProductSku} to indicate
 * their associated country.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
@Entity
@Table(name = "countries")
public class Country implements Serializable {

    private static final long serialVersionUID = 236968937243173868L;

    @Id
    @Column(name = "country_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer countryId;

    @Column(name = "name")
    private String name;

    public Country() {
    }

    public Country(String name) {
        this.name = name;
    }

    public Country(Integer countryId, String name) {
        this.countryId = countryId;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Country country = (Country) o;

        if (!Objects.equals(countryId, country.countryId)) return false;
        return Objects.equals(name, country.name);
    }

    @Override
    public int hashCode() {
        int result = countryId != null ? countryId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryId=" + countryId +
                ", name='" + name + '\'' +
                '}';
    }
}
