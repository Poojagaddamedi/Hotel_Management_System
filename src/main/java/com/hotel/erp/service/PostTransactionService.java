package com.hotel.erp.service;

import com.hotel.erp.dto.PostTransactionDTO;
import com.hotel.erp.entity.PostTransaction;
import com.hotel.erp.entity.Checkin;
import com.hotel.erp.repository.PostTransactionRepository;
import com.hotel.erp.repository.CheckinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PostTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(PostTransactionService.class);

    @Autowired
    private PostTransactionRepository postTransactionRepository;

    @Autowired
    private CheckinRepository checkinRepository;

    /**
     * Get all post transactions
     */
    public List<PostTransaction> getAllTransactions() {
        return postTransactionRepository.findAll();
    }

    /**
     * Get transaction by ID
     */
    public Optional<PostTransaction> getTransactionById(Long id) {
        return postTransactionRepository.findById(id);
    }

    /**
     * Create a new post transaction
     */
    public PostTransaction createTransaction(PostTransactionDTO transactionDTO) {
        logger.info("Creating post transaction with data: {}", transactionDTO);

        try {
            validateTransactionData(transactionDTO);
            logger.info("Validation passed for transaction creation");

            PostTransaction transaction = new PostTransaction();
            transaction.setRoomNo(transactionDTO.getRoomNo());
            transaction.setAuditDate(transactionDTO.getAuditDate());
            transaction.setGuestName(transactionDTO.getGuestName());
            transaction.setFolioNo(transactionDTO.getFolioNo());
            transaction.setTransDate(transactionDTO.getTransDate());
            transaction.setAccHead(transactionDTO.getAccHead());
            transaction.setVoucherNo(transactionDTO.getVoucherNo());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setNarration(transactionDTO.getNarration());
            transaction.setBillNo(transactionDTO.getBillNo());
            transaction.setUserId(transactionDTO.getUserId());

            if (transactionDTO.getTransactionStatus() != null) {
                transaction.setTransactionStatus(
                        PostTransaction.TransactionStatus.valueOf(transactionDTO.getTransactionStatus()));
            }

            PostTransaction savedTransaction = postTransactionRepository.save(transaction);
            logger.info("Post transaction created successfully with ID: {}", savedTransaction.getId());
            return savedTransaction;

        } catch (Exception e) {
            logger.error("Error creating post transaction: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Update an existing transaction
     */
    public PostTransaction updateTransaction(Long id, PostTransactionDTO transactionDTO) {
        logger.info("Updating post transaction with ID: {} and data: {}", id, transactionDTO);

        try {
            Optional<PostTransaction> existingTransaction = postTransactionRepository.findById(id);
            if (existingTransaction.isEmpty()) {
                throw new IllegalArgumentException("Post transaction not found with ID: " + id);
            }

            validateTransactionData(transactionDTO);
            logger.info("Validation passed for transaction update");

            PostTransaction transaction = existingTransaction.get();
            transaction.setRoomNo(transactionDTO.getRoomNo());
            transaction.setAuditDate(transactionDTO.getAuditDate());
            transaction.setGuestName(transactionDTO.getGuestName());
            transaction.setFolioNo(transactionDTO.getFolioNo());
            transaction.setTransDate(transactionDTO.getTransDate());
            transaction.setAccHead(transactionDTO.getAccHead());
            transaction.setVoucherNo(transactionDTO.getVoucherNo());
            transaction.setAmount(transactionDTO.getAmount());
            transaction.setNarration(transactionDTO.getNarration());
            transaction.setBillNo(transactionDTO.getBillNo());
            transaction.setUserId(transactionDTO.getUserId());

            if (transactionDTO.getTransactionStatus() != null) {
                transaction.setTransactionStatus(
                        PostTransaction.TransactionStatus.valueOf(transactionDTO.getTransactionStatus()));
            }

            PostTransaction savedTransaction = postTransactionRepository.save(transaction);
            logger.info("Post transaction updated successfully with ID: {}", savedTransaction.getId());
            return savedTransaction;

        } catch (Exception e) {
            logger.error("Error updating post transaction: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Delete a transaction
     */
    public void deleteTransaction(Long id) {
        if (!postTransactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Post transaction not found with ID: " + id);
        }
        postTransactionRepository.deleteById(id);
    }

    /**
     * Search transactions by query
     */
    public List<PostTransaction> searchTransactions(String query) {
        return postTransactionRepository.searchTransactions(query);
    }

    /**
     * Get transactions by folio number
     */
    public List<PostTransaction> getTransactionsByFolioNo(String folioNo) {
        return postTransactionRepository.findByFolioNo(folioNo);
    }

    /**
     * Get transactions by guest name
     */
    public List<PostTransaction> getTransactionsByGuestName(String guestName) {
        return postTransactionRepository.findByGuestNameContainingIgnoreCase(guestName);
    }

    /**
     * Get transactions by account head
     */
    public List<PostTransaction> getTransactionsByAccHead(String accHead) {
        return postTransactionRepository.findByAccHead(accHead);
    }

    /**
     * Get transactions by date range
     */
    public List<PostTransaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate) {
        return postTransactionRepository.findByTransDateBetween(startDate, endDate);
    }

    /**
     * Get total amount by folio number
     */
    public BigDecimal getTotalAmountByFolioNo(String folioNo) {
        return postTransactionRepository.getTotalAmountByFolioNo(folioNo);
    }

    /**
     * Get total amount by account head
     */
    public BigDecimal getTotalAmountByAccHead(String accHead) {
        return postTransactionRepository.getTotalAmountByAccHead(accHead);
    }

    /**
     * Get total amount by date range
     */
    public BigDecimal getTotalAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return postTransactionRepository.getTotalAmountByDateRange(startDate, endDate);
    }

    /**
     * Get total amount by folio number and account head
     */
    public BigDecimal getTotalAmountByFolioNoAndAccHead(String folioNo, String accHead) {
        return postTransactionRepository.getTotalAmountByFolioNoAndAccHead(folioNo, accHead);
    }

    /**
     * Get pending transactions by folio number
     */
    public List<PostTransaction> getPendingTransactionsByFolioNo(String folioNo) {
        return postTransactionRepository.findPendingTransactionsByFolioNo(folioNo);
    }

    /**
     * Get completed transactions by folio number
     */
    public List<PostTransaction> getCompletedTransactionsByFolioNo(String folioNo) {
        return postTransactionRepository.findCompletedTransactionsByFolioNo(folioNo);
    }

    /**
     * Calculate outstanding amount for a folio (total transactions minus advances)
     */
    public BigDecimal calculateOutstandingAmount(String folioNo) {
        BigDecimal totalTransactions = getTotalAmountByFolioNo(folioNo);
        // Note: This would need to be integrated with AdvancesService to get total
        // advances
        // For now, returning total transactions
        return totalTransactions;
    }

    /**
     * Get total amount by reservation number
     */
    public BigDecimal getTotalAmountByReservationNo(String reservationNo) {
        List<PostTransaction> transactions = getTransactionsByReservationNo(reservationNo);
        return transactions.stream()
                .map(PostTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Get total amount by guest name
     */
    public BigDecimal getTotalAmountByGuestName(String guestName) {
        List<PostTransaction> transactions = getTransactionsByGuestName(guestName);
        return transactions.stream()
                .map(PostTransaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Count transactions by folio number
     */
    public long countTransactionsByFolioNo(String folioNo) {
        return getTransactionsByFolioNo(folioNo).size();
    }

    /**
     * Get balance by folio number (total advance - total transactions)
     */
    public BigDecimal getBalanceByFolioNo(String folioNo) {
        BigDecimal totalTransactions = getTotalAmountByFolioNo(folioNo);
        // Note: This would need to be integrated with AdvancesService to get total
        // advances
        // For now, returning negative total transactions as balance
        return totalTransactions.negate();
    }

    /**
     * Validate transaction data
     */
    private void validateTransactionData(PostTransactionDTO transactionDTO) {
        logger.info("Validating transaction data: {}", transactionDTO);

        // Validate mandatory fields
        if (transactionDTO.getAmount() == null || transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Validation failed: Amount must be positive");
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (transactionDTO.getTransDate() == null) {
            logger.error("Validation failed: Transaction date is required");
            throw new IllegalArgumentException("Transaction date is required");
        }

        if (transactionDTO.getTransDate().isAfter(LocalDate.now())) {
            logger.error("Validation failed: Transaction date cannot be in the future");
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }

        if (transactionDTO.getAccHead() == null || transactionDTO.getAccHead().trim().isEmpty()) {
            logger.error("Validation failed: Account head is required");
            throw new IllegalArgumentException("Account head is required");
        }

        // Validate folio number if provided
        if (transactionDTO.getFolioNo() != null && !transactionDTO.getFolioNo().trim().isEmpty()) {
            Optional<Checkin> checkin = checkinRepository.findByFolioNo(transactionDTO.getFolioNo());
            if (checkin.isEmpty()) {
                logger.error("Validation failed: Folio number does not exist: {}", transactionDTO.getFolioNo());
                throw new IllegalArgumentException("Folio number does not exist in checkin table");
            }

            // Validate transaction date aligns with checkin arrival date
            Checkin checkinData = checkin.get();
            if (checkinData.getCheckInDate() != null
                    && transactionDTO.getTransDate().isBefore(checkinData.getCheckInDate())) {
                logger.error("Validation failed: Transaction date cannot be before check-in date");
                throw new IllegalArgumentException("Transaction date cannot be before check-in date");
            }
        }

        logger.info("Validation passed successfully");
    }

    /**
     * Get transactions by reservation number
     */
    public List<PostTransaction> getTransactionsByReservationNo(String reservationNo) {
        logger.info("Fetching transactions for reservation number: {}", reservationNo);
        return postTransactionRepository.findByReservationNo(reservationNo);
    }

    /**
     * Get transactions between dates (alias for getTransactionsByDateRange)
     */
    public List<PostTransaction> getTransactionsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return getTransactionsByDateRange(startDate, endDate);
    }

    /**
     * Get transactions by user ID
     */
    public List<PostTransaction> getTransactionsByUserId(Integer userId) {
        logger.info("Fetching transactions for user ID: {}", userId);
        return postTransactionRepository.findByUserId(userId);
    }

    /**
     * Get transactions by room number
     */
    public List<PostTransaction> getTransactionsByRoomNo(String roomNo) {
        logger.info("Fetching transactions for room number: {}", roomNo);
        return postTransactionRepository.findByRoomNo(roomNo);
    }

    /**
     * Get transactions by status
     */
    public List<PostTransaction> getTransactionsByStatus(String status) {
        logger.info("Fetching transactions for status: {}", status);
        PostTransaction.TransactionStatus transactionStatus = PostTransaction.TransactionStatus
                .valueOf(status.toUpperCase());
        return postTransactionRepository.findByTransactionStatus(transactionStatus);
    }

}