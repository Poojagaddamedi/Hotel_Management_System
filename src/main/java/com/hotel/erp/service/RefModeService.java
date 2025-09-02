package com.hotel.erp.service;

import com.hotel.erp.dto.RefModeDTO;
import java.util.List;

public interface RefModeService {
    RefModeDTO createRefMode(RefModeDTO refModeDTO);

    RefModeDTO updateRefMode(Integer id, RefModeDTO refModeDTO);

    RefModeDTO getRefModeById(Integer id);

    RefModeDTO getRefModeByName(String name);

    List<RefModeDTO> getAllRefModes();

    void deleteRefMode(Integer id);
}
