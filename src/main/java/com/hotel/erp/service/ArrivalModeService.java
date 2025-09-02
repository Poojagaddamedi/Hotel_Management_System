package com.hotel.erp.service;

import com.hotel.erp.dto.ArrivalModeDTO;
import java.util.List;

public interface ArrivalModeService {
    ArrivalModeDTO createArrivalMode(ArrivalModeDTO arrivalModeDTO);

    ArrivalModeDTO updateArrivalMode(Integer id, ArrivalModeDTO arrivalModeDTO);

    ArrivalModeDTO getArrivalModeById(Integer id);

    ArrivalModeDTO getArrivalModeByName(String name);

    List<ArrivalModeDTO> getAllArrivalModes();

    void deleteArrivalMode(Integer id);
}
