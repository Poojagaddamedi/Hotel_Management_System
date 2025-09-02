package com.hotel.erp.service;

import com.hotel.erp.dto.ReservationSourceDTO;
import java.util.List;

public interface ReservationSourceService {
    ReservationSourceDTO createReservationSource(ReservationSourceDTO reservationSourceDTO);

    ReservationSourceDTO updateReservationSource(Integer id, ReservationSourceDTO reservationSourceDTO);

    ReservationSourceDTO getReservationSourceById(Integer id);

    ReservationSourceDTO getReservationSourceByName(String name);

    List<ReservationSourceDTO> getAllReservationSources();

    void deleteReservationSource(Integer id);
}
