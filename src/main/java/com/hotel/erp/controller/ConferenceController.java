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
 * Conference Management Controller
 * Handles conference room bookings, event planning, equipment management, and
 * catering coordination
 */
@RestController
@RequestMapping("/api/conference")
@CrossOrigin(origins = "*")
public class ConferenceController {

    /**
     * Get all conference rooms
     */
    @GetMapping("/rooms")
    public ResponseEntity<Map<String, Object>> getConferenceRooms() {
        try {
            List<Map<String, Object>> rooms = Arrays.asList(
                    createConferenceRoom(1L, "Executive Boardroom", 20, "AVAILABLE"),
                    createConferenceRoom(2L, "Grand Conference Hall", 150, "BOOKED"),
                    createConferenceRoom(3L, "Meeting Room A", 12, "AVAILABLE"),
                    createConferenceRoom(4L, "Presentation Theater", 80, "MAINTENANCE"),
                    createConferenceRoom(5L, "Training Center", 35, "AVAILABLE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Conference rooms retrieved successfully");
            response.put("data", rooms);
            response.put("available", 3);
            response.put("total", rooms.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch conference rooms: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get room availability for specific date
     */
    @GetMapping("/rooms/availability")
    public ResponseEntity<Map<String, Object>> getRoomAvailability(@RequestParam String date,
            @RequestParam(required = false) String roomId) {
        try {
            List<Map<String, Object>> availability = Arrays.asList(
                    createAvailabilitySlot("Executive Boardroom", "09:00", "11:00", "AVAILABLE"),
                    createAvailabilitySlot("Executive Boardroom", "14:00", "17:00", "BOOKED"),
                    createAvailabilitySlot("Grand Conference Hall", "10:00", "16:00", "BOOKED"),
                    createAvailabilitySlot("Meeting Room A", "09:00", "12:00", "AVAILABLE"),
                    createAvailabilitySlot("Meeting Room A", "13:00", "18:00", "AVAILABLE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Room availability retrieved successfully");
            response.put("data", availability);
            response.put("date", date);
            response.put("availableSlots", 3);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch room availability: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get conference bookings
     */
    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getConferenceBookings(@RequestParam(required = false) String status,
            @RequestParam(required = false) String date) {
        try {
            List<Map<String, Object>> bookings = Arrays.asList(
                    createConferenceBooking(1L, "Annual Board Meeting", "Executive Boardroom", "CONFIRMED"),
                    createConferenceBooking(2L, "Product Launch Event", "Grand Conference Hall", "PENDING"),
                    createConferenceBooking(3L, "Training Workshop", "Training Center", "CONFIRMED"),
                    createConferenceBooking(4L, "Client Presentation", "Meeting Room A", "DRAFT"),
                    createConferenceBooking(5L, "Team Building Session", "Presentation Theater", "CANCELLED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Conference bookings retrieved successfully");
            response.put("data", bookings);
            response.put("filters", Map.of("status", status, "date", date));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch conference bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create conference booking
     */
    @PostMapping("/bookings")
    public ResponseEntity<Map<String, Object>> createConferenceBooking(@RequestBody Map<String, Object> bookingData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> booking = new HashMap<>(bookingData);
            booking.put("id", newId);
            booking.put("bookingNumber", "CNF_" + newId);
            booking.put("status", "PENDING");
            booking.put("bookingDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Conference booking created successfully");
            response.put("data", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create conference booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get equipment inventory
     */
    @GetMapping("/equipment")
    public ResponseEntity<Map<String, Object>> getEquipment(@RequestParam(required = false) String category) {
        try {
            List<Map<String, Object>> equipment = Arrays.asList(
                    createEquipment(1L, "Projector - 4K", "AUDIO_VIDEO", "AVAILABLE"),
                    createEquipment(2L, "Wireless Microphone Set", "AUDIO_VIDEO", "IN_USE"),
                    createEquipment(3L, "Flip Chart Stand", "PRESENTATION", "AVAILABLE"),
                    createEquipment(4L, "Sound System - Premium", "AUDIO_VIDEO", "MAINTENANCE"),
                    createEquipment(5L, "Video Conference Setup", "TECHNOLOGY", "AVAILABLE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Conference equipment retrieved successfully");
            response.put("data", equipment);
            response.put("available", 3);
            response.put("category", category);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch equipment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Book equipment for event
     */
    @PostMapping("/equipment/book")
    public ResponseEntity<Map<String, Object>> bookEquipment(@RequestBody Map<String, Object> bookingData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> booking = new HashMap<>(bookingData);
            booking.put("id", newId);
            booking.put("equipmentBookingId", "EQP_" + newId);
            booking.put("status", "RESERVED");
            booking.put("bookingDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Equipment booked successfully");
            response.put("data", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to book equipment: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get catering packages
     */
    @GetMapping("/catering")
    public ResponseEntity<Map<String, Object>> getCateringPackages() {
        try {
            List<Map<String, Object>> packages = Arrays.asList(
                    createCateringPackage(1L, "Coffee Break Package", "BREAK", new BigDecimal("15.00")),
                    createCateringPackage(2L, "Business Lunch", "LUNCH", new BigDecimal("35.00")),
                    createCateringPackage(3L, "Welcome Reception", "RECEPTION", new BigDecimal("25.00")),
                    createCateringPackage(4L, "Executive Dinner", "DINNER", new BigDecimal("65.00")),
                    createCateringPackage(5L, "Continental Breakfast", "BREAKFAST", new BigDecimal("20.00")));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Catering packages retrieved successfully");
            response.put("data", packages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch catering packages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Book catering for event
     */
    @PostMapping("/catering/book")
    public ResponseEntity<Map<String, Object>> bookCatering(@RequestBody Map<String, Object> cateringData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> booking = new HashMap<>(cateringData);
            booking.put("id", newId);
            booking.put("cateringOrderId", "CAT_" + newId);
            booking.put("status", "CONFIRMED");
            booking.put("orderDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Catering booked successfully");
            response.put("data", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to book catering: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get event packages
     */
    @GetMapping("/packages")
    public ResponseEntity<Map<String, Object>> getEventPackages() {
        try {
            List<Map<String, Object>> packages = Arrays.asList(
                    createEventPackage(1L, "Basic Meeting Package", "MEETING", new BigDecimal("200.00")),
                    createEventPackage(2L, "Premium Conference Package", "CONFERENCE", new BigDecimal("500.00")),
                    createEventPackage(3L, "Corporate Event Package", "CORPORATE", new BigDecimal("800.00")),
                    createEventPackage(4L, "Training Workshop Package", "TRAINING", new BigDecimal("350.00")),
                    createEventPackage(5L, "Executive Retreat Package", "RETREAT", new BigDecimal("1200.00")));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Event packages retrieved successfully");
            response.put("data", packages);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch event packages: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get event setup templates
     */
    @GetMapping("/setup-templates")
    public ResponseEntity<Map<String, Object>> getSetupTemplates(@RequestParam(required = false) String roomType) {
        try {
            List<Map<String, Object>> templates = Arrays.asList(
                    createSetupTemplate("Boardroom Style", "12-20 people", "BOARDROOM"),
                    createSetupTemplate("Theater Style", "50-150 people", "THEATER"),
                    createSetupTemplate("Classroom Style", "30-60 people", "CLASSROOM"),
                    createSetupTemplate("U-Shape Setup", "15-25 people", "U_SHAPE"),
                    createSetupTemplate("Round Tables", "40-120 people", "BANQUET"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Setup templates retrieved successfully");
            response.put("data", templates);
            response.put("roomType", roomType);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch setup templates: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Generate event proposal
     */
    @PostMapping("/proposals")
    public ResponseEntity<Map<String, Object>> generateEventProposal(@RequestBody Map<String, Object> proposalData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> proposal = new HashMap<>(proposalData);
            proposal.put("id", newId);
            proposal.put("proposalNumber", "PROP_" + newId);
            proposal.put("status", "DRAFT");
            proposal.put("createdDate", LocalDateTime.now().toString());
            proposal.put("validUntil", LocalDate.now().plusDays(30).toString());

            // Calculate estimated costs
            proposal.put("roomRental", new BigDecimal("500.00"));
            proposal.put("equipmentCost", new BigDecimal("200.00"));
            proposal.put("cateringCost", new BigDecimal("800.00"));
            proposal.put("serviceFee", new BigDecimal("150.00"));
            proposal.put("totalCost", new BigDecimal("1650.00"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Event proposal generated successfully");
            response.put("data", proposal);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to generate event proposal: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get conference analytics
     */
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getConferenceAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();
            analytics.put("totalBookings", 89);
            analytics.put("roomUtilization", 78.5);
            analytics.put("averageEventDuration", "4.5 hours");
            analytics.put("monthlyRevenue", new BigDecimal("45250.00"));
            analytics.put("customerSatisfaction", 4.6);

            analytics.put("popularRooms", Arrays.asList(
                    Map.of("room", "Grand Conference Hall", "bookings", 23, "revenue", new BigDecimal("18500.00")),
                    Map.of("room", "Executive Boardroom", "bookings", 19, "revenue", new BigDecimal("12750.00")),
                    Map.of("room", "Training Center", "bookings", 17, "revenue", new BigDecimal("8500.00"))));

            analytics.put("eventTypes", Arrays.asList(
                    Map.of("type", "Corporate Meetings", "count", 34, "percentage", 38.2),
                    Map.of("type", "Training Sessions", "count", 21, "percentage", 23.6),
                    Map.of("type", "Product Launches", "count", 15, "percentage", 16.9),
                    Map.of("type", "Conferences", "count", 12, "percentage", 13.5),
                    Map.of("type", "Workshops", "count", 7, "percentage", 7.8)));

            analytics.put("monthlyTrend", Arrays.asList(
                    Map.of("month", "January", "bookings", 15, "revenue", new BigDecimal("8750.00")),
                    Map.of("month", "February", "bookings", 18, "revenue", new BigDecimal("9200.00")),
                    Map.of("month", "March", "bookings", 22, "revenue", new BigDecimal("11500.00")),
                    Map.of("month", "April", "bookings", 19, "revenue", new BigDecimal("9800.00")),
                    Map.of("month", "May", "bookings", 15, "revenue", new BigDecimal("6000.00"))));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Conference analytics retrieved successfully");
            response.put("data", analytics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch conference analytics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods
    private Map<String, Object> createConferenceRoom(Long id, String name, int capacity, String status) {
        Map<String, Object> room = new HashMap<>();
        room.put("id", id);
        room.put("roomCode", "CR_" + String.format("%03d", id));
        room.put("name", name);
        room.put("capacity", capacity);
        room.put("status", status);
        room.put("hourlyRate", new BigDecimal((50 + capacity) + ".00"));
        room.put("size", capacity * 2 + " sq ft");
        room.put("features", Arrays.asList("Wi-Fi", "Projector", "Sound System", "AC"));
        room.put("floor", id <= 2 ? 1 : 2);
        return room;
    }

    private Map<String, Object> createAvailabilitySlot(String room, String startTime, String endTime, String status) {
        Map<String, Object> slot = new HashMap<>();
        slot.put("room", room);
        slot.put("startTime", startTime);
        slot.put("endTime", endTime);
        slot.put("status", status);
        slot.put("duration", "Available");
        slot.put("bookingId", status.equals("BOOKED") ? "CNF_123456" : null);
        return slot;
    }

    private Map<String, Object> createConferenceBooking(Long id, String eventName, String room, String status) {
        Map<String, Object> booking = new HashMap<>();
        booking.put("id", id);
        booking.put("bookingNumber", "CNF_" + String.format("%06d", id));
        booking.put("eventName", eventName);
        booking.put("room", room);
        booking.put("status", status);
        booking.put("organizer", "Event Organizer " + id);
        booking.put("attendees", 25 + id * 5);
        booking.put("eventDate", LocalDate.now().plusDays(id * 3).toString());
        booking.put("startTime", "09:00");
        booking.put("endTime", "17:00");
        booking.put("totalCost", new BigDecimal((500 + id * 200) + ".00"));
        return booking;
    }

    private Map<String, Object> createEquipment(Long id, String name, String category, String status) {
        Map<String, Object> equipment = new HashMap<>();
        equipment.put("id", id);
        equipment.put("equipmentCode", "EQP_" + String.format("%03d", id));
        equipment.put("name", name);
        equipment.put("category", category);
        equipment.put("status", status);
        equipment.put("rentalRate", new BigDecimal((20 + id * 10) + ".00"));
        equipment.put("location", "Equipment Room " + ((id % 3) + 1));
        equipment.put("lastMaintenance", LocalDate.now().minusDays(id * 10).toString());
        equipment.put("nextMaintenance", LocalDate.now().plusDays(90 - id * 10).toString());
        return equipment;
    }

    private Map<String, Object> createCateringPackage(Long id, String name, String type, BigDecimal pricePerPerson) {
        Map<String, Object> package_ = new HashMap<>();
        package_.put("id", id);
        package_.put("packageCode", "CAT_" + String.format("%03d", id));
        package_.put("name", name);
        package_.put("type", type);
        package_.put("pricePerPerson", pricePerPerson);
        package_.put("minimumPeople", 10);
        package_.put("description", "Delicious " + name.toLowerCase() + " for your event");
        package_.put("includes", Arrays.asList("Food", "Beverages", "Setup", "Service"));
        package_.put("dietaryOptions", Arrays.asList("Vegetarian", "Vegan", "Gluten-free"));
        return package_;
    }

    private Map<String, Object> createEventPackage(Long id, String name, String type, BigDecimal price) {
        Map<String, Object> package_ = new HashMap<>();
        package_.put("id", id);
        package_.put("packageCode", "EVT_" + String.format("%03d", id));
        package_.put("name", name);
        package_.put("type", type);
        package_.put("price", price);
        package_.put("duration", "Full Day");
        package_.put("includes", Arrays.asList("Room Rental", "Basic Equipment", "Coffee Breaks", "Setup"));
        package_.put("maxAttendees", type.equals("RETREAT") ? 50 : 100);
        package_.put("popular", id <= 3);
        return package_;
    }

    private Map<String, Object> createSetupTemplate(String style, String capacity, String type) {
        Map<String, Object> template = new HashMap<>();
        template.put("style", style);
        template.put("capacity", capacity);
        template.put("type", type);
        template.put("setupTime", "30 minutes");
        template.put("requirements", Arrays.asList("Tables", "Chairs", "Sound System"));
        template.put("suitableFor", Arrays.asList("Meetings", "Presentations", "Training"));
        template.put("diagram", "setup_" + type.toLowerCase() + ".png");
        return template;
    }
}
