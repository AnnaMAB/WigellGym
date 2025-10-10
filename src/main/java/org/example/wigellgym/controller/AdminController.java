package org.example.wigellgym.controller;

import org.example.wigellgym.dto.BookingDTO;
import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.services.BookingServiceImpl;
import org.example.wigellgym.services.InstructorServiceImpl;
import org.example.wigellgym.services.WorkoutServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wigellgym")
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listcanceled")
    public ResponseEntity<List<BookingDTO>> listCanceledBookings() {
        return ResponseEntity.ok(bookingService.getCanceledBookings());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listupcoming")
    public ResponseEntity<List<BookingDTO>> listUpcomingBookings() {
        return ResponseEntity.ok(bookingService.getUpcomingBookings());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listpast")
    public ResponseEntity<List<BookingDTO>> listPastBookings() {
        return ResponseEntity.ok(bookingService.getOldBookings());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addworkout")
    public ResponseEntity<Workout> addWorkout(@RequestBody WorkoutDTO workoutDto) {
        return ResponseEntity.ok(workoutService.addWorkout(workoutDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateworkout")
    public ResponseEntity<Workout> updateWorkout(@RequestBody WorkoutDTO newWorkout) {
        return ResponseEntity.ok(workoutService.updateWorkout(newWorkout));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/remworkout/{id}")
    public ResponseEntity<String> deleteWorkout(@PathVariable Integer id) {
        return ResponseEntity.ok(workoutService.deleteWorkout(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addinstructor")
    public ResponseEntity<Instructor> addInstructor(@RequestBody Instructor instructor) {
        return ResponseEntity.ok(instructorService.addInstructor(instructor));
    }
}
