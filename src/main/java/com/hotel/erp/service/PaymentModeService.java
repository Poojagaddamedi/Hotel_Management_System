package com.hotel.erp.service;

import com.hotel.erp.dto.PaymentModeDTO;
import java.util.List;

public interface PaymentModeService {
    PaymentModeDTO createPaymentMode(PaymentModeDTO paymentModeDTO);

    PaymentModeDTO updatePaymentMode(Integer id, PaymentModeDTO paymentModeDTO);

    PaymentModeDTO getPaymentModeById(Integer id);

    PaymentModeDTO getPaymentModeByType(String type);

    List<PaymentModeDTO> getAllPaymentModes();

    void deletePaymentMode(Integer id);
}
