package org.study.hydro.dao;

import org.study.hydro.entity.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDao {

    int save(Company company);

    List<Company> companies(int limit, int offset);

    List<Company> companiesByName(String name);

    Optional<Company> companyById(int id);
}
