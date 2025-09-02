package com.hotel.erp.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code", nullable = false, unique = true, length = 50)
    private String itemCode;

    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @Column(name = "item_description", length = 300)
    private String itemDescription;

    @Column(name = "category", nullable = false, length = 50)
    private String category; // HOUSEKEEPING, F&B, MAINTENANCE, OFFICE, AMENITIES

    @Column(name = "sub_category", length = 50)
    private String subCategory;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "unit_of_measure", nullable = false, length = 20)
    private String unitOfMeasure; // PCS, KG, LITER, BOX, etc.

    @Column(name = "current_stock", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentStock = BigDecimal.ZERO;

    @Column(name = "minimum_stock", nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumStock = BigDecimal.ZERO;

    @Column(name = "maximum_stock", precision = 10, scale = 2)
    private BigDecimal maximumStock = BigDecimal.ZERO;

    @Column(name = "reorder_level", nullable = false, precision = 10, scale = 2)
    private BigDecimal reorderLevel = BigDecimal.ZERO;

    @Column(name = "reorder_quantity", precision = 10, scale = 2)
    private BigDecimal reorderQuantity = BigDecimal.ZERO;

    @Column(name = "unit_cost", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(name = "total_value", precision = 10, scale = 2)
    private BigDecimal totalValue = BigDecimal.ZERO;

    @Column(name = "supplier_name", length = 100)
    private String supplierName;

    @Column(name = "supplier_contact", length = 50)
    private String supplierContact;

    @Column(name = "last_purchase_date")
    private LocalDate lastPurchaseDate;

    @Column(name = "last_purchase_price", precision = 10, scale = 2)
    private BigDecimal lastPurchasePrice = BigDecimal.ZERO;

    @Column(name = "location", nullable = false, length = 100)
    private String location; // Storage location

    @Column(name = "shelf_number", length = 20)
    private String shelfNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Column(name = "serial_number", length = 50)
    private String serialNumber;

    @Column(name = "is_perishable", nullable = false)
    private Boolean isPerishable = false;

    @Column(name = "is_critical", nullable = false)
    private Boolean isCritical = false;

    @Column(name = "item_status", nullable = false, length = 20)
    private String itemStatus = "ACTIVE"; // ACTIVE, INACTIVE, DISCONTINUED

    @Column(name = "stock_status", length = 20)
    private String stockStatus = "IN_STOCK"; // IN_STOCK, LOW_STOCK, OUT_OF_STOCK, OVERSTOCK

    @Column(name = "last_count_date")
    private LocalDate lastCountDate;

    @Column(name = "last_count_quantity", precision = 10, scale = 2)
    private BigDecimal lastCountQuantity = BigDecimal.ZERO;

    @Column(name = "variance_quantity", precision = 10, scale = 2)
    private BigDecimal varianceQuantity = BigDecimal.ZERO;

    @Column(name = "total_consumed", precision = 10, scale = 2)
    private BigDecimal totalConsumed = BigDecimal.ZERO;

    @Column(name = "total_received", precision = 10, scale = 2)
    private BigDecimal totalReceived = BigDecimal.ZERO;

    @Column(name = "average_consumption", precision = 10, scale = 2)
    private BigDecimal averageConsumption = BigDecimal.ZERO;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays = 0;

    @Column(name = "is_asset", nullable = false)
    private Boolean isAsset = false;

    @Column(name = "asset_tag", length = 50)
    private String assetTag;

    @Column(name = "warranty_date")
    private LocalDate warrantyDate;

    @Column(name = "maintenance_due_date")
    private LocalDate maintenanceDueDate;

    @Column(name = "barcode", length = 50)
    private String barcode;

    @Column(name = "qr_code", length = 100)
    private String qrCode;

    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @Column(name = "specifications", length = 1000)
    private String specifications;

    @Column(name = "usage_instructions", length = 500)
    private String usageInstructions;

    @Column(name = "safety_notes", length = 500)
    private String safetyNotes;

    @Column(name = "allocated_quantity", precision = 10, scale = 2)
    private BigDecimal allocatedQuantity = BigDecimal.ZERO;

    @Column(name = "available_quantity", precision = 10, scale = 2)
    private BigDecimal availableQuantity = BigDecimal.ZERO;

    @Column(name = "reserved_quantity", precision = 10, scale = 2)
    private BigDecimal reservedQuantity = BigDecimal.ZERO;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "audit_date", nullable = false)
    private LocalDate auditDate;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.auditDate == null) {
            this.auditDate = LocalDate.now();
        }

        // Auto-generate item code if not provided
        if (this.itemCode == null || this.itemCode.isEmpty()) {
            this.itemCode = "ITM_" + System.currentTimeMillis();
        }

        calculateStockValues();
        updateStockStatus();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();
        calculateStockValues();
        updateStockStatus();
    }

    // Helper method to calculate stock values
    public void calculateStockValues() {
        this.totalValue = this.currentStock.multiply(this.unitCost);
        this.availableQuantity = this.currentStock.subtract(this.allocatedQuantity).subtract(this.reservedQuantity);
        if (this.availableQuantity.compareTo(BigDecimal.ZERO) < 0) {
            this.availableQuantity = BigDecimal.ZERO;
        }
    }

    // Helper method to update stock status
    public void updateStockStatus() {
        if (this.currentStock.compareTo(BigDecimal.ZERO) <= 0) {
            this.stockStatus = "OUT_OF_STOCK";
        } else if (this.currentStock.compareTo(this.reorderLevel) <= 0) {
            this.stockStatus = "LOW_STOCK";
        } else if (this.maximumStock != null && this.currentStock.compareTo(this.maximumStock) > 0) {
            this.stockStatus = "OVERSTOCK";
        } else {
            this.stockStatus = "IN_STOCK";
        }
    }

    // Helper method to add stock
    public void addStock(BigDecimal quantity, BigDecimal unitPrice) {
        this.currentStock = this.currentStock.add(quantity);
        this.totalReceived = this.totalReceived.add(quantity);
        this.lastPurchasePrice = unitPrice;
        this.lastPurchaseDate = LocalDate.now();
        calculateStockValues();
        updateStockStatus();
    }

    // Helper method to consume stock
    public boolean consumeStock(BigDecimal quantity) {
        if (this.availableQuantity.compareTo(quantity) >= 0) {
            this.currentStock = this.currentStock.subtract(quantity);
            this.totalConsumed = this.totalConsumed.add(quantity);
            calculateStockValues();
            updateStockStatus();
            return true;
        }
        return false;
    }

    // Helper method to check if reorder is needed
    public boolean isReorderNeeded() {
        return this.currentStock.compareTo(this.reorderLevel) <= 0;
    }

    // Helper method to check if item is expired
    public boolean isExpired() {
        return this.isPerishable && this.expiryDate != null && this.expiryDate.isBefore(LocalDate.now());
    }

    // Helper method to check if item is expiring soon (within 30 days)
    public boolean isExpiringSoon() {
        return this.isPerishable && this.expiryDate != null &&
                this.expiryDate.isBefore(LocalDate.now().plusDays(30)) &&
                this.expiryDate.isAfter(LocalDate.now());
    }
}
