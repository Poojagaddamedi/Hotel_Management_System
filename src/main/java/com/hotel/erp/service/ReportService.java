package com.hotel.erp.service;

import com.hotel.erp.dto.PaymentReportDTO;
import com.hotel.erp.dto.FoBillDTO;
import com.hotel.erp.entity.Advances;
import com.hotel.erp.entity.FoBill;
import com.hotel.erp.entity.BillSettlement;
import com.hotel.erp.entity.AdditionalCharges;
import com.hotel.erp.repository.AdvancesRepository;
import com.hotel.erp.repository.FoBillRepository;
import com.hotel.erp.repository.BillSettlementRepository;
import com.hotel.erp.repository.AdditionalChargesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    private AdvancesRepository advancesRepository;

    @Autowired
    private FoBillRepository foBillRepository;

    @Autowired
    private BillSettlementRepository billSettlementRepository;

    @Autowired
    private AdditionalChargesRepository additionalChargesRepository;

    /**
     * Get payment summaries by date range and payment mode
     * 
     * @param startDate     the start date
     * @param endDate       the end date
     * @param paymentMode   the payment mode (optional)
     * @param folioNo       the folio number (optional)
     * @param reservationNo the reservation number (optional)
     * @param guestName     the guest name (optional)
     * @return List of payment summaries
     */
    public List<PaymentReportDTO> getPaymentSummaries(LocalDate startDate, LocalDate endDate,
            String paymentMode, String folioNo,
            String reservationNo, String guestName) {
        logger.info("Generating payment summaries from {} to {}", startDate, endDate);

        try {
            List<Advances> advances = advancesRepository.findByPaymentDateBetween(startDate, endDate);

            // Filter by additional criteria
            if (paymentMode != null && !paymentMode.trim().isEmpty()) {
                advances = advances.stream()
                        .filter(a -> paymentMode.equals(a.getPaymentMode()))
                        .collect(Collectors.toList());
            }

            if (folioNo != null && !folioNo.trim().isEmpty()) {
                advances = advances.stream()
                        .filter(a -> folioNo.equals(a.getFolioNo()))
                        .collect(Collectors.toList());
            }

            if (reservationNo != null && !reservationNo.trim().isEmpty()) {
                advances = advances.stream()
                        .filter(a -> reservationNo.equals(a.getReservationNo()))
                        .collect(Collectors.toList());
            }

            if (guestName != null && !guestName.trim().isEmpty()) {
                advances = advances.stream()
                        .filter(a -> guestName.equals(a.getGuestName()))
                        .collect(Collectors.toList());
            }

            // Group by date and payment mode
            Map<String, List<Advances>> groupedAdvances = advances.stream()
                    .collect(Collectors.groupingBy(a -> a.getPaymentDate() + "|" + a.getPaymentMode()));

            List<PaymentReportDTO> summaries = new ArrayList<>();

            for (Map.Entry<String, List<Advances>> entry : groupedAdvances.entrySet()) {
                String[] parts = entry.getKey().split("\\|");
                LocalDate reportDate = LocalDate.parse(parts[0]);
                String mode = parts[1];

                List<Advances> dayAdvances = entry.getValue();
                BigDecimal totalAmount = dayAdvances.stream()
                        .map(Advances::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                PaymentReportDTO summary = new PaymentReportDTO();
                summary.setReportDate(reportDate);
                summary.setTotalAmount(totalAmount);
                summary.setPaymentMode(mode);
                summary.setTransactionCount(dayAdvances.size());

                // Set guest info from first advance
                if (!dayAdvances.isEmpty()) {
                    Advances firstAdvance = dayAdvances.get(0);
                    summary.setFolioNo(firstAdvance.getFolioNo());
                    summary.setReservationNo(firstAdvance.getReservationNo());
                    summary.setGuestName(firstAdvance.getGuestName());
                }

                summaries.add(summary);
            }

            logger.info("Generated {} payment summaries", summaries.size());
            return summaries;

        } catch (Exception e) {
            logger.error("Error generating payment summaries: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get bill summaries by date range and folio number
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @param folioNo   the folio number (optional)
     * @return List of bill summaries
     */
    public List<FoBillDTO> getBillSummaries(LocalDate startDate, LocalDate endDate, String folioNo) {
        logger.info("Generating bill summaries from {} to {}", startDate, endDate);

        try {
            List<FoBill> bills = foBillRepository.findByBillDateBetween(startDate, endDate);

            // Filter by folio number if provided
            if (folioNo != null && !folioNo.trim().isEmpty()) {
                bills = bills.stream()
                        .filter(b -> folioNo.equals(b.getFolioNo()))
                        .collect(Collectors.toList());
            }

            List<FoBillDTO> summaries = bills.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            logger.info("Generated {} bill summaries", summaries.size());
            return summaries;

        } catch (Exception e) {
            logger.error("Error generating bill summaries: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get charge summaries by date range and charge type
     * 
     * @param startDate  the start date
     * @param endDate    the end date
     * @param chargeType the charge type (optional)
     * @return List of charge summaries
     */
    public List<Map<String, Object>> getChargeSummaries(LocalDate startDate, LocalDate endDate, String chargeType) {
        logger.info("Generating charge summaries from {} to {}", startDate, endDate);

        try {
            List<AdditionalCharges> charges = additionalChargesRepository.findByChargeDateBetween(startDate, endDate);

            // Filter by charge type if provided
            if (chargeType != null && !chargeType.trim().isEmpty()) {
                charges = charges.stream()
                        .filter(c -> chargeType.equals(c.getChargeType()))
                        .collect(Collectors.toList());
            }

            // Group by charge type
            Map<String, List<AdditionalCharges>> groupedCharges = charges.stream()
                    .collect(Collectors.groupingBy(AdditionalCharges::getChargeType));

            List<Map<String, Object>> summaries = new ArrayList<>();

            for (Map.Entry<String, List<AdditionalCharges>> entry : groupedCharges.entrySet()) {
                String type = entry.getKey();
                List<AdditionalCharges> typeCharges = entry.getValue();

                BigDecimal totalAmount = typeCharges.stream()
                        .map(AdditionalCharges::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> summary = new HashMap<>();
                summary.put("chargeType", type);
                summary.put("totalAmount", totalAmount);
                summary.put("transactionCount", typeCharges.size());
                summary.put("charges", typeCharges);

                summaries.add(summary);
            }

            logger.info("Generated {} charge summaries", summaries.size());
            return summaries;

        } catch (Exception e) {
            logger.error("Error generating charge summaries: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    /**
     * Get financial summary for a specific date range
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return Financial summary map
     */
    public Map<String, Object> getFinancialSummary(LocalDate startDate, LocalDate endDate) {
        logger.info("Generating financial summary from {} to {}", startDate, endDate);

        try {
            Map<String, Object> summary = new HashMap<>();

            // Total advances
            BigDecimal totalAdvances = advancesRepository.getTotalAmountByDateRange(startDate, endDate);
            summary.put("totalAdvances", totalAdvances);

            // Total bills
            BigDecimal totalBills = foBillRepository.getTotalAmountByDateRange(startDate, endDate);
            summary.put("totalBills", totalBills);

            // Total settlements
            BigDecimal totalSettlements = billSettlementRepository.getTotalAmountByDateRange(startDate, endDate);
            summary.put("totalSettlements", totalSettlements);

            // Total charges
            BigDecimal totalCharges = additionalChargesRepository.getTotalAmountByDateRange(startDate, endDate);
            summary.put("totalCharges", totalCharges);

            // Outstanding amount
            BigDecimal outstanding = totalBills.add(totalCharges).subtract(totalSettlements);
            summary.put("outstandingAmount", outstanding);

            // Payment mode breakdown
            List<Advances> advances = advancesRepository.findByPaymentDateBetween(startDate, endDate);
            Map<String, BigDecimal> paymentModeBreakdown = advances.stream()
                    .collect(Collectors.groupingBy(
                            Advances::getPaymentMode,
                            Collectors.reducing(BigDecimal.ZERO, Advances::getAmount, BigDecimal::add)));
            summary.put("paymentModeBreakdown", paymentModeBreakdown);

            logger.info("Generated financial summary successfully");
            return summary;

        } catch (Exception e) {
            logger.error("Error generating financial summary: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Convert FoBill entity to DTO
     * 
     * @param foBill the FoBill entity
     * @return FoBillDTO
     */
    private FoBillDTO convertToDTO(FoBill foBill) {
        FoBillDTO dto = new FoBillDTO();
        dto.setBillId(foBill.getBillId());
        dto.setFolioNo(foBill.getFolioNo());
        dto.setTotalAmount(foBill.getTotalAmount());
        dto.setBillDate(foBill.getBillDate());
        dto.setUserId(foBill.getUserId());
        return dto;
    }

    /**
     * Get room occupancy report
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return Room occupancy report
     */
    public Map<String, Object> getRoomOccupancyReport(LocalDate startDate, LocalDate endDate) {
        logger.info("Generating room occupancy report from {} to {}", startDate, endDate);

        try {
            Map<String, Object> report = new HashMap<>();

            // Mock data for room occupancy - you would integrate with actual room service
            report.put("totalRooms", 50);
            report.put("occupiedRooms", 35);
            report.put("availableRooms", 15);
            report.put("occupancyRate", 70.0);
            report.put("period", startDate.toString() + " to " + endDate.toString());

            logger.info("Generated room occupancy report successfully");
            return report;

        } catch (Exception e) {
            logger.error("Error generating room occupancy report: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Get payment summary report
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return Payment summary report
     */
    public Map<String, Object> getPaymentSummaryReport(LocalDate startDate, LocalDate endDate) {
        logger.info("Generating payment summary report from {} to {}", startDate, endDate);

        try {
            Map<String, Object> report = new HashMap<>();

            // Get payment summaries
            List<PaymentReportDTO> summaries = getPaymentSummaries(startDate, endDate, null, null, null, null);

            BigDecimal totalPayments = summaries.stream()
                    .map(PaymentReportDTO::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, BigDecimal> paymentModeBreakdown = summaries.stream()
                    .collect(Collectors.groupingBy(
                            PaymentReportDTO::getPaymentMode,
                            Collectors.reducing(BigDecimal.ZERO, PaymentReportDTO::getTotalAmount, BigDecimal::add)));

            report.put("totalPayments", totalPayments);
            report.put("paymentCount", summaries.size());
            report.put("paymentModeBreakdown", paymentModeBreakdown);
            report.put("period", startDate.toString() + " to " + endDate.toString());

            logger.info("Generated payment summary report successfully");
            return report;

        } catch (Exception e) {
            logger.error("Error generating payment summary report: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Get financial summary report
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return Financial summary report
     */
    public Map<String, Object> getFinancialSummaryReport(LocalDate startDate, LocalDate endDate) {
        logger.info("Generating financial summary report from {} to {}", startDate, endDate);

        try {
            // This can reuse the existing getFinancialSummary method
            Map<String, Object> report = getFinancialSummary(startDate, endDate);
            report.put("period", startDate.toString() + " to " + endDate.toString());
            report.put("reportType", "financial_summary");

            logger.info("Generated financial summary report successfully");
            return report;

        } catch (Exception e) {
            logger.error("Error generating financial summary report: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Get daily summary report
     * 
     * @param reportDate the report date
     * @return Daily summary report
     */
    public Map<String, Object> getDailySummaryReport(LocalDate reportDate) {
        logger.info("Generating daily summary report for {}", reportDate);

        try {
            Map<String, Object> report = new HashMap<>();

            // Total advances for the day
            BigDecimal dailyAdvances = advancesRepository.getTotalAmountByDate(reportDate);
            report.put("dailyAdvances", dailyAdvances);

            // Total bills for the day
            BigDecimal dailyBills = foBillRepository.getTotalAmountByDate(reportDate);
            report.put("dailyBills", dailyBills);

            // Total settlements for the day
            BigDecimal dailySettlements = billSettlementRepository.getTotalAmountByDate(reportDate);
            report.put("dailySettlements", dailySettlements);

            // Total charges for the day
            BigDecimal dailyCharges = additionalChargesRepository.getTotalAmountByDate(reportDate);
            report.put("dailyCharges", dailyCharges);

            // Net revenue for the day
            BigDecimal netRevenue = dailyBills.add(dailyCharges);
            report.put("netRevenue", netRevenue);

            // Transaction counts
            int advanceCount = advancesRepository.countByPaymentDate(reportDate);
            int billCount = foBillRepository.countByBillDate(reportDate);
            int settlementCount = billSettlementRepository.countByPaymentDate(reportDate);

            report.put("advanceCount", advanceCount);
            report.put("billCount", billCount);
            report.put("settlementCount", settlementCount);
            report.put("reportDate", reportDate.toString());

            logger.info("Generated daily summary report successfully");
            return report;

        } catch (Exception e) {
            logger.error("Error generating daily summary report: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }
}