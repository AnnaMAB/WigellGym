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

    @GetMapping("listcanceled")                     //• Lista avbokningar
    public ResponseEntity<List<Booking>> listCanceledBookings() {
        return ResponseEntity.ok(bookingService.getCanceledBookings());
    }

    @GetMapping("listupcoming")                     //• Lista kommande bokningar
    public ResponseEntity<List<Booking>> listUpcomingBookings() {
        return ResponseEntity.ok(bookingService.getUpcomingBookings());
    }

    @GetMapping("listpast")                         //• Lista historiska bokningar
    public ResponseEntity<List<Booking>> listPastBookings() {
        return ResponseEntity.ok(bookingService.getOldBookings());
    }

    @PostMapping("addworkout")                     //• Lägg till träningspass
    public ResponseEntity<Workout> addWorkout(@RequestBody Workout workout) {
        return ResponseEntity.ok(workoutService.addWorkout(workout));
    }

    @PutMapping("updateworkout")                //• Uppdatera träningspass
    public ResponseEntity<Workout> updateWorkout(@RequestBody Workout workout) {
        return ResponseEntity.ok(workoutService.updateWorkout(workout));
    }

    @DeleteMapping("remworkout/{id}")               //• Radera träningspass
    public ResponseEntity<String> deleteWorkout(@PathVariable Integer id) {
        workoutService.deleteWorkout(id);
        return ResponseEntity.ok(String.format("Entry with Id: %s has been successfully deleted.", id));
    }

    @PostMapping("addinstructor")               //• Lägg till instruktör
    public ResponseEntity<Instructor> addInstructor(@RequestBody Instructor instructor) {
        return ResponseEntity.ok(instructorService.addInstructor(instructor));
    }

    @GetMapping("instructors")         //• Lista instruktörer
    public ResponseEntity<List<Instructor>> listInstructors() {
        return ResponseEntity.ok(instructorService.getInstructors());
    }

}
