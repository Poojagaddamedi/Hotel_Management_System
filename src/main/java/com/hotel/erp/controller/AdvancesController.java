package com.hotel.erp.controller;

import com.hotel.erp.dto.AdvancesDTO;
import com.hotel.erp.entity.Advances;
import com.hotel.erp.service.AdvancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/advances")
public class AdvancesController {

    private static final Logger logger = LoggerFactory.getLogger(AdvancesController.class);

    @Autowired
    private AdvancesService advancesService;

    /**
     * Test endpoint to debug JSON deserialization
     */
    @PostMapping("/test")
    public ResponseEntity<?> testAdvance(@RequestBody Map<String, Object> requestData) {
        logger.info("Received test request with data: {}", requestData);
        return ResponseEntity.ok(Map.of("message", "Test successful", "data", requestData));
    }

    /**
     * Create a new advance
     * 
     * @param requestData the advance data as Map
     * @return the created advance
     */
    @PostMapping
    public ResponseEntity<?> createAdvance(@RequestBody Map<String, Object> requestData) {
        logger.info("Received create advance request with data: {}", requestData);

        try {
            // Validate required fields at controller level
            if (!requestData.containsKey("amount") || requestData.get("amount") == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Amount is required"));
            }

            // Convert Map to DTO manually with robust error handling
            AdvancesDTO advancesDTO = new AdvancesDTO();

            try {
                if (requestData.containsKey("amount") && requestData.get("amount") != null) {
                    try {
                        advancesDTO.setAmount(new java.math.BigDecimal(requestData.get("amount").toString()));
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid amount format: {}", requestData.get("amount"));
                        return ResponseEntity.badRequest().body(Map.of(
                                "status", "error",
                                "message", "Invalid amount format: must be a valid number"));
                    }
                }

                if (requestData.containsKey("paymentDate") && requestData.get("paymentDate") != null) {
                    String paymentDateStr = requestData.get("paymentDate").toString().trim();
                    if (!paymentDateStr.isEmpty()) {
                        try {
                            advancesDTO.setPaymentDate(java.time.LocalDate.parse(paymentDateStr));
                        } catch (Exception e) {
                            logger.warn("Invalid payment date format: {}", paymentDateStr);
                            return ResponseEntity.badRequest().body(Map.of(
                                    "status", "error",
                                    "message", "Invalid payment date format: must be in YYYY-MM-DD format"));
                        }
                    } else {
                        // Default to today if payment date is empty string
                        advancesDTO.setPaymentDate(java.time.LocalDate.now());
                        logger.info("Empty payment date string provided, defaulting to today: {}",
                                advancesDTO.getPaymentDate());
                    }
                } else {
                    // Default to today if payment date not provided
                    advancesDTO.setPaymentDate(java.time.LocalDate.now());
                    logger.info("Setting default payment date to today: {}", advancesDTO.getPaymentDate());
                }

                if (requestData.containsKey("paymentMode") && requestData.get("paymentMode") != null) {
                    advancesDTO.setPaymentMode(requestData.get("paymentMode").toString());
                } else {
                    // Default to Cash if payment mode not provided
                    advancesDTO.setPaymentMode("Cash");
                    logger.info("Setting default payment mode to Cash");
                }

                if (requestData.containsKey("remarks") && requestData.get("remarks") != null) {
                    advancesDTO.setRemarks(requestData.get("remarks").toString());
                }

                if (requestData.containsKey("referenceNo") && requestData.get("referenceNo") != null) {
                    advancesDTO.setReferenceNo(requestData.get("referenceNo").toString());
                }

                if (requestData.containsKey("guestName") && requestData.get("guestName") != null) {
                    advancesDTO.setGuestName(requestData.get("guestName").toString());
                }

                if (requestData.containsKey("folioNo") && requestData.get("folioNo") != null) {
                    advancesDTO.setFolioNo(requestData.get("folioNo").toString());
                }

                if (requestData.containsKey("reservationNo") && requestData.get("reservationNo") != null) {
                    advancesDTO.setReservationNo(requestData.get("reservationNo").toString());
                }

                if (requestData.containsKey("userId") && requestData.get("userId") != null) {
                    try {
                        advancesDTO.setUserId(Integer.valueOf(requestData.get("userId").toString()));
                    } catch (NumberFormatException e) {
                        // Default to user ID 1 if not provided or invalid
                        advancesDTO.setUserId(1);
                    }
                } else {
                    // Default user ID
                    advancesDTO.setUserId(1);
                }

                if (requestData.containsKey("auditDate") && requestData.get("auditDate") != null) {
                    String auditDateStr = requestData.get("auditDate").toString().trim();
                    if (!auditDateStr.isEmpty()) {
                        try {
                            advancesDTO.setAuditDate(java.time.LocalDate.parse(auditDateStr));
                        } catch (Exception e) {
                            // Default to today if parsing fails
                            advancesDTO.setAuditDate(java.time.LocalDate.now());
                            logger.info("Invalid audit date format, defaulting to today: {}",
                                    advancesDTO.getAuditDate());
                        }
                    } else {
                        // Default to today if empty string
                        advancesDTO.setAuditDate(java.time.LocalDate.now());
                        logger.info("Empty audit date string provided, defaulting to today: {}",
                                advancesDTO.getAuditDate());
                    }
                } else {
                    // Default audit date to today
                    advancesDTO.setAuditDate(java.time.LocalDate.now());
                }

                if (requestData.containsKey("shiftNo") && requestData.get("shiftNo") != null) {
                    try {
                        advancesDTO.setShiftNo(Integer.valueOf(requestData.get("shiftNo").toString()));
                    } catch (NumberFormatException e) {
                        // Default to shift 1
                        advancesDTO.setShiftNo(1);
                    }
                }

                if (requestData.containsKey("shiftDate") && requestData.get("shiftDate") != null) {
                    String shiftDateStr = requestData.get("shiftDate").toString().trim();
                    if (!shiftDateStr.isEmpty()) {
                        try {
                            advancesDTO.setShiftDate(java.time.LocalDate.parse(shiftDateStr));
                        } catch (Exception e) {
                            // Default to today if parsing fails
                            advancesDTO.setShiftDate(java.time.LocalDate.now());
                            logger.info("Invalid shift date format, defaulting to today: {}",
                                    advancesDTO.getShiftDate());
                        }
                    } else {
                        // Default to today if empty string
                        advancesDTO.setShiftDate(java.time.LocalDate.now());
                        logger.info("Empty shift date string provided, defaulting to today: {}",
                                advancesDTO.getShiftDate());
                    }
                } else {
                    // Default shift date to today
                    advancesDTO.setShiftDate(java.time.LocalDate.now());
                }

                if (requestData.containsKey("roomNo") && requestData.get("roomNo") != null) {
                    advancesDTO.setRoomNo(requestData.get("roomNo").toString());
                }

                if (requestData.containsKey("billNo") && requestData.get("billNo") != null) {
                    advancesDTO.setBillNo(requestData.get("billNo").toString());
                }

                if (requestData.containsKey("creditCardCompany") && requestData.get("creditCardCompany") != null) {
                    advancesDTO.setCreditCardCompany(requestData.get("creditCardCompany").toString());
                }

                if (requestData.containsKey("creditCardNo") && requestData.get("creditCardNo") != null) {
                    advancesDTO.setCreditCardNo(requestData.get("creditCardNo").toString());
                }
            } catch (Exception e) {
                logger.error("Error parsing request data: {}", e.getMessage(), e);
                return ResponseEntity.badRequest().body("Error parsing request data: " + e.getMessage());
            }

            Advances createdAdvance = advancesService.createAdvance(advancesDTO);
            logger.info("Advance created successfully: {}", createdAdvance);
            return new ResponseEntity<>(Map.of(
                    "status", "success",
                    "message", "Advance created successfully",
                    "data", createdAdvance), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Get all advances
     * 
     * @return list of all advances
     */
    @GetMapping
    public ResponseEntity<?> getAllAdvances() {
        try {
            List<Advances> advances = advancesService.getAllAdvances();
            logger.info("Retrieved {} advances", advances.size());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", advances,
                    "count", advances.size()));
        } catch (Exception e) {
            logger.error("Error retrieving advances: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error retrieving advances: " + e.getMessage()));
        }
    }

