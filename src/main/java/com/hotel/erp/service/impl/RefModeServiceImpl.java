package com.hotel.erp.service.impl;

import com.hotel.erp.dto.RefModeDTO;
import com.hotel.erp.entity.RefMode;
import com.hotel.erp.repository.RefModeRepository;
import com.hotel.erp.service.RefModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefModeServiceImpl implements RefModeService {

    private final RefModeRepository refModeRepository;

    @Autowired
    public RefModeServiceImpl(RefModeRepository refModeRepository) {
        this.refModeRepository = refModeRepository;
    }

    @Override
    public RefModeDTO createRefMode(RefModeDTO refModeDTO) {
        RefMode refMode = new RefMode();
        refMode.setRefMode(refModeDTO.getRefMode());
        RefMode savedRefMode = refModeRepository.save(refMode);
        return convertToDTO(savedRefMode);
    }

    @Override
    public RefModeDTO updateRefMode(Integer id, RefModeDTO refModeDTO) {
        RefMode refMode = refModeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ref Mode not found with id: " + id));

        refMode.setRefMode(refModeDTO.getRefMode());
        RefMode updatedRefMode = refModeRepository.save(refMode);
        return convertToDTO(updatedRefMode);
    }

    @Override
    public RefModeDTO getRefModeById(Integer id) {
        RefMode refMode = refModeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ref Mode not found with id: " + id));
        return convertToDTO(refMode);
    }

    @Override
    public List<RefModeDTO> getAllRefModes() {
        return refModeRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RefModeDTO getRefModeByName(String name) {
        RefMode refMode = refModeRepository.findByRefMode(name)
                .orElseThrow(() -> new RuntimeException("Ref Mode not found with name: " + name));
        return convertToDTO(refMode);
    }

    @Override
    public void deleteRefMode(Integer id) {
        if (!refModeRepository.existsById(id)) {
            throw new RuntimeException("Ref Mode not found with id: " + id);
        }
        refModeRepository.deleteById(id);
    }

    private RefModeDTO convertToDTO(RefMode refMode) {
        RefModeDTO dto = new RefModeDTO();
        dto.setRefId(refMode.getRefId());
        dto.setRefMode(refMode.getRefMode());
        return dto;
    }
}