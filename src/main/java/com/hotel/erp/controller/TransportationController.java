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
 * Transportation Management Controller
 * Handles fleet management, vehicle bookings, airport transfers, and driver
 * scheduling
 */
@RestController
@RequestMapping("/api/transportation")
@CrossOrigin(origins = "*")
public class TransportationController {

    /**
     * Get all vehicles in fleet
     */
    @GetMapping("/vehicles")
    public ResponseEntity<Map<String, Object>> getAllVehicles() {
        try {
            List<Map<String, Object>> vehicles = Arrays.asList(
                    createVehicle(1L, "Mercedes S-Class", "LUXURY_SEDAN", "AVAILABLE"),
                    createVehicle(2L, "BMW X5", "SUV", "AVAILABLE"),
                    createVehicle(3L, "Toyota Hiace", "VAN", "IN_USE"),
                    createVehicle(4L, "Audi A8", "LUXURY_SEDAN", "MAINTENANCE"),
                    createVehicle(5L, "Ford Transit", "SHUTTLE", "AVAILABLE"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Fleet vehicles retrieved successfully");
            response.put("data", vehicles);
            response.put("available", 3);
            response.put("total", vehicles.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch vehicles: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Add new vehicle to fleet
     */
    @PostMapping("/vehicles")
    public ResponseEntity<Map<String, Object>> addVehicle(@RequestBody Map<String, Object> vehicleData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> vehicle = new HashMap<>(vehicleData);
            vehicle.put("id", newId);
            vehicle.put("vehicleCode", "VEH_" + newId);
            vehicle.put("status", "AVAILABLE");
            vehicle.put("addedDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Vehicle added to fleet successfully");
            response.put("data", vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to add vehicle: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vehicle bookings
     */
    @GetMapping("/bookings")
    public ResponseEntity<Map<String, Object>> getBookings(@RequestParam(required = false) String status,
            @RequestParam(required = false) String vehicleType) {
        try {
            List<Map<String, Object>> bookings = Arrays.asList(
                    createBooking(1L, "Airport Transfer", "LUXURY_SEDAN", "CONFIRMED"),
                    createBooking(2L, "City Tour", "SUV", "PENDING"),
                    createBooking(3L, "Corporate Event", "VAN", "COMPLETED"),
                    createBooking(4L, "Wedding Transfer", "LUXURY_SEDAN", "CONFIRMED"),
                    createBooking(5L, "Group Shuttle", "SHUTTLE", "IN_PROGRESS"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Vehicle bookings retrieved successfully");
            response.put("data", bookings);
            response.put("filters", Map.of("status", status, "vehicleType", vehicleType));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch bookings: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create vehicle booking
     */
    @PostMapping("/bookings")
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody Map<String, Object> bookingData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> booking = new HashMap<>(bookingData);
            booking.put("id", newId);
            booking.put("bookingNumber", "TRN_" + newId);
            booking.put("status", "PENDING");
            booking.put("bookingDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Vehicle booking created successfully");
            response.put("data", booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to create booking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get driver schedules
     */
    @GetMapping("/drivers")
    public ResponseEntity<Map<String, Object>> getDrivers() {
        try {
            List<Map<String, Object>> drivers = Arrays.asList(
                    createDriver(1L, "John Williams", "AVAILABLE", "4.8"),
                    createDriver(2L, "Michael Brown", "ON_DUTY", "4.9"),
                    createDriver(3L, "Robert Davis", "OFF_DUTY", "4.7"),
                    createDriver(4L, "David Wilson", "AVAILABLE", "4.6"),
                    createDriver(5L, "James Smith", "ON_LEAVE", "4.5"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Drivers retrieved successfully");
            response.put("data", drivers);
            response.put("available", 2);
            response.put("onDuty", 1);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch drivers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Add new driver
     */
    @PostMapping("/drivers")
    public ResponseEntity<Map<String, Object>> addDriver(@RequestBody Map<String, Object> driverData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> driver = new HashMap<>(driverData);
            driver.put("id", newId);
            driver.put("driverCode", "DRV_" + newId);
            driver.put("status", "AVAILABLE");
            driver.put("joinDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Driver added successfully");
            response.put("data", driver);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to add driver: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get driver schedule for specific date
     */
    @GetMapping("/drivers/{driverId}/schedule")
    public ResponseEntity<Map<String, Object>> getDriverSchedule(@PathVariable Long driverId,
            @RequestParam(required = false) String date) {
        try {
            List<Map<String, Object>> schedule = Arrays.asList(
                    createScheduleEntry("08:00", "Airport Transfer", "Terminal 1", "CONFIRMED"),
                    createScheduleEntry("10:30", "City Tour", "Downtown", "IN_PROGRESS"),
                    createScheduleEntry("14:00", "Hotel Transfer", "Grand Plaza", "PENDING"),
                    createScheduleEntry("16:30", "Corporate Pickup", "Business Center", "CONFIRMED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Driver schedule retrieved successfully");
            response.put("data", schedule);
            response.put("driverId", driverId);
            response.put("date", date != null ? date : LocalDate.now().toString());
            response.put("totalTrips", schedule.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch driver schedule: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get vehicle maintenance records
     */
    @GetMapping("/maintenance")
    public ResponseEntity<Map<String, Object>> getMaintenanceRecords(@RequestParam(required = false) String vehicleId) {
        try {
            List<Map<String, Object>> maintenance = Arrays.asList(
                    createMaintenanceRecord(1L, "Mercedes S-Class", "Oil Change", "COMPLETED"),
                    createMaintenanceRecord(2L, "BMW X5", "Tire Replacement", "SCHEDULED"),
                    createMaintenanceRecord(3L, "Toyota Hiace", "Engine Service", "IN_PROGRESS"),
                    createMaintenanceRecord(4L, "Audi A8", "Brake Check", "COMPLETED"),
                    createMaintenanceRecord(5L, "Ford Transit", "AC Service", "SCHEDULED"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Maintenance records retrieved successfully");
            response.put("data", maintenance);
            response.put("pendingMaintenance", 2);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch maintenance records: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Schedule vehicle maintenance
     */
    @PostMapping("/maintenance")
    public ResponseEntity<Map<String, Object>> scheduleMaintenance(@RequestBody Map<String, Object> maintenanceData) {
        try {
            Long newId = System.currentTimeMillis();
            Map<String, Object> maintenance = new HashMap<>(maintenanceData);
            maintenance.put("id", newId);
            maintenance.put("serviceNumber", "SRV_" + newId);
            maintenance.put("status", "SCHEDULED");
            maintenance.put("scheduledDate", LocalDateTime.now().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Maintenance scheduled successfully");
            response.put("data", maintenance);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to schedule maintenance: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get trip history
     */
    @GetMapping("/trips")
    public ResponseEntity<Map<String, Object>> getTripHistory(@RequestParam(required = false) String guestId,
            @RequestParam(required = false) String period) {
        try {
            List<Map<String, Object>> trips = Arrays.asList(
                    createTrip(1L, "Airport to Hotel", "Mr. Johnson", "COMPLETED", new BigDecimal("45.00")),
                    createTrip(2L, "City Tour", "Mrs. Smith", "COMPLETED", new BigDecimal("120.00")),
                    createTrip(3L, "Hotel to Restaurant", "Mr. Brown", "IN_PROGRESS", new BigDecimal("25.00")),
                    createTrip(4L, "Shopping Mall", "Ms. Davis", "COMPLETED", new BigDecimal("35.00")),
                    createTrip(5L, "Business Meeting", "Mr. Wilson", "SCHEDULED", new BigDecimal("60.00")));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Trip history retrieved successfully");
            response.put("data", trips);
            response.put("totalRevenue", new BigDecimal("285.00"));
            response.put("totalTrips", trips.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch trip history: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get transportation analytics
     */
    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getTransportationAnalytics() {
        try {
            Map<String, Object> analytics = new HashMap<>();
            analytics.put("totalVehicles", 12);
            analytics.put("availableVehicles", 8);
            analytics.put("vehicleUtilization", 75.5);
            analytics.put("averageTripDuration", "45 minutes");
            analytics.put("monthlyRevenue", new BigDecimal("15750.00"));

            analytics.put("popularDestinations", Arrays.asList(
                    Map.of("destination", "Airport", "trips", 156, "percentage", 35.2),
                    Map.of("destination", "City Center", "trips", 89, "percentage", 20.1),
                    Map.of("destination", "Shopping Mall", "trips", 67, "percentage", 15.1),
                    Map.of("destination", "Business District", "trips", 54, "percentage", 12.2),
                    Map.of("destination", "Tourist Attractions", "trips", 77, "percentage", 17.4)));

            analytics.put("vehiclePerformance", Arrays.asList(
                    Map.of("vehicle", "Mercedes S-Class", "trips", 45, "revenue", new BigDecimal("2250.00"), "rating",
                            4.8),
                    Map.of("vehicle", "BMW X5", "trips", 38, "revenue", new BigDecimal("1900.00"), "rating", 4.7),
                    Map.of("vehicle", "Toyota Hiace", "trips", 67, "revenue", new BigDecimal("2010.00"), "rating",
                            4.6)));

            analytics.put("driverPerformance", Arrays.asList(
                    Map.of("driver", "John Williams", "trips", 89, "rating", 4.8, "earnings", new BigDecimal("890.00")),
                    Map.of("driver", "Michael Brown", "trips", 76, "rating", 4.9, "earnings",
                            new BigDecimal("760.00"))));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Transportation analytics retrieved successfully");
            response.put("data", analytics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch analytics: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Get fuel consumption tracking
     */
    @GetMapping("/fuel-tracking")
    public ResponseEntity<Map<String, Object>> getFuelTracking(@RequestParam(required = false) String vehicleId) {
        try {
            List<Map<String, Object>> fuelRecords = Arrays.asList(
                    createFuelRecord("Mercedes S-Class", 45.5, new BigDecimal("68.25"), 12.5),
                    createFuelRecord("BMW X5", 52.3, new BigDecimal("78.45"), 11.8),
                    createFuelRecord("Toyota Hiace", 38.2, new BigDecimal("57.30"), 15.2),
                    createFuelRecord("Audi A8", 41.7, new BigDecimal("62.55"), 13.1),
                    createFuelRecord("Ford Transit", 49.8, new BigDecimal("74.70"), 14.8));

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalFuelCost", new BigDecimal("341.25"));
            summary.put("averageConsumption", 13.48);
            summary.put("totalDistance", 2847);
            summary.put("costPerKm", new BigDecimal("0.12"));

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Fuel tracking retrieved successfully");
            response.put("data", fuelRecords);
            response.put("summary", summary);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Failed to fetch fuel tracking: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Helper methods
    private Map<String, Object> createVehicle(Long id, String model, String type, String status) {
        Map<String, Object> vehicle = new HashMap<>();
        vehicle.put("id", id);
        vehicle.put("vehicleCode", "VEH_" + String.format("%03d", id));
        vehicle.put("model", model);
        vehicle.put("type", type);
        vehicle.put("status", status);
        vehicle.put("licensePlate", "ABC" + String.format("%03d", id));
        vehicle.put("capacity", type.equals("VAN") ? 8 : (type.equals("SHUTTLE") ? 15 : 4));
        vehicle.put("year", 2020 + (int) (id % 4));
        vehicle.put("mileage", 15000 + id * 2500);
        vehicle.put("lastService", LocalDate.now().minusDays(id * 15).toString());
        return vehicle;
    }

    private Map<String, Object> createBooking(Long id, String purpose, String vehicleType, String status) {
        Map<String, Object> booking = new HashMap<>();
        booking.put("id", id);
        booking.put("bookingNumber", "TRN_" + String.format("%06d", id));
        booking.put("purpose", purpose);
        booking.put("vehicleType", vehicleType);
        booking.put("status", status);
        booking.put("guestName", "Guest " + id);
        booking.put("pickupTime", LocalDateTime.now().plusHours(id * 2).toString());
        booking.put("destination", "Destination " + id);
        booking.put("estimatedCost", new BigDecimal((30 + id * 15) + ".00"));
        booking.put("driver", "Driver " + ((id % 3) + 1));
        return booking;
    }

    private Map<String, Object> createDriver(Long id, String name, String status, String rating) {
        Map<String, Object> driver = new HashMap<>();
        driver.put("id", id);
        driver.put("driverCode", "DRV_" + String.format("%03d", id));
        driver.put("name", name);
        driver.put("status", status);
        driver.put("rating", Double.parseDouble(rating));
        driver.put("licenseNumber", "DL" + String.format("%08d", 10000000 + id));
        driver.put("phone", "+1-555-" + String.format("%04d", 2000 + id));
        driver.put("experience", 3 + (id % 10));
        driver.put("totalTrips", 150 + id * 25);
        driver.put("joinDate", LocalDate.now().minusYears(id).toString());
        return driver;
    }

    private Map<String, Object> createScheduleEntry(String time, String task, String location, String status) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("time", time);
        entry.put("task", task);
        entry.put("location", location);
        entry.put("status", status);
        entry.put("estimatedDuration", "60 minutes");
        entry.put("guestContact", "+1-555-1234");
        return entry;
    }

    private Map<String, Object> createMaintenanceRecord(Long id, String vehicle, String service, String status) {
        Map<String, Object> maintenance = new HashMap<>();
        maintenance.put("id", id);
        maintenance.put("serviceNumber", "SRV_" + String.format("%06d", id));
        maintenance.put("vehicle", vehicle);
        maintenance.put("serviceType", service);
        maintenance.put("status", status);
        maintenance.put("scheduledDate", LocalDate.now().plusDays(id * 5).toString());
        maintenance.put("estimatedCost", new BigDecimal((150 + id * 50) + ".00"));
        maintenance.put("serviceProvider", "Auto Service Center");
        maintenance.put("priority", id % 2 == 0 ? "HIGH" : "MEDIUM");
        return maintenance;
    }

    private Map<String, Object> createTrip(Long id, String route, String guest, String status, BigDecimal fare) {
        Map<String, Object> trip = new HashMap<>();
        trip.put("id", id);
        trip.put("tripNumber", "TRP_" + String.format("%06d", id));
        trip.put("route", route);
        trip.put("guest", guest);
        trip.put("status", status);
        trip.put("fare", fare);
        trip.put("startTime", LocalDateTime.now().minusHours(id).toString());
        trip.put("endTime", status.equals("COMPLETED") ? LocalDateTime.now().minusHours(id - 1).toString() : null);
        trip.put("distance", 5.5 + id * 2.3);
        trip.put("driver", "Driver " + ((id % 3) + 1));
        trip.put("vehicle", "Vehicle " + ((id % 5) + 1));
        return trip;
    }

    private Map<String, Object> createFuelRecord(String vehicle, Double liters, BigDecimal cost, Double consumption) {
        Map<String, Object> record = new HashMap<>();
        record.put("vehicle", vehicle);
        record.put("fuelLiters", liters);
        record.put("totalCost", cost);
        record.put("consumption", consumption);
        record.put("fuelDate", LocalDate.now().minusDays((int) (Math.random() * 30)).toString());
        record.put("odometerReading", 15000 + (int) (Math.random() * 50000));
        record.put("fuelStation", "Shell Gas Station");
        record.put("fuelType", "Premium Gasoline");
        return record;
    }
}
