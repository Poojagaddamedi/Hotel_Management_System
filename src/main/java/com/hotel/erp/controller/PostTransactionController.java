package com.hotel.erp.controller;

import com.hotel.erp.dto.PostTransactionDTO;
import com.hotel.erp.entity.PostTransaction;
import com.hotel.erp.service.PostTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/post-transactions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(PostTransactionController.class);

    @Autowired
    private PostTransactionService postTransactionService;

    @GetMapping
    public ResponseEntity<List<PostTransaction>> getAllTransactions() {
        return ResponseEntity.ok(postTransactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostTransaction> getTransactionById(@PathVariable Long id) {
        return postTransactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Helper method to safely convert map values to string
    private String safeGetString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    // Helper method to safely convert map values to Integer
    private Integer safeGetInteger(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? Integer.valueOf(value.toString()) : null;
    }

    // Helper method to safely convert map values to LocalDate
    private LocalDate safeGetLocalDate(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? LocalDate.parse(value.toString()) : null;
    }

    // Helper method to safely convert map values to BigDecimal
    private BigDecimal safeGetBigDecimal(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? new BigDecimal(value.toString()) : null;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody Map<String, Object> requestData) {
        logger.info("Received create post transaction request with data: {}", requestData);

        try {
            // Convert Map to DTO manually with null safety
            PostTransactionDTO transactionDTO = new PostTransactionDTO();

            if (requestData.containsKey("amount")) {
                BigDecimal amount = safeGetBigDecimal(requestData, "amount");
                if (amount != null) {
                    transactionDTO.setAmount(amount);
                }
            }

            if (requestData.containsKey("transDate")) {
                LocalDate transDate = safeGetLocalDate(requestData, "transDate");
                if (transDate != null) {
                    transactionDTO.setTransDate(transDate);
                }
            }

            if (requestData.containsKey("accHead")) {
                String accHead = safeGetString(requestData, "accHead");
                if (accHead != null) {
                    transactionDTO.setAccHead(accHead);
                }
            }

            if (requestData.containsKey("narration")) {
                String narration = safeGetString(requestData, "narration");
                if (narration != null) {
                    transactionDTO.setNarration(narration);
                }
            }

            if (requestData.containsKey("guestName")) {
                String guestName = safeGetString(requestData, "guestName");
                if (guestName != null) {
                    transactionDTO.setGuestName(guestName);
                }
            }

            if (requestData.containsKey("folioNo")) {
                String folioNo = safeGetString(requestData, "folioNo");
                if (folioNo != null && !folioNo.trim().isEmpty()) {
                    transactionDTO.setFolioNo(folioNo);
                } else {
                    logger.warn("FolioNo is null or empty, this may cause issues in financial tracking");
                }
            }

            if (requestData.containsKey("userId")) {
                Integer userId = safeGetInteger(requestData, "userId");
                if (userId != null) {
                    transactionDTO.setUserId(userId);
                }
            }

            if (requestData.containsKey("roomNo")) {
                String roomNo = safeGetString(requestData, "roomNo");
                if (roomNo != null) {
                    transactionDTO.setRoomNo(roomNo);
                }
            }

            if (requestData.containsKey("auditDate")) {
                LocalDate auditDate = safeGetLocalDate(requestData, "auditDate");
                if (auditDate != null) {
                    transactionDTO.setAuditDate(auditDate);
                }
            }

            if (requestData.containsKey("voucherNo")) {
                String voucherNo = safeGetString(requestData, "voucherNo");
                if (voucherNo != null) {
                    transactionDTO.setVoucherNo(voucherNo);
                }
            }

            if (requestData.containsKey("billNo")) {
                String billNo = safeGetString(requestData, "billNo");
                if (billNo != null) {
                    transactionDTO.setBillNo(billNo);
                }
            }

            if (requestData.containsKey("reservationNo")) {
                String reservationNo = safeGetString(requestData, "reservationNo");
                if (reservationNo != null) {
                    transactionDTO.setReservationNo(reservationNo);
                }
            }

            if (requestData.containsKey("transactionStatus")) {
                String transactionStatus = safeGetString(requestData, "transactionStatus");
                if (transactionStatus != null) {
                    transactionDTO.setTransactionStatus(transactionStatus);
                }
            }

            if (requestData.containsKey("transactionType")) {
                String transactionType = safeGetString(requestData, "transactionType");
                if (transactionType != null) {
                    transactionDTO.setTransactionType(transactionType);
                }
            }

            if (requestData.containsKey("customerType")) {
                String customerType = safeGetString(requestData, "customerType");
                if (customerType != null) {
                    transactionDTO.setCustomerType(customerType);
                }
            }

            if (requestData.containsKey("shiftNo")) {
                Integer shiftNo = safeGetInteger(requestData, "shiftNo");
                if (shiftNo != null) {
                    transactionDTO.setShiftNo(shiftNo);
                }
            }

            if (requestData.containsKey("shiftDate")) {
                LocalDate shiftDate = safeGetLocalDate(requestData, "shiftDate");
                if (shiftDate != null) {
                    transactionDTO.setShiftDate(shiftDate);
                }
            }

            logger.info("Converted DTO: {}", transactionDTO);

            // Create the transaction
            PostTransaction savedTransaction = postTransactionService.createTransaction(transactionDTO);

            logger.info("Successfully created post transaction with ID: {}", savedTransaction.getId());

            return new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);

        } catch (NumberFormatException e) {
            logger.error("Number format error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid number format: " + e.getMessage());
        } catch (java.time.format.DateTimeParseException e) {
            logger.error("Date format error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id, @RequestBody Map<String, Object> requestData) {
        logger.info("Received update post transaction request for ID: {} with data: {}", id, requestData);

        try {
            return postTransactionService.getTransactionById(id)
                    .map(existingTransaction -> {
                        try {
                            // Convert Map to DTO manually with null safety
                            PostTransactionDTO transactionDTO = new PostTransactionDTO();
                            transactionDTO.setId(id);

                            // Copy existing values first
                            transactionDTO.setAmount(existingTransaction.getAmount());
                            transactionDTO.setTransDate(existingTransaction.getTransDate());
                            transactionDTO.setAccHead(existingTransaction.getAccHead());
                            transactionDTO.setNarration(existingTransaction.getNarration());
                            transactionDTO.setGuestName(existingTransaction.getGuestName());
                            transactionDTO.setFolioNo(existingTransaction.getFolioNo());
                            transactionDTO.setUserId(existingTransaction.getUserId());
                            transactionDTO.setRoomNo(existingTransaction.getRoomNo());
                            transactionDTO.setAuditDate(existingTransaction.getAuditDate());
                            transactionDTO.setVoucherNo(existingTransaction.getVoucherNo());
                            transactionDTO.setBillNo(existingTransaction.getBillNo());
                            transactionDTO.setReservationNo(existingTransaction.getReservationNo());
                            transactionDTO.setTransactionStatus(existingTransaction.getTransactionStatus().toString());
                            transactionDTO.setTransactionType(existingTransaction.getTransactionType());
                            transactionDTO.setCustomerType(existingTransaction.getCustomerType());
                            transactionDTO.setShiftNo(existingTransaction.getShiftNo());
                            transactionDTO.setShiftDate(existingTransaction.getShiftDate());

                            // Update with new values
                            if (requestData.containsKey("amount")) {
                                BigDecimal amount = safeGetBigDecimal(requestData, "amount");
                                if (amount != null) {
                                    transactionDTO.setAmount(amount);
                                }
                            }

                            if (requestData.containsKey("transDate")) {
                                LocalDate transDate = safeGetLocalDate(requestData, "transDate");
                                if (transDate != null) {
                                    transactionDTO.setTransDate(transDate);
                                }
                            }

                            if (requestData.containsKey("accHead")) {
                                String accHead = safeGetString(requestData, "accHead");
                                if (accHead != null) {
                                    transactionDTO.setAccHead(accHead);
                                }
                            }

                            if (requestData.containsKey("folioNo")) {
                                String folioNo = safeGetString(requestData, "folioNo");
                                if (folioNo != null) {
                                    transactionDTO.setFolioNo(folioNo);
                                }
                            }

                            // Continue with other fields as needed...

                            PostTransaction updatedTransaction = postTransactionService
                                    .createTransaction(transactionDTO);

                            logger.info("Successfully updated post transaction with ID: {}", id);

                            return ResponseEntity.ok(updatedTransaction);

                        } catch (Exception e) {
                            logger.error("Error updating transaction: {}", e.getMessage());
                            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error updating transaction: " + e.getMessage());
                        }
                    })
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            logger.error("Unexpected error in updateTransaction: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        return postTransactionService.getTransactionById(id)
                .map(transaction -> {
                    postTransactionService.deleteTransaction(id);
                    return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/folio/{folioNo}")
    public ResponseEntity<List<PostTransaction>> getTransactionsByFolioNo(@PathVariable String folioNo) {
        return ResponseEntity.ok(postTransactionService.getTransactionsByFolioNo(folioNo));
    }

    @GetMapping("/reservation/{reservationNo}")
    public ResponseEntity<List<PostTransaction>> getTransactionsByReservationNo(@PathVariable String reservationNo) {
        return ResponseEntity.ok(postTransactionService.getTransactionsByReservationNo(reservationNo));
    }

    @GetMapping("/guest/{guestName}")
    public ResponseEntity<List<PostTransaction>> getTransactionsByGuestName(@PathVariable String guestName) {
        return ResponseEntity.ok(postTransactionService.getTransactionsByGuestName(guestName));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PostTransaction>> getTransactionsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(postTransactionService.getTransactionsBetweenDates(startDate, endDate));
    }

    @GetMapping("/account-head/{accHead}")
    public ResponseEntity<List<PostTransaction>> getTransactionsByAccHead(@PathVariable String accHead) {
        return ResponseEntity.ok(postTransactionService.getTransactionsByAccHead(accHead));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostTransaction>> getTransactionsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(postTransactionService.getTransactionsByUserId(userId));
    }

    @GetMapping("/room/{roomNo}")
    public ResponseEntity<List<PostTransaction>> getTransactionsByRoomNo(@PathVariable String roomNo) {
        return ResponseEntity.ok(postTransactionService.getTransactionsByRoomNo(roomNo));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PostTransaction>> getTransactionsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(postTransactionService.getTransactionsByStatus(status));
    }

    @GetMapping("/total/folio/{folioNo}")
    public ResponseEntity<BigDecimal> getTotalAmountByFolioNo(@PathVariable String folioNo) {
        return ResponseEntity.ok(postTransactionService.getTotalAmountByFolioNo(folioNo));
    }

    @GetMapping("/total/reservation/{reservationNo}")
    public ResponseEntity<BigDecimal> getTotalAmountByReservationNo(@PathVariable String reservationNo) {
        return ResponseEntity.ok(postTransactionService.getTotalAmountByReservationNo(reservationNo));
    }

    @GetMapping("/total/guest/{guestName}")
    public ResponseEntity<BigDecimal> getTotalAmountByGuestName(@PathVariable String guestName) {
        return ResponseEntity.ok(postTransactionService.getTotalAmountByGuestName(guestName));
    }

    @GetMapping("/count/folio/{folioNo}")
    public ResponseEntity<Long> countTransactionsByFolioNo(@PathVariable String folioNo) {
        return ResponseEntity.ok(postTransactionService.countTransactionsByFolioNo(folioNo));
    }

    @GetMapping("/balance/folio/{folioNo}")
    public ResponseEntity<BigDecimal> getBalanceByFolioNo(@PathVariable String folioNo) {
        return ResponseEntity.ok(postTransactionService.getBalanceByFolioNo(folioNo));
    }

    /**
     * Health check endpoint to test if controller is loaded
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("PostTransaction Controller is working!");
    }

    /**
     * Simple test endpoint without service dependency
     */
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        return ResponseEntity.ok("PostTransaction Controller test successful - no service dependency");
    }
}
