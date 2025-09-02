package com.hotel.erp.service;

import com.hotel.erp.dto.CompanyDTO;
import java.util.List;

public interface CompanyService {
    CompanyDTO createCompany(CompanyDTO companyDTO);

    CompanyDTO updateCompany(Integer id, CompanyDTO companyDTO);

    CompanyDTO getCompanyById(Integer id);

    CompanyDTO getCompanyByName(String name);

    List<CompanyDTO> getCompaniesByGst(String gst);

    List<CompanyDTO> getAllCompanies();

    void deleteCompany(Integer id);
}
