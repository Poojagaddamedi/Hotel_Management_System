package com.hotel.erp.service;

import com.hotel.erp.dto.ShiftDTO;
import java.util.List;

public interface ShiftService {
    ShiftDTO createShift(ShiftDTO shiftDTO);

    ShiftDTO updateShift(Long id, ShiftDTO shiftDTO);

    ShiftDTO getShiftById(Long id);

    List<ShiftDTO> getAllShifts();

    void deleteShift(Long id);

    ShiftDTO getShiftByNumber(Integer shiftNumber);

    ShiftDTO getLatestShift();

    ShiftDTO changeAuditDate(Long id, ShiftDTO shiftDTO);
}
