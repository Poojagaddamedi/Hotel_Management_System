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
@Table(name = "guest_services")
public class GuestService {
    // --- Custom fields for controller compatibility ---
    @Column(name = "service_no", unique = true, length = 50)
    private String serviceNo;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "actual_cost", precision = 10, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "special_instructions", length = 1000)
    private String specialInstructions;

    @Column(name = "guest_satisfaction_rating")
    private Integer guestSatisfactionRating;

    @Column(name = "chargeable")
    private Boolean chargeable;

    @Column(name = "user_id")
    private Long userId;

    // --- Getters and Setters for new fields ---
    public String getServiceNo() {
        return serviceNo;
    }

    public void setServiceNo(String serviceNo) {
        this.serviceNo = serviceNo;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public BigDecimal getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(BigDecimal estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public BigDecimal getActualCost() {
        return actualCost;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public Integer getGuestSatisfactionRating() {
        return guestSatisfactionRating;
    }

    public void setGuestSatisfactionRating(Integer guestSatisfactionRating) {
        this.guestSatisfactionRating = guestSatisfactionRating;
    }

    public Boolean getChargeable() {
        return chargeable;
    }

    public void setChargeable(Boolean chargeable) {
        this.chargeable = chargeable;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_request_id", nullable = false, unique = true, length = 50)
    private String serviceRequestId;

    @Column(name = "folio_no", nullable = false, length = 50)
    private String folioNo;

    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "room_no", nullable = false, length = 10)
    private String roomNo;

    @Column(name = "guest_contact", length = 50)
    private String guestContact;

    @Column(name = "service_type", nullable = false, length = 50)
    private String serviceType; // ROOM_SERVICE, LAUNDRY, HOUSEKEEPING, TRANSPORT, CONCIERGE, SPA, etc.

    @Column(name = "service_category", length = 50)
    private String serviceCategory; // F&B, WELLNESS, TRANSPORT, BUSINESS, ENTERTAINMENT

    @Column(name = "service_description", nullable = false, length = 1000)
    private String serviceDescription;

    @Column(name = "priority", nullable = false, length = 20)
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "status", nullable = false, length = 20)
    private String status = "REQUESTED"; // REQUESTED, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "requested_date", nullable = false)
    private LocalDateTime requestedDate;

    @Column(name = "requested_by", nullable = false)
    private Long requestedBy; // Staff ID who took the request

    @Column(name = "preferred_time")
    private LocalDateTime preferredTime;

    @Column(name = "assigned_to")
    private Long assignedTo; // Staff ID responsible for service

    @Column(name = "assigned_by")
    private Long assignedBy; // Manager who assigned

    @Column(name = "assigned_date")
    private LocalDateTime assignedDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    @Column(name = "service_charge", precision = 10, scale = 2)
    private BigDecimal serviceCharge = BigDecimal.ZERO;

    @Column(name = "additional_charges", precision = 10, scale = 2)
    private BigDecimal additionalCharges = BigDecimal.ZERO;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "PENDING"; // PENDING, PAID, POSTED_TO_ROOM

    @Column(name = "billing_status", length = 20)
    private String billingStatus = "UNBILLED"; // UNBILLED, BILLED, POSTED

    @Column(name = "service_notes", length = 1000)
    private String serviceNotes;

    @Column(name = "completion_notes", length = 1000)
    private String completionNotes;

    @Column(name = "guest_instructions", length = 500)
    private String guestInstructions;

    @Column(name = "internal_notes", length = 500)
    private String internalNotes;

    @Column(name = "items_delivered", length = 1000)
    private String itemsDelivered; // JSON string of items delivered

    @Column(name = "equipment_used", length = 500)
    private String equipmentUsed;

    @Column(name = "external_vendor", length = 100)
    private String externalVendor;

    @Column(name = "vendor_contact", length = 50)
    private String vendorContact;

    @Column(name = "vendor_cost", precision = 10, scale = 2)
    private BigDecimal vendorCost = BigDecimal.ZERO;

    @Column(name = "guest_rating")
    private Integer guestRating; // 1-5 stars

    @Column(name = "guest_feedback", length = 1000)
    private String guestFeedback;

    @Column(name = "service_quality_score")
    private Integer serviceQualityScore; // 1-10

    @Column(name = "delivery_location", length = 100)
    private String deliveryLocation;

    @Column(name = "special_requests", length = 500)
    private String specialRequests;

    @Column(name = "allergies_dietary_restrictions", length = 300)
    private String allergiesDietaryRestrictions;

    @Column(name = "repeat_service", nullable = false)
    private Boolean repeatService = false;

    @Column(name = "vip_guest", nullable = false)
    private Boolean vipGuest = false;

    @Column(name = "urgent_request", nullable = false)
    private Boolean urgentRequest = false;

    @Column(name = "complementary_service", nullable = false)
    private Boolean complementaryService = false;

    @Column(name = "follow_up_required", nullable = false)
    private Boolean followUpRequired = false;

    @Column(name = "follow_up_date")
    private LocalDateTime followUpDate;

    @Column(name = "follow_up_notes", length = 500)
    private String followUpNotes;

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
        this.requestedDate = LocalDateTime.now();
        if (this.auditDate == null) {
            this.auditDate = LocalDate.now();
        }

        // Auto-generate service request ID if not provided
        if (this.serviceRequestId == null || this.serviceRequestId.isEmpty()) {
            this.serviceRequestId = "SRV_" + System.currentTimeMillis();
        }

        calculateTotalAmount();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();

        // Calculate actual duration if completed
        if ("COMPLETED".equals(this.status) && this.startTime != null && this.completionTime != null) {
            this.actualDurationMinutes = (int) java.time.Duration.between(this.startTime, this.completionTime)
                    .toMinutes();
        }

        calculateTotalAmount();
    }

    // Helper method to calculate total amount
    public void calculateTotalAmount() {
        this.totalAmount = this.serviceCharge.add(this.additionalCharges).add(this.vendorCost);
        this.netAmount = this.totalAmount.add(this.taxAmount).subtract(this.discountAmount);
    }

    // Helper method to assign service
    public void assignService(Long assignedTo, Long assignedBy) {
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.assignedDate = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    // Helper method to start service
    public void startService() {
        this.status = "IN_PROGRESS";
        this.startTime = LocalDateTime.now();
    }

    // Helper method to complete service
    public void completeService(String completionNotes) {
        this.status = "COMPLETED";
        this.completionTime = LocalDateTime.now();
        this.completionNotes = completionNotes;

        if (this.startTime != null) {
            this.actualDurationMinutes = (int) java.time.Duration.between(this.startTime, this.completionTime)
                    .toMinutes();
        }
    }

    // Helper method to check if service is overdue
    public boolean isOverdue() {
        if (this.preferredTime != null && !"COMPLETED".equals(this.status) && !"CANCELLED".equals(this.status)) {
            return LocalDateTime.now().isAfter(this.preferredTime);
        }
        return false;
    }

    // Helper method to post to room bill
    public void postToRoomBill() {
        this.paymentStatus = "POSTED_TO_ROOM";
        this.billingStatus = "POSTED";
    }
}
