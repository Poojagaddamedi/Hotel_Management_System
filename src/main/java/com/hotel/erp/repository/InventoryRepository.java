package com.hotel.erp.repository;

import com.hotel.erp.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Find by item code
    Optional<Inventory> findByItemCode(String itemCode);

    // Find by item name
    List<Inventory> findByItemNameContainingIgnoreCase(String itemName);

    // Find by category
    List<Inventory> findByCategory(String category);

    // Find by sub category
    List<Inventory> findBySubCategory(String subCategory);

    // Find by brand
    List<Inventory> findByBrand(String brand);

    // Find by location
    List<Inventory> findByLocation(String location);

    // Find by item status
    List<Inventory> findByItemStatus(String itemStatus);

    // Find by stock status
    List<Inventory> findByStockStatus(String stockStatus);

    // Find low stock items
    @Query("SELECT i FROM Inventory i WHERE i.currentStock <= i.reorderLevel")
    List<Inventory> findLowStockItems();

    // Find out of stock items
    @Query("SELECT i FROM Inventory i WHERE i.currentStock <= 0")
    List<Inventory> findOutOfStockItems();

    // Find overstock items
    @Query("SELECT i FROM Inventory i WHERE i.maximumStock IS NOT NULL AND i.currentStock > i.maximumStock")
    List<Inventory> findOverstockItems();

    // Find items needing reorder
    @Query("SELECT i FROM Inventory i WHERE i.currentStock <= i.reorderLevel AND i.itemStatus = 'ACTIVE'")
    List<Inventory> findItemsNeedingReorder();

    // Find perishable items
    List<Inventory> findByIsPerishableTrue();

    // Find critical items
    List<Inventory> findByIsCriticalTrue();

    // Find items by expiry date range
    @Query("SELECT i FROM Inventory i WHERE i.isPerishable = true AND i.expiryDate BETWEEN :startDate AND :endDate")
    List<Inventory> findItemsByExpiryDateRange(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // Find expired items
    @Query("SELECT i FROM Inventory i WHERE i.isPerishable = true AND i.expiryDate < :currentDate")
    List<Inventory> findExpiredItems(@Param("currentDate") LocalDate currentDate);

    // Find items expiring soon (within specified days)
    @Query("SELECT i FROM Inventory i WHERE i.isPerishable = true AND i.expiryDate BETWEEN :currentDate AND :expiryDate")
    List<Inventory> findItemsExpiringSoon(@Param("currentDate") LocalDate currentDate,
            @Param("expiryDate") LocalDate expiryDate);

    // Find assets
    List<Inventory> findByIsAssetTrue();

    // Find items by supplier
    List<Inventory> findBySupplierNameContainingIgnoreCase(String supplierName);

    // Get total inventory value
    @Query("SELECT COALESCE(SUM(i.totalValue), 0) FROM Inventory i WHERE i.itemStatus = 'ACTIVE'")
    BigDecimal getTotalInventoryValue();

    // Get total inventory value by category
    @Query("SELECT COALESCE(SUM(i.totalValue), 0) FROM Inventory i WHERE i.category = :category AND i.itemStatus = 'ACTIVE'")
    BigDecimal getTotalInventoryValueByCategory(@Param("category") String category);

    // Find items with low stock value
    @Query("SELECT i FROM Inventory i WHERE i.currentStock * i.unitCost < :minValue")
    List<Inventory> findItemsWithLowStockValue(@Param("minValue") BigDecimal minValue);

    // Find items by multiple filters
    @Query("SELECT i FROM Inventory i WHERE " +
            "(:itemCode IS NULL OR i.itemCode = :itemCode) AND " +
            "(:itemName IS NULL OR LOWER(i.itemName) LIKE LOWER(CONCAT('%', :itemName, '%'))) AND " +
            "(:category IS NULL OR i.category = :category) AND " +
            "(:subCategory IS NULL OR i.subCategory = :subCategory) AND " +
            "(:location IS NULL OR i.location = :location) AND " +
            "(:stockStatus IS NULL OR i.stockStatus = :stockStatus) AND " +
            "(:itemStatus IS NULL OR i.itemStatus = :itemStatus)")
    List<Inventory> findByMultipleFilters(
            @Param("itemCode") String itemCode,
            @Param("itemName") String itemName,
            @Param("category") String category,
            @Param("subCategory") String subCategory,
            @Param("location") String location,
            @Param("stockStatus") String stockStatus,
            @Param("itemStatus") String itemStatus);

    // Get inventory statistics by category
    @Query("SELECT i.category, COUNT(i), SUM(i.currentStock), SUM(i.totalValue) FROM Inventory i " +
            "WHERE i.itemStatus = 'ACTIVE' GROUP BY i.category ORDER BY i.category")
    List<Object[]> getInventoryStatisticsByCategory();

    // Get inventory statistics by location
    @Query("SELECT i.location, COUNT(i), SUM(i.currentStock), SUM(i.totalValue) FROM Inventory i " +
            "WHERE i.itemStatus = 'ACTIVE' GROUP BY i.location ORDER BY i.location")
    List<Object[]> getInventoryStatisticsByLocation();

    // Get top items by value
    @Query("SELECT i FROM Inventory i WHERE i.itemStatus = 'ACTIVE' ORDER BY i.totalValue DESC")
    List<Inventory> findTopItemsByValue();

    // Get items with zero consumption
    @Query("SELECT i FROM Inventory i WHERE i.totalConsumed = 0 AND i.itemStatus = 'ACTIVE'")
    List<Inventory> findItemsWithZeroConsumption();

    // Get fast moving items (high consumption)
    @Query("SELECT i FROM Inventory i WHERE i.averageConsumption > :threshold AND i.itemStatus = 'ACTIVE' ORDER BY i.averageConsumption DESC")
    List<Inventory> findFastMovingItems(@Param("threshold") BigDecimal threshold);

    // Get slow moving items (low consumption)
    @Query("SELECT i FROM Inventory i WHERE i.averageConsumption < :threshold AND i.averageConsumption > 0 AND i.itemStatus = 'ACTIVE' ORDER BY i.averageConsumption ASC")
    List<Inventory> findSlowMovingItems(@Param("threshold") BigDecimal threshold);

    // Find items due for maintenance
    @Query("SELECT i FROM Inventory i WHERE i.isAsset = true AND i.maintenanceDueDate <= :currentDate")
    List<Inventory> findItemsDueForMaintenance(@Param("currentDate") LocalDate currentDate);

    // Find items with warranty expiring
    @Query("SELECT i FROM Inventory i WHERE i.isAsset = true AND i.warrantyDate BETWEEN :currentDate AND :expiryDate")
    List<Inventory> findItemsWithWarrantyExpiring(@Param("currentDate") LocalDate currentDate,
            @Param("expiryDate") LocalDate expiryDate);
}
