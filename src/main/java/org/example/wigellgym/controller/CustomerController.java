package org.example.wigellgym.controller;

import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.services.BookingServiceImpl;
import org.example.wigellgym.services.InstructorServiceImpl;
import org.example.wigellgym.services.WorkoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wigellgym")
public class CustomerController {

    private final WorkoutServiceImpl workoutService;
    private final BookingServiceImpl bookingService;


    @Autowired
    public CustomerController(WorkoutServiceImpl workoutService, BookingServiceImpl bookingService) {
        this.workoutService = workoutService;
        this.bookingService = bookingService;
    }

    @GetMapping("/workouts")
    public ResponseEntity<List<Workout>> getAllWorkouts() {
        return ResponseEntity.ok(workoutService.getAllWorkouts());
    }


    @GetMapping("/mybookings")
    public ResponseEntity<List<Booking>> getCustomerBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @PostMapping("/bookworkout")
    public ResponseEntity<Booking> bookWorkout(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.makeBooking(booking));
    }

    @PutMapping("cancelworkout")
    public ResponseEntity<Booking> cancelBooking(@RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.cancelBooking(booking));
    }

}
