package com.hotel.erp.service;

import com.hotel.erp.dto.BillSettlementDTO;
import com.hotel.erp.dto.FoBillDTO;
import com.hotel.erp.entity.AdditionalCharges;
import com.hotel.erp.entity.Advances;
import com.hotel.erp.entity.BillSettlement;
import com.hotel.erp.entity.Checkin;
import com.hotel.erp.entity.FoBill;
import com.hotel.erp.exception.ResourceNotFoundException;
import com.hotel.erp.repository.AdditionalChargesRepository;
import com.hotel.erp.repository.AdvancesRepository;
import com.hotel.erp.repository.BillSettlementRepository;
import com.hotel.erp.repository.CheckinRepository;
import com.hotel.erp.repository.FoBillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BillingServiceImpl extends BillingService {

    @Autowired
    private FoBillRepository foBillRepository;

    @Autowired
    private BillSettlementRepository billSettlementRepository;

    @Autowired
    private AdvancesRepository advancesRepository;

    @Autowired
    private AdditionalChargesRepository additionalChargesRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    @Transactional
    public FoBill createBill(FoBillDTO billDTO) {
        FoBill bill = new FoBill();
        bill.setFolioNo(billDTO.getFolioNo());
        bill.setTotalAmount(billDTO.getTotalAmount());
        bill.setBillDate(billDTO.getBillDate());
        bill.setUserId(billDTO.getUserId());

        return foBillRepository.save(bill);
    }

    @Transactional
    public BillSettlement createSettlement(BillSettlementDTO settlementDTO) {
        FoBill bill = foBillRepository.findById(settlementDTO.getBillId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Bill not found with id: " + settlementDTO.getBillId()));

        BillSettlement settlement = new BillSettlement();
        // Use setter methods that match the actual entity properties
        // Add appropriate setters based on the actual BillSettlement entity

        return billSettlementRepository.save(settlement);
    }

    public Map<String, Object> getBillSummary(String folioNo) {
        List<FoBill> bills = foBillRepository.findByFolioNo(folioNo);
        if (bills.isEmpty()) {
            throw new ResourceNotFoundException("No bills found for folio: " + folioNo);
        }

        FoBill bill = bills.get(0); // Get the first bill for this folio

        List<BillSettlement> settlements = billSettlementRepository.findByBillId(bill.getBillId());
        List<AdditionalCharges> additionalCharges = additionalChargesRepository.findByFolioNo(folioNo);
        List<Advances> advances = advancesRepository.findByFolioNo(folioNo);

        Optional<Checkin> checkin = checkinRepository.findByFolioNo(folioNo);

        BigDecimal totalSettlements = settlements.stream()
                .map(s -> new BigDecimal("0")) // Use getter method for amount when available
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAdditionalCharges = additionalCharges.stream()
                .map(ac -> new BigDecimal("0")) // Use getter method for amount when available
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAdvances = advances.stream()
                .map(a -> new BigDecimal("0")) // Use getter method for amount when available
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balanceDue = bill.getTotalAmount()
                .add(totalAdditionalCharges)
                .subtract(totalSettlements)
                .subtract(totalAdvances);

        Map<String, Object> summary = new HashMap<>();
        summary.put("bill", bill);
        summary.put("settlements", settlements);
        summary.put("additionalCharges", additionalCharges);
        summary.put("advances", advances);
        summary.put("totalSettlements", totalSettlements);
        summary.put("totalAdditionalCharges", totalAdditionalCharges);
        summary.put("totalAdvances", totalAdvances);
        summary.put("balanceDue", balanceDue);

        if (checkin.isPresent()) {
            summary.put("checkin", checkin.get());
        }

        return summary;
    }

    public BigDecimal calculateBillTotal(String folioNo) {
        List<FoBill> bills = foBillRepository.findByFolioNo(folioNo);
        if (bills.isEmpty()) {
            throw new ResourceNotFoundException("No bills found for folio: " + folioNo);
        }

        FoBill bill = bills.get(0); // Get the first bill for this folio

        List<AdditionalCharges> additionalCharges = additionalChargesRepository.findByFolioNo(folioNo);

        BigDecimal totalAdditionalCharges = additionalCharges.stream()
                .map(ac -> new BigDecimal("0")) // Use getter method for amount when available
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return bill.getTotalAmount().add(totalAdditionalCharges);
    }

    public List<FoBill> getBillsByFolio(String folioNo) {
        return foBillRepository.findByFolioNo(folioNo);
    }

    public List<BillSettlement> getSettlementsByBill(Long billId) {
        return billSettlementRepository.findByBillId(billId);
    }

    public Map<String, Object> getBillingStats() {
        Map<String, Object> stats = new HashMap<>();

        // Total bills count
        stats.put("totalBills", foBillRepository.count());

        // Total settlements
        stats.put("totalSettlements", "Not implemented yet");

        // Active bills count
        stats.put("activeBillsCount", foBillRepository.count());

        // Total outstanding amount
        stats.put("totalOutstandingAmount", "Not implemented yet");

        return stats;
    }

    public List<FoBill> getAllBills() {
        return foBillRepository.findAll();
    }

    public List<BillSettlement> getAllSettlements() {
        return billSettlementRepository.findAll();
    }

    @Transactional
    public FoBill updateBill(Long billId, FoBillDTO billDTO) {
        FoBill bill = foBillRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));

        bill.setFolioNo(billDTO.getFolioNo());
        bill.setTotalAmount(billDTO.getTotalAmount());
        bill.setBillDate(billDTO.getBillDate());
        bill.setUserId(billDTO.getUserId());

        return foBillRepository.save(bill);
    }

    @Transactional
    public BillSettlement updateSettlement(Long settlementId, BillSettlementDTO settlementDTO) {
        BillSettlement settlement = billSettlementRepository.findById(settlementId)
                .orElseThrow(() -> new ResourceNotFoundException("Settlement not found with id: " + settlementId));

        // Update settlement properties using available setters

        return billSettlementRepository.save(settlement);
    }

    @Transactional
    public void deleteBill(Long billId) {
        FoBill bill = foBillRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));

        foBillRepository.delete(bill);
    }

    @Transactional
    public void deleteSettlement(Long settlementId) {
        BillSettlement settlement = billSettlementRepository.findById(settlementId)
                .orElseThrow(() -> new ResourceNotFoundException("Settlement not found with id: " + settlementId));

        billSettlementRepository.delete(settlement);
    }
}
