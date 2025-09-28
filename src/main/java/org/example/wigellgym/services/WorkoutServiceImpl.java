package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Speciality;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.repositories.InstructorRepository;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.*;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final InstructorRepository instructorRepository;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, InstructorRepository instructorRepository) {
        this.workoutRepository = workoutRepository;
        this.instructorRepository = instructorRepository;
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
        F_LOG.info("USER listed all workouts");
        return allWorkouts;
    }

    @Transactional
    @Override
    public Workout addWorkout(WorkoutDTO workoutDto) {
        Workout newWorkout = new Workout();
        if(workoutDto.getName() == null|| workoutDto.getName().isEmpty()) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid name");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name required"
            );
        }
        if(workoutDto.getTypeOfWorkout() == null || workoutDto.getTypeOfWorkout().isEmpty()){
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid type");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Type of workout required"
            );
        }
        if(workoutDto.getLocation() == null || workoutDto.getLocation().isEmpty()) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid location");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Location required"
            );
        }
        if (workoutDto.getInstructorId() == null){
            F_LOG.warn("ADMIN tried to add a workout missing instructor");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Instructor required"
            );
        }
        if(workoutDto.getMaxParticipants() == null || workoutDto.getMaxParticipants()==0) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid MaxParticipants");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires at least one participant"
            );
        }
        if(workoutDto.getBasePriceSek() == null) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid price");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires at price, if free put: 0 for price"
            );
        }
        if(workoutDto.getDateTime() == null) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid date and time");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires a date and time."
            );
        }
        if(workoutDto.getDurationInMinutes() == null) {
            F_LOG.warn("ADMIN tried to add a workout with missing or invalid duration.");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires a duration in minutes."
            );
        }
        if(workoutDto.getCancelled() == true) {
            F_LOG.warn("ADMIN tried to add a workout that's cancelled from the start.");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout can not be created as cancelled."
            );
        }
        Instructor instructor = instructorRepository.findById(workoutDto.getInstructorId())
                .orElseThrow(() -> {
                    F_LOG.warn("ADMIN tried to add a workout with a non-existing instructor (id: {})",
                            workoutDto.getInstructorId());
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Instructor not found"
                    );
                });
        newWorkout.setName(workoutDto.getName());
        newWorkout.setTypeOfWorkout(workoutDto.getTypeOfWorkout());
        newWorkout.setLocation(workoutDto.getLocation());
        newWorkout.setInstructor(instructor);
        newWorkout.setMaxParticipants(workoutDto.getMaxParticipants());
        newWorkout.setDateTime(workoutDto.getDateTime());
        newWorkout.setEndTime(workoutDto.getDateTime().plusMinutes(workoutDto.getDurationInMinutes()));
        newWorkout.setInstructorSkillPriceMultiplier(getMultiplier(instructor, workoutDto.getTypeOfWorkout()));
        newWorkout.setPriceSek(workoutDto.getBasePriceSek() * newWorkout.getInstructorSkillPriceMultiplier());
        newWorkout.setFreeSpots(newWorkout.getMaxParticipants());
        checkAvailability(newWorkout);
        Workout savedWorkout = workoutRepository.save(newWorkout);
        F_LOG.info("ADMIN added a new workout with id: {}", savedWorkout.getId());
        return savedWorkout;
    }

    @Transactional
    @Override
    public Workout updateWorkout(WorkoutDTO newWorkout) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(newWorkout.getId());
        Workout workoutToUpdate = optionalWorkout.orElseThrow(() -> {
            F_LOG.warn("ADMIN tried to update a workout with id {} that doesn't exist.", newWorkout.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", newWorkout.getId())
            );
        });
        List<String> parts = new ArrayList<>();
        if (newWorkout.getName() != null && !newWorkout.getName().equals(workoutToUpdate.getName())) {
            if(newWorkout.getName().isEmpty()) {
                F_LOG.warn("ADMIN tried to update a workout with invalid name");
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Name can not be left blank"
                );
            }
            workoutToUpdate.setName(newWorkout.getName());
            parts.add("name");
        }
        Boolean newMultiplier = false;
        if(newWorkout.getTypeOfWorkout() != null && !newWorkout.getTypeOfWorkout().equals(workoutToUpdate.getTypeOfWorkout())){
            if(newWorkout.getTypeOfWorkout().isEmpty()) {
                F_LOG.warn("ADMIN tried to update a workout with invalid typeOfWorkout");
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "TypeofWorkout can not be left blank"
                );
            }
            workoutToUpdate.setTypeOfWorkout(newWorkout.getTypeOfWorkout());
            newMultiplier = true;
            parts.add("typeOfWorkout");
        }
        if(newWorkout.getLocation() != null && !newWorkout.getLocation().equals(workoutToUpdate.getLocation())) {
            if(newWorkout.getLocation().isEmpty()) {
                F_LOG.warn("ADMIN tried to update a workout with invalid location");
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "location can not be left blank"
                );
            }
            workoutToUpdate.setLocation(newWorkout.getLocation());
            parts.add("location");
        }
        Boolean newInstructor = !newWorkout.getInstructorId().equals(workoutToUpdate.getInstructor().getId());
        double basePrice = workoutToUpdate.getPriceSek()/ workoutToUpdate.getInstructorSkillPriceMultiplier();
        if (newWorkout.getInstructorId() != null && newInstructor){
            Instructor instructor = instructorRepository.findById(newWorkout.getInstructorId())
                    .orElseThrow(() -> {
                        F_LOG.warn("ADMIN tried to update a workout with a non-existing instructor (id: {})",
                                newWorkout.getInstructorId());
                        return new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Instructor not found"
                        );
                    });
            newMultiplier = true;
            workoutToUpdate.setInstructor(instructor);
            parts.add("instructor");
        }
        if(newWorkout.getMaxParticipants() != null && newWorkout.getMaxParticipants().equals(workoutToUpdate.getMaxParticipants())) {
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
        Long duration = Duration.between(workoutToUpdate.getDateTime(), workoutToUpdate.getEndTime()).toMinutes();
        boolean timeChanged = false;
        if(newWorkout.getDurationInMinutes() != null && newWorkout.getDurationInMinutes() != duration) {
            if(newWorkout.getDurationInMinutes()==0) {
                F_LOG.warn("ADMIN tried to update a workout with missing or invalid duration.");
                throw new ResponseStatusException(
                       HttpStatus.BAD_REQUEST,
                       "A workout requires a duration in minutes."
               );
            } else {
                timeChanged = true;
                duration = newWorkout.getDurationInMinutes();
                parts.add("duration");
            }
        }
        if(newWorkout.getDateTime() != null && !newWorkout.getDateTime().equals(workoutToUpdate.getDateTime())) {
            workoutToUpdate.setDateTime(newWorkout.getDateTime());
            timeChanged = true;
            parts.add("dateTime");
        }
        if (timeChanged) {
            workoutToUpdate.setEndTime(workoutToUpdate.getDateTime().plusMinutes(duration));
            parts.add("endTime");
        }
        if(newWorkout.getCancelled() != null && newWorkout.getCancelled() != workoutToUpdate.isCancelled())  {
            cancelWorkout(workoutToUpdate);
            workoutToUpdate.setCancelled(newWorkout.getCancelled());
            parts.add("cancelled");
        }
        if (newWorkout.getBasePriceSek() != null || newMultiplier) {
            if (newMultiplier) {
                workoutToUpdate.setInstructorSkillPriceMultiplier(getMultiplier(workoutToUpdate.getInstructor(), workoutToUpdate.getTypeOfWorkout()));
            }
            if (newWorkout.getBasePriceSek() != null && !newWorkout.getBasePriceSek().equals(basePrice)) {
                basePrice = newWorkout.getBasePriceSek();
            }
            double newPriceSek = basePrice * workoutToUpdate.getInstructorSkillPriceMultiplier();
            workoutToUpdate.setPriceSek(newPriceSek);
            parts.add("calculated price");
        }
        checkAvailability(workoutToUpdate);
        String updated = String.join(", ", parts);
        F_LOG.info("ADMIN updated workout {} in the following fields: {}.", workoutToUpdate, updated);
        return workoutRepository.save(workoutToUpdate);
    }


    @Transactional
    @Override                                   //KLAR?
    public String deleteWorkout(Integer id) {
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
        return String.format("Entry with Id: %s has been successfully deleted.", id);
    }

    @Transactional
                                //KLAR? @Override???
    public void cancelWorkout(Workout workout) {
        List<Booking> bookings = workout.getBookings();
        for (Booking booking : bookings) {
            booking.setCancelled(true);
        }
        workout.setCancelled(true);
        workoutRepository.save(workout);
        F_LOG.info("ADMIN cancelled workout with id: {}", workout.getId());
    }


    private void checkAvailability(Workout workout) {
        int bufferTimeLocation = 5;
        int bufferTimeInstructor = 10;

        boolean locationUnavailable = workoutRepository.existsByLocationAndCancelledFalseAndDateTimeLessThanAndEndTimeGreaterThan(
                workout.getLocation(), workout.getEndTime().plusMinutes(bufferTimeLocation),workout.getDateTime().minusMinutes(bufferTimeLocation));
        if (locationUnavailable) {
            F_LOG.warn("ADMIN tried to schedule a workout with an unavailable location.");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The location is unavailable at that time."
            );
        }
        boolean instructorUnavailable = workoutRepository.existsByInstructorAndCancelledFalseAndDateTimeLessThanAndEndTimeGreaterThan(
                workout.getInstructor(), workout.getEndTime().plusMinutes(bufferTimeInstructor),workout.getDateTime().minusMinutes(bufferTimeInstructor));
        if(instructorUnavailable) {
            F_LOG.warn("ADMIN tried to schedule a workout with an unavailable instructor.");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "The instructor is not available."
            );
        }
    }


    private double getMultiplier(Instructor instructor, String typeOfWorkout) {
        double toReturn = 1.0;
        for (Speciality instructorSpeciality : instructor.getSpeciality()) {
            if (instructorSpeciality.getType().equalsIgnoreCase(typeOfWorkout)) {
                toReturn = 1.0 + (instructorSpeciality.getCertificateLevel()* 0.1);
                break;
            }
        }
        return toReturn;
    }

}
