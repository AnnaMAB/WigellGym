package org.example.wigellgym.services;

import java.util.stream.Collectors;
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

import java.util.*;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final ConversionServiceImpl conversionService;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, ConversionServiceImpl conversionService) {
        this.conversionService = conversionService;
        this.workoutRepository = workoutRepository;
    }

    @Override                               //klar?
    public Map<String, Set<String>> getAllWorkouts() {
        Set<String> typesOfWorkout = workoutRepository.findTypeOfWorkout();
        Set<String> names;
        Map<String, Set<String>> allWorkouts = new HashMap<>();

        for (String type : typesOfWorkout) {
            names = workoutRepository.findNamesByTypeOfWorkout(type);
            allWorkouts.put(type, names);
        }
        return allWorkouts;
    }

    @Transactional
    @Override
    public Workout addWorkout(Workout workout) {
        if(workout.getName() == null|| workout.getName().isEmpty()) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name required"
            );
        }
        if(workout.getTypeOfWorkout() == null || workout.getTypeOfWorkout().isEmpty()){
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Type of workout required"
            );
        }
        if(workout.getLocation() == null || workout.getLocation().isEmpty()) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Location required"
            );
        }
        if (workout.getInstructor() == null){
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Instructor required"
            );
        }
        if(workout.getMaxParticipants() == null || workout.getMaxParticipants()==0) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires at least one participant"
            );
        }
        if(workout.getPriceSek() == null) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires at price, if free put: 0 for price"
            );
        }
        if(workout.getDate() == null) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires a date"
            );
        }
        double euroRate = conversionService.getConversionRate();
        workout.setPreliminaryPriceEuro(workout.getPriceSek()*euroRate);
        workout.setFreeSpots(workout.getMaxParticipants());
        Workout savedWorkout = workoutRepository.save(workout);
        if (euroRate == 0.0) {
            F_LOG.warn("ADMIN added a new workout with id: {}. Could not reach conversion API: PreliminaryPriceEuro set to 0", savedWorkout.getId());
        } else {
            F_LOG.info("ADMIN added a new workout with id: {}", savedWorkout.getId());
        }
        return savedWorkout;
    }

    @Transactional
    @Override
    public Workout updateWorkout(Workout newWorkout) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(newWorkout.getId());
        Workout workoutToUpdate = optionalWorkout.orElseThrow(() -> {
            F_LOG.warn("ADMIN tried to update a workout with id {} that doesn't exist.", newWorkout.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", newWorkout.getId())
            );
        });
        List<String> parts = new ArrayList<>();
        if (newWorkout.getName() != null && !newWorkout.getName().isEmpty()) {
            workoutToUpdate.setName(newWorkout.getName());
            parts.add("name");
        }
        if(newWorkout.getTypeOfWorkout() != null && newWorkout.getTypeOfWorkout().isEmpty()){
            workoutToUpdate.setTypeOfWorkout(newWorkout.getTypeOfWorkout());
            parts.add("typeOfWorkout");
        }
        if(newWorkout.getLocation() != null && newWorkout.getLocation().isEmpty()) {
            workoutToUpdate.setLocation(newWorkout.getLocation());
            parts.add("location");
        }
        if (newWorkout.getInstructor() == null){
            workoutToUpdate.setInstructor(newWorkout.getInstructor());
            parts.add("instructor");
        }
        if(newWorkout.getMaxParticipants() != null) {
            if(newWorkout.getMaxParticipants()==0) {
                F_LOG.warn("ADMIN tried to update a workout to 0 participants");
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "A workout requires at least one participant"
                );
            } else {
                workoutToUpdate.setMaxParticipants(newWorkout.getMaxParticipants());
                parts.add("maxParticipants");
            }
        }
        if(newWorkout.getPriceSek() != null) {
            workoutToUpdate.setPriceSek(newWorkout.getPriceSek());
            parts.add("price");
        }
        if(newWorkout.getDate() != null) {
            workoutToUpdate.setDate(newWorkout.getDate());
            parts.add("date");
        }
        workoutToUpdate.setPreliminaryPriceEuro(workoutToUpdate.getPriceSek()*conversionService.getConversionRate());
        String updated = String.join(", ", parts);
        F_LOG.info("ADMIN updated workout {} in the following fields: {}.", workoutToUpdate, updated);
        return workoutRepository.save(workoutToUpdate);
    }


    @Transactional
    @Override                                   //KLAR?
    public void deleteWorkout(Integer id) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(id);
        Workout workout = optionalWorkout.orElseThrow(() -> {
            F_LOG.warn("ADMIN tried to delete a workout with id {} that doesn't exist.", id);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", id)
            );
        });
        List<Booking> bookings = workout.getBookings();
        for (Booking booking : bookings) {
            booking.setCancelled(true);
        }
        workoutRepository.deleteById(id);
        F_LOG.info("ADMIN deleted workout with id: {}", id);
    }
}
