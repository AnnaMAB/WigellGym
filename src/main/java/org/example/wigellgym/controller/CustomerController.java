package org.example.wigellgym.controller;

import org.example.wigellgym.dto.BookingDTO;
import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.services.BookingServiceImpl;
import org.example.wigellgym.services.WorkoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/wigellgym")
public class CustomerController {

    private final WorkoutServiceImpl workoutService;
    private final BookingServiceImpl bookingService;


    @Autowired
    public CustomerController(WorkoutServiceImpl workoutService, BookingServiceImpl bookingService) {
        this.workoutService = workoutService;
        this.bookingService = bookingService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/workouts")
    public ResponseEntity<Map<String, Set<String>>> getAllWorkouts() {
        return ResponseEntity.ok(workoutService.getAllWorkouts());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mybookings")
    public ResponseEntity<List<BookingDTO>> getCustomerBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/bookworkout")
    public ResponseEntity<BookingDTO> bookWorkout(@RequestBody WorkoutDTO workoutToBook) {
        return ResponseEntity.ok(bookingService.makeBooking(workoutToBook));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cancelworkout")
    public ResponseEntity<BookingDTO> cancelBooking(@RequestBody BookingDTO booking) {
        return ResponseEntity.ok(bookingService.cancelBooking(booking));
    }

}
