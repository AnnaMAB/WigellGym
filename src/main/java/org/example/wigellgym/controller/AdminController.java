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
@RequestMapping("/api/wigellgym/")
public class AdminController {

    private final WorkoutServiceImpl workoutService;
    private final InstructorServiceImpl instructorService;
    private final BookingServiceImpl bookingService;

    @Autowired
    public AdminController(WorkoutServiceImpl workoutService, InstructorServiceImpl instructorService, BookingServiceImpl bookingService) {
        this.workoutService = workoutService;
        this.instructorService = instructorService;
        this.bookingService = bookingService;
    }

    @GetMapping("listcanceled")
    public ResponseEntity<List<Booking>> listCanceledBookings() {
        return ResponseEntity.ok(bookingService.getCanceledBookings());
    }

    @GetMapping("listupcoming")
    public ResponseEntity<List<Booking>> listUpcomingBookings() {
        return ResponseEntity.ok(bookingService.getUpcomingBookings());
    }

    @GetMapping("listpast")
    public ResponseEntity<List<Booking>> listPastBookings() {
        return ResponseEntity.ok(bookingService.getOldBookings());
    }

    @PostMapping("addworkout")
    public ResponseEntity<Workout> addWorkout(@RequestBody Workout workout) {
        return ResponseEntity.ok(workoutService.addWorkout(workout));
    }

    @PutMapping("updateworkout")
    public ResponseEntity<Workout> updateWorkout(@RequestBody Workout workout) {
        return ResponseEntity.ok(workoutService.updateWorkout(workout));
    }

    @DeleteMapping("remworkout/{id}")
    public ResponseEntity<String> deleteWorkout(@PathVariable Integer id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.ok(String.format("Entry with Id: %s has been successfully deleted.", id));
    }

    @PostMapping("addinstructor")
    public ResponseEntity<Instructor> addInstructor(@RequestBody Instructor instructor) {
        return ResponseEntity.ok(instructorService.addInstructor(instructor));
    }
}
