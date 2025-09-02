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
 * Spa & Wellness Management Controller
 * Handles spa services, wellness programs, therapist scheduling, and treatment
 * packages
 */
@RestController
@RequestMapping("/api/spa")
@CrossOrigin(origins = "*")
public class SpaController {

    /**
     * Get all spa services
     */
    @GetMapping("/services")
    public ResponseEntity<Map<String, Object>> getSpaServices(@RequestParam(required = false) String category) {
        try {
            List<Map<String, Object>> services = Arrays.asList(
                    createSpaService(1L, "Swedish Massage", "MASSAGE", new BigDecimal("120.00"), 60),
                    createSpaService(2L, "Deep Tissue Massage", "MASSAGE", new BigDecimal("150.00"), 90),
                    createSpaService(3L, "Hydrating Facial", "FACIAL", new BigDecimal("100.00"), 75),
                    createSpaService(4L, "Hot Stone Therapy", "THERAPY", new BigDecimal("180.00"), 90),
                    createSpaService(5L, "Aromatherapy Session", "AROMATHERAPY", new BigDecimal("130.00"), 60));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Spa services retrieved successfully");
            response.put("data", services);
            response.put("category", category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch spa services: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get spa appointments
     */
    @GetMapping("/appointments")
    public ResponseEntity<Map<String, Object>> getSpaAppointments(@RequestParam(required = false) String date,
            @RequestParam(required = false) String therapistId) {
        try {
            List<Map<String, Object>> appointments = Arrays.asList(
                    createAppointment(1L, "Mrs. Johnson", "Swedish Massage", "Sarah Williams", "CONFIRMED"),
                    createAppointment(2L, "Mr. Smith", "Deep Tissue Massage", "Emily Davis", "IN_PROGRESS"),
                    createAppointment(3L, "Ms. Brown", "Hydrating Facial", "Lisa Anderson", "COMPLETED"),
                    createAppointment(4L, "Mr. Wilson", "Hot Stone Therapy", "Sarah Williams", "PENDING"),
                    createAppointment(5L, "Mrs. Taylor", "Aromatherapy Session", "Michelle Lee", "CONFIRMED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Spa appointments retrieved successfully");
            response.put("data", appointments);
            response.put("filters", Map.of("date", date, "therapistId", therapistId));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch spa appointments: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Book spa appointment
     */
    @PostMapping("/appointments")
    public ResponseEntity<Map<String, Object>> bookAppointment(@RequestBody Map<String, Object> appointmentData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> appointment = new HashMap<>(appointmentData);
            appointment.put("id", newId);
            appointment.put("bookingNumber", "SPA_" + newId);
            appointment.put("status", "PENDING");
            appointment.put("bookingDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Spa appointment booked successfully");
            response.put("data", appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to book appointment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get therapist schedules
     */
    @GetMapping("/therapists")
    public ResponseEntity<Map<String, Object>> getTherapists() {
        try {
            List<Map<String, Object>> therapists = Arrays.asList(
                    createTherapist(1L, "Sarah Williams", "MASSAGE", "AVAILABLE", 4.8),
                    createTherapist(2L, "Emily Davis", "FACIAL", "BUSY", 4.9),
                    createTherapist(3L, "Lisa Anderson", "THERAPY", "AVAILABLE", 4.7),
                    createTherapist(4L, "Michelle Lee", "AROMATHERAPY", "ON_BREAK", 4.6),
                    createTherapist(5L, "Jessica Brown", "MASSAGE", "AVAILABLE", 4.8));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Therapists retrieved successfully");
            response.put("data", therapists);
            response.put("available", 3);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch therapists: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get therapist availability
     */
    @GetMapping("/therapists/{therapistId}/availability")
    public ResponseEntity<Map<String, Object>> getTherapistAvailability(@PathVariable Long therapistId,
            @RequestParam String date) {
        try {
            List<Map<String, Object>> availability = Arrays.asList(
                    createAvailabilitySlot("09:00", "10:30", "AVAILABLE"),
                    createAvailabilitySlot("10:30", "12:00", "BOOKED"),
                    createAvailabilitySlot("13:00", "14:30", "AVAILABLE"),
                    createAvailabilitySlot("14:30", "16:00", "AVAILABLE"),
                    createAvailabilitySlot("16:00", "17:30", "BOOKED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Therapist availability retrieved successfully");
            response.put("data", availability);
            response.put("therapistId", therapistId);
            response.put("date", date);
            response.put("availableSlots", 3);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch therapist availability: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get wellness packages
     */
    @GetMapping("/packages")
    public ResponseEntity<Map<String, Object>> getWellnessPackages() {
        try {
            List<Map<String, Object>> packages = Arrays.asList(
                    createWellnessPackage(1L, "Relaxation Retreat", new BigDecimal("350.00"), 3),
                    createWellnessPackage(2L, "Ultimate Wellness Experience", new BigDecimal("550.00"), 5),
                    createWellnessPackage(3L, "Couple's Spa Day", new BigDecimal("480.00"), 4),
                    createWellnessPackage(4L, "Stress Relief Package", new BigDecimal("280.00"), 2),
                    createWellnessPackage(5L, "Beauty & Wellness Combo", new BigDecimal("420.00"), 4));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Wellness packages retrieved successfully");
            response.put("data", packages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch wellness packages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Book wellness package
     */
    @PostMapping("/packages/book")
    public ResponseEntity<Map<String, Object>> bookWellnessPackage(@RequestBody Map<String, Object> bookingData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> booking = new HashMap<>(bookingData);
            booking.put("id", newId);
            booking.put("packageBookingId", "WPK_" + newId);
            booking.put("status", "CONFIRMED");
            booking.put("bookingDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Wellness package booked successfully");
            response.put("data", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to book wellness package: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get treatment rooms
     */
    @GetMapping("/rooms")
    public ResponseEntity<Map<String, Object>> getTreatmentRooms() {
        try {
            List<Map<String, Object>> rooms = Arrays.asList(
                    createTreatmentRoom(1L, "Serenity Suite", "PREMIUM", "AVAILABLE"),
                    createTreatmentRoom(2L, "Tranquility Room", "STANDARD", "OCCUPIED"),
                    createTreatmentRoom(3L, "Harmony Chamber", "PREMIUM", "AVAILABLE"),
                    createTreatmentRoom(4L, "Zen Den", "DELUXE", "MAINTENANCE"),
                    createTreatmentRoom(5L, "Relaxation Room", "STANDARD", "AVAILABLE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Treatment rooms retrieved successfully");
            response.put("data", rooms);
            response.put("available", 3);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch treatment rooms: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get spa inventory (products and supplies)
     */
    @GetMapping("/inventory")
    public ResponseEntity<Map<String, Object>> getSpaInventory(@RequestParam(required = false) String category) {
        try {
            List<Map<String, Object>> inventory = Arrays.asList(
                    createInventoryItem("Essential Oil Set", "AROMATHERAPY", 25, 10),
                    createInventoryItem("Massage Oil - Premium", "MASSAGE", 18, 5),
                    createInventoryItem("Facial Masks", "FACIAL", 45, 20),
                    createInventoryItem("Hot Stones", "THERAPY", 8, 2),
                    createInventoryItem("Towels - Spa Grade", "LINENS", 120, 30));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Spa inventory retrieved successfully");
            response.put("data", inventory);
            response.put("lowStockItems", 2);
            response.put("category", category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch spa inventory: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get spa analytics and reports
     */
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getSpaAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();
            analytics.put("totalBookings", 267);
            analytics.put("monthlyRevenue", new BigDecimal("32450.00"));
            analytics.put("averageServiceDuration", "75 minutes");
            analytics.put("customerSatisfaction", 4.7);
            analytics.put("therapistUtilization", 82.5);

            analytics.put("popularServices", Arrays.asList(
                    Map.of("service", "Swedish Massage", "bookings", 89, "revenue", new BigDecimal("10680.00")),
                    Map.of("service", "Deep Tissue Massage", "bookings", 67, "revenue", new BigDecimal("10050.00")),
                    Map.of("service", "Hydrating Facial", "bookings", 56, "revenue", new BigDecimal("5600.00")),
                    Map.of("service", "Hot Stone Therapy", "bookings", 34, "revenue", new BigDecimal("6120.00"))));

            analytics.put("therapistPerformance", Arrays.asList(
                    Map.of("therapist", "Sarah Williams", "bookings", 67, "rating", 4.8, "revenue",
                            new BigDecimal("8040.00")),
                    Map.of("therapist", "Emily Davis", "bookings", 54, "rating", 4.9, "revenue",
                            new BigDecimal("6480.00")),
                    Map.of("therapist", "Lisa Anderson", "bookings", 49, "rating", 4.7, "revenue",
                            new BigDecimal("5880.00"))));

            analytics.put("monthlyTrend", Arrays.asList(
                    Map.of("month", "January", "bookings", 45, "revenue", new BigDecimal("5400.00")),
                    Map.of("month", "February", "bookings", 52, "revenue", new BigDecimal("6240.00")),
                    Map.of("month", "March", "bookings", 58, "revenue", new BigDecimal("6960.00")),
                    Map.of("month", "April", "bookings", 67, "revenue", new BigDecimal("8040.00")),
                    Map.of("month", "May", "bookings", 45, "revenue", new BigDecimal("5810.00"))));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Spa analytics retrieved successfully");
            response.put("data", analytics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch spa analytics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get guest wellness profile
     */
    @GetMapping("/guests/{guestId}/profile")
    public ResponseEntity<Map<String, Object>> getGuestWellnessProfile(@PathVariable Long guestId) {
        try {
            Map<String, Object> profile = new HashMap<>();
            profile.put("guestId", guestId);
            profile.put("guestName", "Mrs. Johnson");
            profile.put("membershipLevel", "PREMIUM");
            profile.put("totalVisits", 23);
            profile.put("totalSpent", new BigDecimal("2340.00"));
            profile.put("favoriteServices", Arrays.asList("Swedish Massage", "Aromatherapy"));
            profile.put("preferredTherapist", "Sarah Williams");
            profile.put("allergies", Arrays.asList("Nuts", "Fragrances"));
            profile.put("preferences", Arrays.asList("Light pressure", "Quiet environment"));

            profile.put("visitHistory", Arrays.asList(
                    Map.of("date", "2024-05-15", "service", "Swedish Massage", "rating", 5),
                    Map.of("date", "2024-04-20", "service", "Hydrating Facial", "rating", 4),
                    Map.of("date", "2024-03-10", "service", "Aromatherapy Session", "rating", 5)));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Guest wellness profile retrieved successfully");
            response.put("data", profile);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch guest wellness profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update appointment status
     */
    @PutMapping("/appointments/{id}/status")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatus(@PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            Map<String, Object> updatedAppointment = new HashMap<>();
            updatedAppointment.put("id", id);
            updatedAppointment.put("status", statusUpdate.get("status"));
            updatedAppointment.put("updatedDate", LocalDateTime.now().toString());
            updatedAppointment.put("notes", statusUpdate.get("notes"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Appointment status updated successfully");
            response.put("data", updatedAppointment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to update appointment status: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods
    private Map<String, Object> createSpaService(Long id, String name, String category, BigDecimal price,
            int duration) {
        Map<String, Object> service = new HashMap<>();
        service.put("id", id);
        service.put("serviceCode", "SVC_" + String.format("%03d", id));
        service.put("name", name);
        service.put("category", category);
        service.put("price", price);
        service.put("duration", duration);
        service.put("description", "Professional " + name.toLowerCase() + " service");
        service.put("benefits", Arrays.asList("Relaxation", "Stress Relief", "Improved Circulation"));
        service.put("suitableFor", Arrays.asList("Adults", "Seniors"));
        service.put("popular", id <= 3);
        return service;
    }

    private Map<String, Object> createAppointment(Long id, String guestName, String service, String therapist,
            String status) {
        Map<String, Object> appointment = new HashMap<>();
        appointment.put("id", id);
        appointment.put("bookingNumber", "SPA_" + String.format("%06d", id));
        appointment.put("guestName", guestName);
        appointment.put("service", service);
        appointment.put("therapist", therapist);
        appointment.put("status", status);
        appointment.put("appointmentDate", LocalDate.now().plusDays(id).toString());
        appointment.put("appointmentTime", String.format("%02d:00", 9 + (id % 8)));
        appointment.put("duration", 60 + (id % 3) * 30);
        appointment.put("roomNumber", "TR_" + (id % 5 + 1));
        appointment.put("specialRequests", id % 2 == 0 ? "Light pressure" : "Standard pressure");
        return appointment;
    }

    private Map<String, Object> createTherapist(Long id, String name, String specialization, String status,
            Double rating) {
        Map<String, Object> therapist = new HashMap<>();
        therapist.put("id", id);
        therapist.put("employeeCode", "TH_" + String.format("%03d", id));
        therapist.put("name", name);
        therapist.put("specialization", specialization);
        therapist.put("status", status);
        therapist.put("rating", rating);
        therapist.put("experience", 3 + (id % 8));
        therapist.put("certifications", Arrays.asList("Licensed Massage Therapist", "Aromatherapy Certified"));
        therapist.put("languages", Arrays.asList("English", "Spanish"));
        therapist.put("totalAppointments", 150 + id * 25);
        return therapist;
    }

    private Map<String, Object> createAvailabilitySlot(String startTime, String endTime, String status) {
        Map<String, Object> slot = new HashMap<>();
        slot.put("startTime", startTime);
        slot.put("endTime", endTime);
        slot.put("status", status);
        slot.put("duration", "90 minutes");
        slot.put("bookingId", status.equals("BOOKED") ? "SPA_123456" : null);
        return slot;
    }

    private Map<String, Object> createWellnessPackage(Long id, String name, BigDecimal price, int services) {
        Map<String, Object> package_ = new HashMap<>();
        package_.put("id", id);
        package_.put("packageCode", "WPK_" + String.format("%03d", id));
        package_.put("name", name);
        package_.put("price", price);
        package_.put("servicesIncluded", services);
        package_.put("validityDays", 30);
        package_.put("description", "Complete wellness experience with " + services + " premium services");
        package_.put("includes", Arrays.asList("Massage", "Facial", "Aromatherapy", "Consultation"));
        package_.put("discount", services >= 4 ? 15 : 10);
        package_.put("popular", id <= 2);
        return package_;
    }

    private Map<String, Object> createTreatmentRoom(Long id, String name, String type, String status) {
        Map<String, Object> room = new HashMap<>();
        room.put("id", id);
        room.put("roomCode", "TR_" + String.format("%02d", id));
        room.put("name", name);
        room.put("type", type);
        room.put("status", status);
        room.put("capacity", 2);
        room.put("amenities", Arrays.asList("Heated bed", "Sound system", "Aromatherapy diffuser", "Private bathroom"));
        room.put("temperature", "25°C");
        room.put("lighting", "Dimmable LED");
        room.put("size", "12 sq m");
        return room;
    }

    private Map<String, Object> createInventoryItem(String itemName, String category, int currentStock,
            int minimumLevel) {
        Map<String, Object> item = new HashMap<>();
        item.put("itemName", itemName);
        item.put("category", category);
        item.put("currentStock", currentStock);
        item.put("minimumLevel", minimumLevel);
        item.put("unit", "pieces");
        item.put("status", currentStock <= minimumLevel ? "LOW_STOCK" : "NORMAL");
        item.put("supplier", "Spa Supplies Pro");
        item.put("lastRestocked", LocalDate.now().minusDays((int) (Math.random() * 30)).toString());
        item.put("unitCost", new BigDecimal((10 + Math.random() * 40) + "0"));
        return item;
    }
}
