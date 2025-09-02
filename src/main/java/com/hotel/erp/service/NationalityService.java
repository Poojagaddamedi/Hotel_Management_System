package com.hotel.erp.service;

import com.hotel.erp.dto.NationalityDTO;
import java.util.List;

public interface NationalityService {
    NationalityDTO createNationality(NationalityDTO nationalityDTO);

    NationalityDTO updateNationality(Integer id, NationalityDTO nationalityDTO);

    NationalityDTO getNationalityById(Integer id);

    NationalityDTO getNationalityByCountryName(String countryName);

    NationalityDTO getNationalityByCountryCode(String countryCode);

    List<NationalityDTO> getAllNationalities();

    void deleteNationality(Integer id);
}
