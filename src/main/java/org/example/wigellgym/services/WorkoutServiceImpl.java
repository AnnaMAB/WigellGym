package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }


    @Override                               //klar?
    public Set<String> getAllWorkouts() {
        List<Workout> workouts = workoutRepository.findAll();

        Set<String> workoutTypes = new HashSet<>();

        for (Workout workout : workouts) {
            workoutTypes.add(workout.getTypeOfWorkout());
        }
        F_LOG.info("USER displayed all workout types");
        return workoutTypes;
    }


    @Override                               //TODO-----EURO
    public Workout addWorkout(Workout workout) {
        if(workout.getName() == null|| workout.getName().isEmpty()) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Name required")
            );
        }
        if(workout.getTypeOfWorkout() == null || workout.getTypeOfWorkout().isEmpty()){
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Type of workout required")
            );
        }
        if(workout.getLocation() == null || workout.getLocation().isEmpty()) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Location required")
            );
        }
        if (workout.getInstructor() == null){
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Instructor required")
            );
        }
        if(workout.getMaxParticipants() == null || workout.getMaxParticipants()==0) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("A workout requires at least one participant")
            );
        }
        if(workout.getPriceSek() == null) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("A workout requires at price, if free put: 0 for price")
            );
        }
        if(workout.getDate() == null) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("A workout requires a date")
            );
        }
        workout.setFreeSpots(workout.getMaxParticipants());
        Workout savedWorkout = workoutRepository.save(workout);
        F_LOG.info("ADMIN added a new workout wit id: {}", savedWorkout.getId());
        return savedWorkout;
    }


    @Override                               //klar?//TODO-----LOG
    public Workout updateWorkout(Workout newWorkout) {
        Workout workoutToUpdate = workoutRepository.findById(newWorkout.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No workout exists with id: %d.", newWorkout.getId())
                ));
        if (newWorkout.getName() != null && !newWorkout.getName().isEmpty()) {
            workoutToUpdate.setName(newWorkout.getName());
        }
        if(newWorkout.getTypeOfWorkout() != null && newWorkout.getTypeOfWorkout().isEmpty()){
            workoutToUpdate.setTypeOfWorkout(newWorkout.getTypeOfWorkout());
        }
        if(newWorkout.getLocation() != null && newWorkout.getLocation().isEmpty()) {
            workoutToUpdate.setLocation(newWorkout.getLocation());
        }
        if (newWorkout.getInstructor() == null){
            workoutToUpdate.setInstructor(newWorkout.getInstructor());
        }
        if(newWorkout.getMaxParticipants() != null) {
            if(newWorkout.getMaxParticipants()==0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("A workout requires at least one participant")
                );
            } else {
                workoutToUpdate.setMaxParticipants(newWorkout.getMaxParticipants());
            }
        }
        if(newWorkout.getPriceSek() != null) {
            workoutToUpdate.setPriceSek(newWorkout.getPriceSek());
        }
        if(newWorkout.getDate() != null) {
            workoutToUpdate.setDate(newWorkout.getDate());
        }
        return workoutRepository.save(workoutToUpdate);
    }


    @Transactional
    @Override                                   //KLAR?//TODO-----LOG
    public void deleteWorkout(Integer id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No workout exists with id: %d.", id)
                ));
        List<Booking> bookings = workout.getBookings();
        for (Booking booking : bookings) {
            booking.setCancelled(true);
        }
        workoutRepository.deleteById(id);
        F_LOG.info("ADMIN deleted workout with id: {}", id);
    }
}
