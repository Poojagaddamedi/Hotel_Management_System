package com.hotel.erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Payroll Management Controller
 * Handles employee payroll, salary, deductions, and benefits management
 */
@RestController
@RequestMapping("/api/payroll")
@CrossOrigin(origins = "*")
public class PayrollController {

    /**
     * Get all payroll records
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPayrollRecords() {
        try {
            // Mock data for payroll records
            List<Map<String, Object>> payrollRecords = Arrays.asList(
                    createPayrollRecord(1L, "EMP001", "John Doe", "MANAGER", 75000.00, "MONTHLY"),
                    createPayrollRecord(2L, "EMP002", "Jane Smith", "RECEPTIONIST", 35000.00, "MONTHLY"),
                    createPayrollRecord(3L, "EMP003", "Mike Johnson", "HOUSEKEEPING", 25000.00, "MONTHLY"),
                    createPayrollRecord(4L, "EMP004", "Sarah Wilson", "CHEF", 45000.00, "MONTHLY"),
                    createPayrollRecord(5L, "EMP005", "David Brown", "SECURITY", 30000.00, "MONTHLY"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payroll records retrieved successfully");
            response.put("data", payrollRecords);
            response.put("total", payrollRecords.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch payroll records: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get payroll record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPayrollById(@PathVariable Long id) {
        try {
            Map<String, Object> payrollRecord = createDetailedPayrollRecord(id);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payroll record retrieved successfully");
            response.put("data", payrollRecord);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch payroll record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create new payroll record
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPayrollRecord(@RequestBody Map<String, Object> payrollData) {
        try {
            // Simulate creating payroll record
            Long newId = System.currentTimeMillis();
            Map<String, Object> createdRecord = new HashMap<>(payrollData);
            createdRecord.put("id", newId);
            createdRecord.put("status", "ACTIVE");
            createdRecord.put("createdDate", LocalDateTime.now().toString());
            createdRecord.put("lastUpdated", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payroll record created successfully");
            response.put("data", createdRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create payroll record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update payroll record
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePayrollRecord(@PathVariable Long id,
            @RequestBody Map<String, Object> payrollData) {
        try {
            Map<String, Object> updatedRecord = new HashMap<>(payrollData);
            updatedRecord.put("id", id);
            updatedRecord.put("lastUpdated", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payroll record updated successfully");
            response.put("data", updatedRecord);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update payroll record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete payroll record
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePayrollRecord(@PathVariable Long id) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payroll record deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to delete payroll record: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get payroll records by employee ID
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Map<String, Object>> getPayrollByEmployee(@PathVariable String employeeId) {
        try {
            List<Map<String, Object>> payrollRecords = Arrays.asList(
                    createPayrollRecord(1L, employeeId, "John Doe", "MANAGER", 75000.00, "MONTHLY"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Employee payroll records retrieved successfully");
            response.put("data", payrollRecords);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch employee payroll: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get payroll records by department
     */
    @GetMapping("/department/{department}")
    public ResponseEntity<Map<String, Object>> getPayrollByDepartment(@PathVariable String department) {
        try {
            List<Map<String, Object>> payrollRecords = Arrays.asList(
                    createPayrollRecord(1L, "EMP001", "John Doe", department, 75000.00, "MONTHLY"),
                    createPayrollRecord(2L, "EMP002", "Jane Smith", department, 35000.00, "MONTHLY"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Department payroll records retrieved successfully");
            response.put("data", payrollRecords);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch department payroll: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Process monthly payroll
     */
    @PostMapping("/process-monthly")
    public ResponseEntity<Map<String, Object>> processMonthlyPayroll(@RequestBody Map<String, Object> payrollData) {
        try {
            Map<String, Object> processResult = new HashMap<>();
            processResult.put("month", payrollData.get("month"));
            processResult.put("year", payrollData.get("year"));
            processResult.put("totalEmployees", 25);
            processResult.put("totalGrossPay", 1250000.00);
            processResult.put("totalDeductions", 150000.00);
            processResult.put("totalNetPay", 1100000.00);
            processResult.put("processedDate", LocalDateTime.now().toString());
            processResult.put("status", "COMPLETED");

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Monthly payroll processed successfully");
            response.put("data", processResult);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to process monthly payroll: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get salary slips
     */
    @GetMapping("/salary-slip/{employeeId}")
    public ResponseEntity<Map<String, Object>> getSalarySlip(@PathVariable String employeeId,
            @RequestParam String month,
            @RequestParam String year) {
        try {
            Map<String, Object> salarySlip = createSalarySlip(employeeId, month, year);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Salary slip retrieved successfully");
            response.put("data", salarySlip);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch salary slip: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate payroll report
     */
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> generatePayrollReport(@RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            Map<String, Object> report = new HashMap<>();
            report.put("reportPeriod", startDate + " to " + endDate);
            report.put("totalEmployees", 25);
            report.put("totalGrossPay", 3750000.00);
            report.put("totalDeductions", 450000.00);
            report.put("totalNetPay", 3300000.00);
            report.put("averageSalary", 150000.00);
            report.put("departments", Arrays.asList(
                    Map.of("name", "Management", "employees", 5, "totalPay", 750000.00),
                    Map.of("name", "Front Office", "employees", 8, "totalPay", 1200000.00),
                    Map.of("name", "Housekeeping", "employees", 7, "totalPay", 875000.00),
                    Map.of("name", "F&B", "employees", 5, "totalPay", 625000.00)));
            report.put("generatedDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payroll report generated successfully");
            response.put("data", report);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to generate payroll report: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods
    private Map<String, Object> createPayrollRecord(Long id, String employeeId, String employeeName,
            String department, Double baseSalary, String payFrequency) {
        Map<String, Object> record = new HashMap<>();
        record.put("id", id);
        record.put("employeeId", employeeId);
        record.put("employeeName", employeeName);
        record.put("department", department);
        record.put("position", department);
        record.put("baseSalary", baseSalary);
        record.put("payFrequency", payFrequency);
        record.put("status", "ACTIVE");
        record.put("joinDate", LocalDate.now().minusYears(2).toString());
        record.put("lastPayDate", LocalDate.now().minusMonths(1).toString());
        record.put("nextPayDate", LocalDate.now().toString());
        return record;
    }

    private Map<String, Object> createDetailedPayrollRecord(Long id) {
        Map<String, Object> record = new HashMap<>();
        record.put("id", id);
        record.put("employeeId", "EMP" + String.format("%03d", id));
        record.put("employeeName", "Employee " + id);
        record.put("department", "MANAGEMENT");
        record.put("position", "Manager");
        record.put("baseSalary", 75000.00);
        record.put("allowances", Arrays.asList(
                Map.of("type", "HRA", "amount", 22500.00),
                Map.of("type", "Transport", "amount", 5000.00),
                Map.of("type", "Medical", "amount", 2500.00)));
        record.put("deductions", Arrays.asList(
                Map.of("type", "PF", "amount", 9000.00),
                Map.of("type", "ESI", "amount", 1500.00),
                Map.of("type", "Tax", "amount", 12000.00)));
        record.put("grossSalary", 105000.00);
        record.put("totalDeductions", 22500.00);
        record.put("netSalary", 82500.00);
        record.put("payFrequency", "MONTHLY");
        record.put("status", "ACTIVE");
        return record;
    }

    private Map<String, Object> createSalarySlip(String employeeId, String month, String year) {
        Map<String, Object> slip = new HashMap<>();
        slip.put("employeeId", employeeId);
        slip.put("employeeName", "John Doe");
        slip.put("department", "MANAGEMENT");
        slip.put("month", month);
        slip.put("year", year);
        slip.put("payPeriod", month + " " + year);
        slip.put("baseSalary", 75000.00);
        slip.put("allowances", Arrays.asList(
                Map.of("type", "HRA", "amount", 22500.00),
                Map.of("type", "Transport", "amount", 5000.00)));
        slip.put("deductions", Arrays.asList(
                Map.of("type", "PF", "amount", 9000.00),
                Map.of("type", "Tax", "amount", 12000.00)));
        slip.put("grossSalary", 102500.00);
        slip.put("totalDeductions", 21000.00);
        slip.put("netSalary", 81500.00);
        slip.put("generatedDate", LocalDateTime.now().toString());
        return slip;
    }
}
