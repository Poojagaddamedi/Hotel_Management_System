package com.hotel.erp.repository;

import com.hotel.erp.entity.PostTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PostTransactionRepository extends JpaRepository<PostTransaction, Long> {

    /**
     * Find transactions by folio number
     */
    List<PostTransaction> findByFolioNo(String folioNo);

    /**
     * Find transactions by guest name
     */
    List<PostTransaction> findByGuestNameContainingIgnoreCase(String guestName);

    /**
     * Find transactions by account head
     */
    List<PostTransaction> findByAccHead(String accHead);

    /**
     * Find transactions by account head (case insensitive search)
     */
    List<PostTransaction> findByAccHeadContainingIgnoreCase(String accHead);

    /**
     * Find transactions by room number
     */
    List<PostTransaction> findByRoomNo(String roomNo);

    /**
     * Find transactions by reservation number
     */
    List<PostTransaction> findByReservationNo(String reservationNo);

    /**
     * Find transactions by transaction date
     */
    List<PostTransaction> findByTransDate(LocalDate transDate);

    /**
     * Find transactions between dates
     */
    List<PostTransaction> findByTransDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find transactions by status
     */
    List<PostTransaction> findByTransactionStatus(PostTransaction.TransactionStatus status);

    /**
     * Find transactions by user ID
     */
    List<PostTransaction> findByUserId(Integer userId);

    /**
     * Find transactions by bill number
     */
    List<PostTransaction> findByBillNo(String billNo);

    /**
     * Search transactions by query (folio, guest name, or narration)
     */
    @Query("SELECT pt FROM PostTransaction pt WHERE " +
            "pt.folioNo LIKE %:query% OR " +
            "pt.guestName LIKE %:query% OR " +
            "pt.narration LIKE %:query% OR " +
            "pt.accHead LIKE %:query%")
    List<PostTransaction> searchTransactions(@Param("query") String query);

    /**
     * Get total amount by folio number
     */
    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PostTransaction pt WHERE pt.folioNo = :folioNo")
    BigDecimal getTotalAmountByFolioNo(@Param("folioNo") String folioNo);

    /**
     * Get total amount by account head
     */
    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PostTransaction pt WHERE pt.accHead = :accHead")
    BigDecimal getTotalAmountByAccHead(@Param("accHead") String accHead);

    /**
     * Get total amount by date range
     */
    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PostTransaction pt WHERE pt.transDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * Get total amount by folio number and account head
     */
    @Query("SELECT COALESCE(SUM(pt.amount), 0) FROM PostTransaction pt WHERE pt.folioNo = :folioNo AND pt.accHead = :accHead")
    BigDecimal getTotalAmountByFolioNoAndAccHead(@Param("folioNo") String folioNo, @Param("accHead") String accHead);

    /**
     * Get transactions by folio number and account head
     */
    List<PostTransaction> findByFolioNoAndAccHead(String folioNo, String accHead);

    /**
     * Get transactions by folio number and transaction status
     */
    List<PostTransaction> findByFolioNoAndTransactionStatus(String folioNo, PostTransaction.TransactionStatus status);

    /**
     * Get pending transactions by folio number
     */
    @Query("SELECT pt FROM PostTransaction pt WHERE pt.folioNo = :folioNo AND pt.transactionStatus = 'Pending'")
    List<PostTransaction> findPendingTransactionsByFolioNo(@Param("folioNo") String folioNo);

    /**
     * Get completed transactions by folio number
     */
    @Query("SELECT pt FROM PostTransaction pt WHERE pt.folioNo = :folioNo AND pt.transactionStatus = 'Completed'")
    List<PostTransaction> findCompletedTransactionsByFolioNo(@Param("folioNo") String folioNo);
}