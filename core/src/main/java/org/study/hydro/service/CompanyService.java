package org.study.hydro.service;

import org.study.hydro.entity.Dto.CompanyDto;

import java.util.List;
import java.util.Optional;

public interface CompanyService {

    boolean create(CompanyDto companyDto);

    List<CompanyDto> findByName(String name);

    List<CompanyDto> findAll(int offset, int size);

    Optional<CompanyDto> findById(int id);
}
