package com.hotel.erp.controller;

import com.hotel.erp.dto.PaymentReportDTO;
import com.hotel.erp.dto.FoBillDTO;
import com.hotel.erp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportController {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    /**
     * Get payment summaries
     * 
     * @param dateFrom      the start date
     * @param dateTo        the end date
     * @param paymentMode   the payment mode (optional)
     * @param folioNo       the folio number (optional)
     * @param reservationNo the reservation number (optional)
     * @param guestName     the guest name (optional)
     * @return List of payment summaries
     */
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentReportDTO>> getPaymentSummaries(
            @RequestParam String dateFrom,
            @RequestParam String dateTo,
            @RequestParam(required = false) String paymentMode,
            @RequestParam(required = false) String folioNo,
            @RequestParam(required = false) String reservationNo,
            @RequestParam(required = false) String guestName) {

        logger.info("Getting payment summaries from {} to {}", dateFrom, dateTo);

        try {
            LocalDate startDate = LocalDate.parse(dateFrom);
            LocalDate endDate = LocalDate.parse(dateTo);

            List<PaymentReportDTO> summaries = reportService.getPaymentSummaries(
                    startDate, endDate, paymentMode, folioNo, reservationNo, guestName);

            return ResponseEntity.ok(summaries);

        } catch (Exception e) {
            logger.error("Error getting payment summaries: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get bill summaries
     * 
     * @param dateFrom the start date
     * @param dateTo   the end date
     * @param folioNo  the folio number (optional)
     * @return List of bill summaries
     */
    @GetMapping("/bills")
    public ResponseEntity<List<FoBillDTO>> getBillSummaries(
            @RequestParam String dateFrom,
            @RequestParam String dateTo,
            @RequestParam(required = false) String folioNo) {

        logger.info("Getting bill summaries from {} to {}", dateFrom, dateTo);

        try {
            LocalDate startDate = LocalDate.parse(dateFrom);
            LocalDate endDate = LocalDate.parse(dateTo);

            List<FoBillDTO> summaries = reportService.getBillSummaries(startDate, endDate, folioNo);

            return ResponseEntity.ok(summaries);

        } catch (Exception e) {
            logger.error("Error getting bill summaries: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get charge summaries
     * 
     * @param dateFrom   the start date
     * @param dateTo     the end date
     * @param chargeType the charge type (optional)
     * @return List of charge summaries
     */
    @GetMapping("/charges")
    public ResponseEntity<List<Map<String, Object>>> getChargeSummaries(
            @RequestParam String dateFrom,
            @RequestParam String dateTo,
            @RequestParam(required = false) String chargeType) {

        logger.info("Getting charge summaries from {} to {}", dateFrom, dateTo);

        try {
            LocalDate startDate = LocalDate.parse(dateFrom);
            LocalDate endDate = LocalDate.parse(dateTo);

            List<Map<String, Object>> summaries = reportService.getChargeSummaries(
                    startDate, endDate, chargeType);

            return ResponseEntity.ok(summaries);

        } catch (Exception e) {
            logger.error("Error getting charge summaries: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get financial summary
     * 
     * @param dateFrom the start date
     * @param dateTo   the end date
     * @return Financial summary
     */
    @GetMapping("/financial")
    public ResponseEntity<Map<String, Object>> getFinancialSummary(
            @RequestParam String dateFrom,
            @RequestParam String dateTo) {

        logger.info("Getting financial summary from {} to {}", dateFrom, dateTo);

        try {
            LocalDate startDate = LocalDate.parse(dateFrom);
            LocalDate endDate = LocalDate.parse(dateTo);

            Map<String, Object> summary = reportService.getFinancialSummary(startDate, endDate);

            return ResponseEntity.ok(summary);

        } catch (Exception e) {
            logger.error("Error getting financial summary: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get room occupancy report
     */
    @GetMapping("/room-occupancy")
    public ResponseEntity<Map<String, Object>> getRoomOccupancyReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        logger.info("Getting room occupancy report from {} to {}", startDate, endDate);

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            Map<String, Object> report = reportService.getRoomOccupancyReport(start, end);

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            logger.error("Error getting room occupancy report: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get payment summary report
     */
    @GetMapping("/payment-summary")
    public ResponseEntity<Map<String, Object>> getPaymentSummaryReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        logger.info("Getting payment summary report from {} to {}", startDate, endDate);

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            Map<String, Object> report = reportService.getPaymentSummaryReport(start, end);

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            logger.error("Error getting payment summary report: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get financial summary report
     */
    @GetMapping("/financial-summary")
    public ResponseEntity<Map<String, Object>> getFinancialSummaryReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        logger.info("Getting financial summary report from {} to {}", startDate, endDate);

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            Map<String, Object> report = reportService.getFinancialSummaryReport(start, end);

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            logger.error("Error getting financial summary report: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Get daily summary report
     */
    @GetMapping("/daily-summary")
    public ResponseEntity<Map<String, Object>> getDailySummaryReport(
            @RequestParam String date) {

        logger.info("Getting daily summary report for {}", date);

        try {
            LocalDate reportDate = LocalDate.parse(date);

            Map<String, Object> report = reportService.getDailySummaryReport(reportDate);

            return ResponseEntity.ok(report);

        } catch (Exception e) {
            logger.error("Error getting daily summary report: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }
}