    /**
     * Get advance by ID
     * 
     * @param id the advance ID
     * @return the advance if found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdvanceById(@PathVariable Long id) {
        try {
            return advancesService.getAdvanceById(id)
                    .map(advance -> {
                        logger.info("Retrieved advance with ID: {}", id);
                        return ResponseEntity.ok(Map.of(
                                "status", "success",
                                "data", advance));
                    })
                    .orElseGet(() -> {
                        logger.warn("Advance not found with ID: {}", id);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                                "status", "error",
                                "message", "Advance not found with ID: " + id));
                    });
        } catch (Exception e) {
            logger.error("Error retrieving advance with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error retrieving advance: " + e.getMessage()));
        }
    }

    /**
     * Update an existing advance
     * 
     * @param id          the advance ID
     * @param requestData the updated advance data as Map
     * @return the updated advance
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdvance(@PathVariable Long id, @RequestBody Map<String, Object> requestData) {
        logger.info("Received update advance request for ID: {} with data: {}", id, requestData);

        try {
            // First check if the advance exists
            var existingAdvance = advancesService.getAdvanceById(id);
            if (existingAdvance.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Convert Map to DTO manually with robust error handling
            AdvancesDTO advancesDTO = new AdvancesDTO();

            // Set the ID so service knows it's an update
            advancesDTO.setAdvancesId(id);

            try {
                // Pre-populate with existing values for fields that might not be provided
                Advances advance = existingAdvance.get();

                // Only set fields that are provided in the request, otherwise leave them null
                // for the service to handle keeping existing values

                if (requestData.containsKey("amount") && requestData.get("amount") != null) {
                    try {
                        advancesDTO.setAmount(new java.math.BigDecimal(requestData.get("amount").toString()));
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid amount format: {}", requestData.get("amount"));
                        return ResponseEntity.badRequest().body(Map.of(
                                "status", "error",
                                "message", "Invalid amount format: must be a valid number"));
                    }
                }

                if (requestData.containsKey("paymentDate") && requestData.get("paymentDate") != null) {
                    String paymentDateStr = requestData.get("paymentDate").toString().trim();
                    if (!paymentDateStr.isEmpty()) {
                        try {
                            advancesDTO.setPaymentDate(java.time.LocalDate.parse(paymentDateStr));
                        } catch (Exception e) {
                            logger.warn("Invalid payment date format: {}", paymentDateStr);
                            return ResponseEntity.badRequest().body(Map.of(
                                    "status", "error",
                                    "message", "Invalid payment date format: must be in YYYY-MM-DD format"));
                        }
                    } else {
                        // Skip setting payment date if empty string is provided (keep existing value)
                        logger.info("Empty payment date string provided, keeping existing value");
                    }
                }

                if (requestData.containsKey("paymentMode") && requestData.get("paymentMode") != null) {
                    advancesDTO.setPaymentMode(requestData.get("paymentMode").toString());
                }

                if (requestData.containsKey("remarks") && requestData.get("remarks") != null) {
                    advancesDTO.setRemarks(requestData.get("remarks").toString());
                }

                if (requestData.containsKey("referenceNo") && requestData.get("referenceNo") != null) {
                    advancesDTO.setReferenceNo(requestData.get("referenceNo").toString());
                }

                if (requestData.containsKey("guestName") && requestData.get("guestName") != null) {
                    advancesDTO.setGuestName(requestData.get("guestName").toString());
                }

                if (requestData.containsKey("folioNo") && requestData.get("folioNo") != null) {
                    advancesDTO.setFolioNo(requestData.get("folioNo").toString());
                }

                if (requestData.containsKey("reservationNo") && requestData.get("reservationNo") != null) {
                    advancesDTO.setReservationNo(requestData.get("reservationNo").toString());
                }

                if (requestData.containsKey("userId") && requestData.get("userId") != null) {
                    try {
                        advancesDTO.setUserId(Integer.valueOf(requestData.get("userId").toString()));
                    } catch (NumberFormatException e) {
                        // Keep existing userId
                    }
                }
                if (requestData.containsKey("auditDate") && requestData.get("auditDate") != null) {
                    String auditDateStr = requestData.get("auditDate").toString().trim();
                    if (!auditDateStr.isEmpty()) {
                        try {
                            advancesDTO.setAuditDate(java.time.LocalDate.parse(auditDateStr));
                        } catch (Exception e) {
                            // Keep existing auditDate if parsing fails
                            logger.info("Invalid audit date format, keeping existing value");
                        }
                    } else {
                        // Keep existing auditDate if empty string
                        logger.info("Empty audit date string provided, keeping existing value");
                    }
                }

                if (requestData.containsKey("shiftNo") && requestData.get("shiftNo") != null) {
                    try {
                        advancesDTO.setShiftNo(Integer.valueOf(requestData.get("shiftNo").toString()));
                    } catch (NumberFormatException e) {
                        // Keep existing shiftNo
                    }
                }

                if (requestData.containsKey("shiftDate") && requestData.get("shiftDate") != null) {
                    String shiftDateStr = requestData.get("shiftDate").toString().trim();
                    if (!shiftDateStr.isEmpty()) {
                        try {
                            advancesDTO.setShiftDate(java.time.LocalDate.parse(shiftDateStr));
                        } catch (Exception e) {
                            // Keep existing shiftDate if parsing fails
                            logger.info("Invalid shift date format, keeping existing value");
                        }
                    } else {
                        // Keep existing shiftDate if empty string
                        logger.info("Empty shift date string provided, keeping existing value");
                    }
                }

                if (requestData.containsKey("roomNo") && requestData.get("roomNo") != null) {
                    advancesDTO.setRoomNo(requestData.get("roomNo").toString());
                }

                if (requestData.containsKey("billNo") && requestData.get("billNo") != null) {
                    advancesDTO.setBillNo(requestData.get("billNo").toString());
                }

                if (requestData.containsKey("creditCardCompany") && requestData.get("creditCardCompany") != null) {
                    advancesDTO.setCreditCardCompany(requestData.get("creditCardCompany").toString());
                }

                if (requestData.containsKey("creditCardNo") && requestData.get("creditCardNo") != null) {
                    advancesDTO.setCreditCardNo(requestData.get("creditCardNo").toString());
                }
            } catch (Exception e) {
                logger.error("Error parsing request data: {}", e.getMessage(), e);
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "error",
                        "message", "Error parsing request data: " + e.getMessage()));
            }

            Advances updatedAdvance = advancesService.updateAdvance(id, advancesDTO);
            logger.info("Advance updated successfully: {}", updatedAdvance);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Advance updated successfully",
                    "data", updatedAdvance));
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Delete an advance
     * 
     * @param id the advance ID
     * @return response with status message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvance(@PathVariable Long id) {
        try {
            // Check if the advance exists first
            if (!advancesService.getAdvanceById(id).isPresent()) {
                logger.warn("Cannot delete - Advance not found with ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "Advance not found with ID: " + id));
            }

            advancesService.deleteAdvance(id);
            logger.info("Advance deleted successfully with ID: {}", id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Advance deleted successfully"));
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting advance with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()));
        } catch (Exception e) {
            logger.error("Unexpected error deleting advance with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error deleting advance: " + e.getMessage()));
        }
    }

    /**
     * Search advances by query
     * 
     * @param query the search query
     * @return list of advances matching the query
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchAdvances(@RequestParam String query) {
        try {
            List<Advances> advances = advancesService.searchAdvances(query);
            logger.info("Search for '{}' returned {} results", query, advances.size());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", advances,
                    "count", advances.size(),
                    "query", query));
        } catch (Exception e) {
            logger.error("Error searching advances with query '{}': {}", query, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error searching advances: " + e.getMessage()));
        }
    }

    /**
     * Get advances by folio number
     * 
     * @param folioNo the folio number
     * @return list of advances for the folio number
     */
    @GetMapping("/folio/{folioNo}")
    public ResponseEntity<?> getAdvancesByFolioNo(@PathVariable String folioNo) {
        try {
            List<Advances> advances = advancesService.getAdvancesByFolioNo(folioNo);
            logger.info("Found {} advances for folio number: {}", advances.size(), folioNo);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", advances,
                    "count", advances.size(),
                    "folioNo", folioNo));
        } catch (Exception e) {
            logger.error("Error retrieving advances for folio number {}: {}", folioNo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error retrieving advances: " + e.getMessage()));
        }
    }

    /**
     * Get advances by reservation number
     * 
     * @param reservationNo the reservation number
     * @return list of advances for the reservation number
     */
    @GetMapping("/reservation/{reservationNo}")
    public ResponseEntity<?> getAdvancesByReservationNo(@PathVariable String reservationNo) {
        try {
            List<Advances> advances = advancesService.getAdvancesByReservationNo(reservationNo);
            logger.info("Found {} advances for reservation number: {}", advances.size(), reservationNo);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", advances,
                    "count", advances.size(),
                    "reservationNo", reservationNo));
        } catch (Exception e) {
            logger.error("Error retrieving advances for reservation number {}: {}", reservationNo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error retrieving advances: " + e.getMessage()));
        }
    }

    /**
     * Get advances by payment mode
     * 
     * @param paymentMode the payment mode
     * @return list of advances for the payment mode
     */
    @GetMapping("/payment-mode/{paymentMode}")
    public ResponseEntity<?> getAdvancesByPaymentMode(@PathVariable String paymentMode) {
        try {
            List<Advances> advances = advancesService.getAdvancesByPaymentMode(paymentMode);
            logger.info("Found {} advances for payment mode: {}", advances.size(), paymentMode);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", advances,
                    "count", advances.size(),
                    "paymentMode", paymentMode));
        } catch (Exception e) {
            logger.error("Error retrieving advances for payment mode {}: {}", paymentMode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error retrieving advances: " + e.getMessage()));
        }
    }

    /**
     * Get total amount by folio number
     * 
     * @param folioNo the folio number
     * @return total amount for the folio number
     */
    @GetMapping("/total/folio/{folioNo}")
    public ResponseEntity<?> getTotalAmountByFolioNo(@PathVariable String folioNo) {
        try {
            java.math.BigDecimal totalAmount = advancesService.getTotalAmountByFolioNo(folioNo);
            logger.info("Total amount for folio number {}: {}", folioNo, totalAmount);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "totalAmount", totalAmount,
                    "folioNo", folioNo));
        } catch (Exception e) {
            logger.error("Error retrieving total amount for folio number {}: {}", folioNo, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error retrieving total amount: " + e.getMessage()));
        }
    }

    /**
     * Get total amount by reservation number
     * 
     * @param reservationNo the reservation number
     * @return total amount for the reservation number
     */
    @GetMapping("/total/reservation/{reservationNo}")
    public ResponseEntity<?> getTotalAmountByReservationNo(@PathVariable String reservationNo) {
        try {
            java.math.BigDecimal totalAmount = advancesService.getTotalAmountByReservationNo(reservationNo);
            logger.info("Total amount for reservation number {}: {}", reservationNo, totalAmount);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "totalAmount", totalAmount,
                    "reservationNo", reservationNo));
        } catch (Exception e) {
            logger.error("Error retrieving total amount for reservation number {}: {}", reservationNo, e.getMessage(),
                    e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error retrieving total amount: " + e.getMessage()));
        }
    }

    /**
     * Get total amount by payment mode
     * 
     * @param paymentMode the payment mode
     * @return total amount for the payment mode
     */
    @GetMapping("/total/payment-mode/{paymentMode}")
    public ResponseEntity<java.math.BigDecimal> getTotalAmountByPaymentMode(@PathVariable String paymentMode) {
        try {
            java.math.BigDecimal totalAmount = advancesService.getTotalAmountByPaymentMode(paymentMode);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get total amount by date range
     * 
     * @param startDate the start date
     * @param endDate   the end date
     * @return total amount for the date range
     */
    @GetMapping("/total/date-range")
    public ResponseEntity<java.math.BigDecimal> getTotalAmountByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            // Parse date strings with validation
            java.time.LocalDate start, end;
            try {
                startDate = startDate.trim();
                start = java.time.LocalDate.parse(startDate);
            } catch (Exception e) {
                logger.warn("Invalid start date format: {}, using today's date", startDate);
                start = java.time.LocalDate.now();
            }

            try {
                endDate = endDate.trim();
                end = java.time.LocalDate.parse(endDate);
            } catch (Exception e) {
                logger.warn("Invalid end date format: {}, using today's date", endDate);
                end = java.time.LocalDate.now();
            }
            java.math.BigDecimal totalAmount = advancesService.getTotalAmountByDateRange(start, end);
            return ResponseEntity.ok(totalAmount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}