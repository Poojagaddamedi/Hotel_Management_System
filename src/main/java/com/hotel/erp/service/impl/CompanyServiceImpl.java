package com.hotel.erp.service.impl;

import com.hotel.erp.dto.CompanyDTO;
import com.hotel.erp.entity.Company;
import com.hotel.erp.exception.ResourceNotFoundException;
import com.hotel.erp.repository.CompanyRepository;
import com.hotel.erp.service.CompanyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDTO getCompanyById(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return convertToDTO(company);
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = new Company();
        BeanUtils.copyProperties(companyDTO, company);
        Company savedCompany = companyRepository.save(company);
        return convertToDTO(savedCompany);
    }

    @Override
    public CompanyDTO updateCompany(Integer id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));

        BeanUtils.copyProperties(companyDTO, company);
        company.setId(id);
        Company updatedCompany = companyRepository.save(company);
        return convertToDTO(updatedCompany);
    }

    @Override
    public void deleteCompany(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        companyRepository.delete(company);
    }

    private CompanyDTO convertToDTO(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        BeanUtils.copyProperties(company, companyDTO);
        return companyDTO;
    }

    @Override
    public CompanyDTO getCompanyByName(String name) {
        Company company = companyRepository.findByCompanyName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with name: " + name));
        return convertToDTO(company);
    }

    @Override
    public List<CompanyDTO> getCompaniesByGst(String gst) {
        List<Company> companies = companyRepository.findByGst(gst);
        return companies.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
