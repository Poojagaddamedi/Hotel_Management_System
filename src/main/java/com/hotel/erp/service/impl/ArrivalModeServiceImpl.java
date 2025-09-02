package com.hotel.erp.service.impl;

import com.hotel.erp.dto.ArrivalModeDTO;
import com.hotel.erp.entity.ArrivalMode;
import com.hotel.erp.repository.ArrivalModeRepository;
import com.hotel.erp.service.ArrivalModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArrivalModeServiceImpl implements ArrivalModeService {

    private final ArrivalModeRepository arrivalModeRepository;

    @Autowired
    public ArrivalModeServiceImpl(ArrivalModeRepository arrivalModeRepository) {
        this.arrivalModeRepository = arrivalModeRepository;
    }

    @Override
    public ArrivalModeDTO createArrivalMode(ArrivalModeDTO arrivalModeDTO) {
        ArrivalMode arrivalMode = new ArrivalMode();
        arrivalMode.setArrivalMode(arrivalModeDTO.getArrivalMode());
        ArrivalMode savedArrivalMode = arrivalModeRepository.save(arrivalMode);
        return convertToDTO(savedArrivalMode);
    }

    @Override
    public ArrivalModeDTO updateArrivalMode(Integer id, ArrivalModeDTO arrivalModeDTO) {
        ArrivalMode arrivalMode = arrivalModeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrival Mode not found with id: " + id));

        arrivalMode.setArrivalMode(arrivalModeDTO.getArrivalMode());
        ArrivalMode updatedArrivalMode = arrivalModeRepository.save(arrivalMode);
        return convertToDTO(updatedArrivalMode);
    }

    @Override
    public ArrivalModeDTO getArrivalModeById(Integer id) {
        ArrivalMode arrivalMode = arrivalModeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Arrival Mode not found with id: " + id));
        return convertToDTO(arrivalMode);
    }

    @Override
    public List<ArrivalModeDTO> getAllArrivalModes() {
        return arrivalModeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ArrivalModeDTO getArrivalModeByName(String name) {
        ArrivalMode arrivalMode = arrivalModeRepository.findByArrivalMode(name)
                .orElseThrow(() -> new RuntimeException("Arrival Mode not found with name: " + name));
        return convertToDTO(arrivalMode);
    }

    @Override
    public void deleteArrivalMode(Integer id) {
        if (!arrivalModeRepository.existsById(id)) {
            throw new RuntimeException("Arrival Mode not found with id: " + id);
        }
        arrivalModeRepository.deleteById(id);
    }

    private ArrivalModeDTO convertToDTO(ArrivalMode arrivalMode) {
        ArrivalModeDTO dto = new ArrivalModeDTO();
        dto.setArrId(arrivalMode.getArrId());
        dto.setArrivalMode(arrivalMode.getArrivalMode());
        return dto;
    }
}