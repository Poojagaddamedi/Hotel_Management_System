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
@Table(name = "maintenance")
public class Maintenance {
    // --- Custom fields for controller compatibility ---
    @Column(name = "ticket_no", unique = true, length = 50)
    private String ticketNo;

    @Column(name = "room_no", length = 20)
    private String roomNo;

    @Column(name = "area", length = 100)
    private String area;

    @Column(name = "issue_type", length = 100)
    private String issueType;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "vendor_name", length = 100)
    private String vendorName;

    @Column(name = "equipment_needed", length = 200)
    private String equipmentNeeded;

    @Column(name = "user_id")
    private Long userId;

    // --- Getters and Setters for new fields ---
    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getEquipmentNeeded() {
        return equipmentNeeded;
    }

    public void setEquipmentNeeded(String equipmentNeeded) {
        this.equipmentNeeded = equipmentNeeded;
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

    @Column(name = "maintenance_id", nullable = false, unique = true, length = 50)
    private String maintenanceId;

    @Column(name = "request_type", nullable = false, length = 30)
    private String requestType; // PREVENTIVE, CORRECTIVE, EMERGENCY, SCHEDULED

    @Column(name = "priority", nullable = false, length = 20)
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING"; // PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(name = "location", nullable = false, length = 100)
    private String location; // Room number, area, equipment location

    @Column(name = "equipment_type", length = 50)
    private String equipmentType; // AC, TV, PLUMBING, ELECTRICAL, FURNITURE, etc.

    @Column(name = "equipment_id", length = 50)
    private String equipmentId;

    @Column(name = "issue_description", nullable = false, length = 1000)
    private String issueDescription;

    @Column(name = "reported_by", nullable = false)
    private Long reportedBy; // Staff ID who reported

    @Column(name = "reported_date", nullable = false)
    private LocalDateTime reportedDate;

    @Column(name = "assigned_to")
    private Long assignedTo; // Maintenance staff ID

    @Column(name = "assigned_by")
    private Long assignedBy; // Manager who assigned

    @Column(name = "assigned_date")
    private LocalDateTime assignedDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "estimated_duration_hours")
    private Integer estimatedDurationHours;

    @Column(name = "actual_duration_hours")
    private Integer actualDurationHours;

    @Column(name = "estimated_cost", precision = 10, scale = 2)
    private BigDecimal estimatedCost = BigDecimal.ZERO;

    @Column(name = "actual_cost", precision = 10, scale = 2)
    private BigDecimal actualCost = BigDecimal.ZERO;

    @Column(name = "parts_used", length = 1000)
    private String partsUsed; // JSON string of parts/materials used

    @Column(name = "parts_cost", precision = 10, scale = 2)
    private BigDecimal partsCost = BigDecimal.ZERO;

    @Column(name = "labor_cost", precision = 10, scale = 2)
    private BigDecimal laborCost = BigDecimal.ZERO;

    @Column(name = "external_vendor", length = 100)
    private String externalVendor;

    @Column(name = "vendor_contact", length = 50)
    private String vendorContact;

    @Column(name = "vendor_cost", precision = 10, scale = 2)
    private BigDecimal vendorCost = BigDecimal.ZERO;

    @Column(name = "work_description", length = 2000)
    private String workDescription;

    @Column(name = "completion_notes", length = 1000)
    private String completionNotes;

    @Column(name = "quality_check_status", length = 20)
    private String qualityCheckStatus; // PASSED, FAILED, PENDING

    @Column(name = "quality_checked_by")
    private Long qualityCheckedBy;

    @Column(name = "quality_check_date")
    private LocalDateTime qualityCheckDate;

    @Column(name = "quality_check_notes", length = 500)
    private String qualityCheckNotes;

    @Column(name = "guest_impact", nullable = false)
    private Boolean guestImpact = false;

    @Column(name = "room_blocked", nullable = false)
    private Boolean roomBlocked = false;

    @Column(name = "safety_concern", nullable = false)
    private Boolean safetyConcern = false;

    @Column(name = "recurring_issue", nullable = false)
    private Boolean recurringIssue = false;

    @Column(name = "preventive_schedule_id", length = 50)
    private String preventiveScheduleId;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @Column(name = "warranty_claim", nullable = false)
    private Boolean warrantyClaim = false;

    @Column(name = "warranty_details", length = 500)
    private String warrantyDetails;

    @Column(name = "insurance_claim", nullable = false)
    private Boolean insuranceClaim = false;

    @Column(name = "insurance_details", length = 500)
    private String insuranceDetails;

    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired = false;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "approval_notes", length = 500)
    private String approvalNotes;

    @Column(name = "guest_feedback", length = 500)
    private String guestFeedback;

    @Column(name = "guest_rating")
    private Integer guestRating; // 1-5 stars

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
        this.reportedDate = LocalDateTime.now();
        if (this.auditDate == null) {
            this.auditDate = LocalDate.now();
        }

        // Auto-generate maintenance ID if not provided
        if (this.maintenanceId == null || this.maintenanceId.isEmpty()) {
            this.maintenanceId = "MNT_" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate = LocalDateTime.now();

        // Calculate actual duration if completed
        if ("COMPLETED".equals(this.status) && this.startDate != null && this.completionDate != null) {
            this.actualDurationHours = (int) java.time.Duration.between(this.startDate, this.completionDate).toHours();
        }

        // Calculate total actual cost
        this.actualCost = this.partsCost.add(this.laborCost).add(this.vendorCost);
    }

    // Helper method to assign maintenance
    public void assignMaintenance(Long assignedTo, Long assignedBy) {
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.assignedDate = LocalDateTime.now();
        this.status = "ASSIGNED";
    }

    // Helper method to start maintenance
    public void startMaintenance() {
        this.status = "IN_PROGRESS";
        this.startDate = LocalDateTime.now();
    }

    // Helper method to complete maintenance
    public void completeMaintenance(String completionNotes) {
        this.status = "COMPLETED";
        this.completionDate = LocalDateTime.now();
        this.completionNotes = completionNotes;

        if (this.startDate != null) {
            this.actualDurationHours = (int) java.time.Duration.between(this.startDate, this.completionDate).toHours();
        }
    }

    // Helper method to check if maintenance is overdue
    public boolean isOverdue() {
        if (this.estimatedDurationHours != null && this.startDate != null &&
                !"COMPLETED".equals(this.status) && !"CANCELLED".equals(this.status)) {
            LocalDateTime expectedEndTime = this.startDate.plusHours(this.estimatedDurationHours);
            return LocalDateTime.now().isAfter(expectedEndTime);
        }
        return false;
    }

    // Helper method to approve maintenance
    public void approveMaintenance(Long approvedBy, String approvalNotes) {
        this.approvedBy = approvedBy;
        this.approvalDate = LocalDateTime.now();
        this.approvalNotes = approvalNotes;
    }

    // Helper method to perform quality check
    public void performQualityCheck(Long qualityCheckedBy, String status, String notes) {
        this.qualityCheckedBy = qualityCheckedBy;
        this.qualityCheckDate = LocalDateTime.now();
        this.qualityCheckStatus = status;
        this.qualityCheckNotes = notes;
    }
}
