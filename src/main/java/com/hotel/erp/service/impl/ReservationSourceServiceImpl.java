package com.hotel.erp.service.impl;

import com.hotel.erp.dto.ReservationSourceDTO;
import com.hotel.erp.entity.ReservationSource;
import com.hotel.erp.repository.ReservationSourceRepository;
import com.hotel.erp.service.ReservationSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationSourceServiceImpl implements ReservationSourceService {

    private final ReservationSourceRepository reservationSourceRepository;

    @Autowired
    public ReservationSourceServiceImpl(ReservationSourceRepository reservationSourceRepository) {
        this.reservationSourceRepository = reservationSourceRepository;
    }

    @Override
    public ReservationSourceDTO createReservationSource(ReservationSourceDTO reservationSourceDTO) {
        ReservationSource reservationSource = new ReservationSource();
        reservationSource.setResSource(reservationSourceDTO.getResSource());
        ReservationSource savedReservationSource = reservationSourceRepository.save(reservationSource);
        return convertToDTO(savedReservationSource);
    }

    @Override
    public ReservationSourceDTO updateReservationSource(Integer id, ReservationSourceDTO reservationSourceDTO) {
        ReservationSource reservationSource = reservationSourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation Source not found with id: " + id));

        reservationSource.setResSource(reservationSourceDTO.getResSource());
        ReservationSource updatedReservationSource = reservationSourceRepository.save(reservationSource);
        return convertToDTO(updatedReservationSource);
    }

    @Override
    public ReservationSourceDTO getReservationSourceById(Integer id) {
        ReservationSource reservationSource = reservationSourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation Source not found with id: " + id));
        return convertToDTO(reservationSource);
    }

    @Override
    public List<ReservationSourceDTO> getAllReservationSources() {
        return reservationSourceRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationSourceDTO getReservationSourceByName(String name) {
        ReservationSource reservationSource = reservationSourceRepository.findByResSource(name)
                .orElseThrow(() -> new RuntimeException("Reservation Source not found with name: " + name));
        return convertToDTO(reservationSource);
    }

    @Override
    public void deleteReservationSource(Integer id) {
        if (!reservationSourceRepository.existsById(id)) {
            throw new RuntimeException("Reservation Source not found with id: " + id);
        }
        reservationSourceRepository.deleteById(id);
    }

    private ReservationSourceDTO convertToDTO(ReservationSource reservationSource) {
        ReservationSourceDTO dto = new ReservationSourceDTO();
        dto.setResId(reservationSource.getResId());
        dto.setResSource(reservationSource.getResSource());
        return dto;
    }
}