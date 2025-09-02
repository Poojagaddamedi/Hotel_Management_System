package com.hotel.erp.service;

import com.hotel.erp.dto.TaxDTO;
import java.util.List;

public interface TaxService {
    TaxDTO createTax(TaxDTO taxDTO);

    TaxDTO updateTax(Integer id, TaxDTO taxDTO);

    TaxDTO getTaxById(Integer id);

    List<TaxDTO> getAllTaxes();

    void deleteTax(Integer id);
}
