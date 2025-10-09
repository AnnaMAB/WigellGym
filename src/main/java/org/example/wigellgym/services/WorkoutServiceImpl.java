package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Speciality;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.mapper.DtoConverter;
import org.example.wigellgym.repositories.InstructorRepository;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final InstructorRepository instructorRepository;
    private static final Logger F_LOG = LogManager.getLogger("functionality");
    private final AuthInfo authInfo;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository, InstructorRepository instructorRepository, AuthInfo authInfo) {
        this.workoutRepository = workoutRepository;
        this.instructorRepository = instructorRepository;
        this.authInfo = authInfo;
    }

    @Override
    public Map<String, Set<String>> getAllWorkouts() {
        Set<String> typesOfWorkout = workoutRepository.findTypeOfWorkout();
        Set<String> names;
        Map<String, Set<String>> allWorkouts = new HashMap<>();

        for (String type : typesOfWorkout) {
            names = workoutRepository.findNamesByTypeOfWorkout(type);
            allWorkouts.put(type, names);
        }
        F_LOG.info("{} listed all workouts", authInfo.getRole());
        return allWorkouts;
    }

    @Transactional
    @Override
    public Workout addWorkout(WorkoutDTO workoutDto) {
        String role = authInfo.getRole();
        Workout newWorkout = new Workout();
        if(workoutDto.getName() == null|| workoutDto.getName().isEmpty()) {
            F_LOG.warn("{} tried to add a workout with missing or invalid name.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name required."
            );
        }
        if(workoutDto.getTypeOfWorkout() == null || workoutDto.getTypeOfWorkout().isEmpty()){
            F_LOG.warn("{} tried to add a workout with missing or invalid type.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Type of workout required."
            );
        }
        if(workoutDto.getLocation() == null || workoutDto.getLocation().isEmpty()) {
            F_LOG.warn("{} tried to add a workout with missing or invalid location.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Location required."
            );
        }
        if (workoutDto.getInstructorUserDTO() == null){
            F_LOG.warn("{} tried to add a workout missing instructor.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Instructor required."
            );
        }
        if(workoutDto.getMaxParticipants() == null || workoutDto.getMaxParticipants()==0) {
            F_LOG.warn("{} tried to add a workout with missing or invalid MaxParticipants.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires at least one participant."
            );
        }
        if(workoutDto.getBasePricePerHourSek() == null) {
            F_LOG.warn("{} tried to add a workout with missing or invalid price.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "BasePrice required, if free put: 0."
            );
        }
        if(workoutDto.getDateTime() == null || workoutDto.getDateTime().isBefore(LocalDateTime.now())) {
            F_LOG.warn("{} tried to add a workout with missing or invalid date and time.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires a date and time that have not already passed."
            );
        }
        if(workoutDto.getDurationInMinutes() == null) {
            F_LOG.warn("{} tried to add a workout with missing or invalid duration.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout requires a duration in minutes."
            );
        }
        if(workoutDto.getCanceled() != null && workoutDto.getCanceled()) {
            F_LOG.warn("{} tried to add a workout that's canceled from the start.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A workout can not be created as canceled."
            );
        }
        Instructor instructor = instructorRepository.findById(workoutDto.getInstructorUserDTO().getId())
                .orElseThrow(() -> {
                    F_LOG.warn("{} tried to add a workout with a non-existing instructor (id: {})", role,
                            workoutDto.getInstructorUserDTO());
                    return new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Instructor not found."
                    );
                });
        newWorkout.setName(workoutDto.getName());
        newWorkout.setTypeOfWorkout(workoutDto.getTypeOfWorkout());
        newWorkout.setLocation(workoutDto.getLocation());
        newWorkout.setInstructor(instructor);
        newWorkout.setMaxParticipants(workoutDto.getMaxParticipants());
        newWorkout.setDateTime(workoutDto.getDateTime());
        newWorkout.setEndTime(workoutDto.getDateTime().plusMinutes(workoutDto.getDurationInMinutes()));
        newWorkout.setPriceSek(
                ((workoutDto.getBasePricePerHourSek() * getMultiplier(instructor, workoutDto.getTypeOfWorkout())) /60.0 )
                        * workoutDto.getDurationInMinutes());
        newWorkout.setFreeSpots(newWorkout.getMaxParticipants());
        checkAvailability(newWorkout);
        Workout savedWorkout = workoutRepository.save(newWorkout);
        F_LOG.info("{} added a new workout with id: {}", role, savedWorkout.getId());
        return savedWorkout;
    }

    @Transactional
    @Override
    public Workout updateWorkout(WorkoutDTO newWorkout) {
        String role = authInfo.getRole();
        if (newWorkout.getId() == null) {
            F_LOG.warn("{} tried to update a workout without an id.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Workout id must be provided for update"
            );
        }
        Workout workoutToUpdate = workoutRepository.findById(newWorkout.getId()).orElseThrow(() -> {
            F_LOG.warn("{} tried to update a workout with id {} that doesn't exist.", role, newWorkout.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", newWorkout.getId())
            );
        });

        Long duration = Duration.between(workoutToUpdate.getDateTime(), workoutToUpdate.getEndTime()).toMinutes();
        Double skillMultiplier = getMultiplier(workoutToUpdate.getInstructor(), workoutToUpdate.getTypeOfWorkout());
        Double basePricePerHourSek = (workoutToUpdate.getPriceSek()*60)/(duration* skillMultiplier);

        boolean timeChanged = false;
        boolean newPriceCalc = false;
        boolean newMultiplier = false;

        List<String> parts = new ArrayList<>();

        if (newWorkout.getName() != null && !newWorkout.getName().equals(workoutToUpdate.getName())) {
            if(newWorkout.getName().isEmpty()) {
                F_LOG.warn("{} tried to update a workout with invalid name.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Name can not be left blank."
                );
            }
            workoutToUpdate.setName(newWorkout.getName());
            parts.add("name");
        }
        if(newWorkout.getTypeOfWorkout() != null && !newWorkout.getTypeOfWorkout().equals(workoutToUpdate.getTypeOfWorkout())){
            if(newWorkout.getTypeOfWorkout().isEmpty()) {
                F_LOG.warn("{} tried to update a workout with invalid typeOfWorkout.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "TypeofWorkout can not be left blank."
                );
            }
            workoutToUpdate.setTypeOfWorkout(newWorkout.getTypeOfWorkout());
            newMultiplier = true;
            parts.add("typeOfWorkout");
        }
        if(newWorkout.getLocation() != null && !newWorkout.getLocation().equals(workoutToUpdate.getLocation())) {
            if(newWorkout.getLocation().isEmpty()) {
                F_LOG.warn("{} tried to update a workout with invalid location.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Location can not be left blank."
                );
            }
            workoutToUpdate.setLocation(newWorkout.getLocation());
            parts.add("location");
        }
        if (newWorkout.getInstructorUserDTO() != null && !newWorkout.getInstructorUserDTO().getId().equals((workoutToUpdate.getInstructor().getId()))){
            Instructor instructor = instructorRepository.findById(newWorkout.getInstructorUserDTO().getId())
                    .orElseThrow(() -> {
                        F_LOG.warn("{} tried to update a workout with a non-existing instructor (id: {}).", role,
                                newWorkout.getInstructorUserDTO());
                        return new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Instructor not found."
                        );
                    });
            newMultiplier = true;
            workoutToUpdate.setInstructor(instructor);
            parts.add("instructor");
        }
        if(newWorkout.getMaxParticipants() != null && !newWorkout.getMaxParticipants().equals(workoutToUpdate.getMaxParticipants())) {
            if(newWorkout.getMaxParticipants()==0) {
                F_LOG.warn("{} tried to update a workout to 0 participants.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "A workout requires at least one participant."
                );
            } else {
                workoutToUpdate.setMaxParticipants(newWorkout.getMaxParticipants());
                parts.add("maxParticipants");
            }
        }
        if(newWorkout.getDurationInMinutes() != null && !newWorkout.getDurationInMinutes().equals(duration)) {
            if(newWorkout.getDurationInMinutes()==0) {
                F_LOG.warn("{} tried to update a workout with missing or invalid duration.", role);
                throw new ResponseStatusException(
                       HttpStatus.BAD_REQUEST,
                       "A workout requires a duration in minutes."
               );
            } else {
                timeChanged = true;
                newPriceCalc = true;
                duration = newWorkout.getDurationInMinutes();
                parts.add("duration");
            }
        }
        if(newWorkout.getDateTime() != null && !newWorkout.getDateTime().equals(workoutToUpdate.getDateTime())) {
            if (newWorkout.getDateTime().isBefore(LocalDateTime.now())) {
                F_LOG.warn("{} tried to update a workout with invalid date and time.", role);
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "A workout requires a date and time that have not already passed."
                );
            }
            workoutToUpdate.setDateTime(newWorkout.getDateTime());
            timeChanged = true;
            parts.add("dateTime");
        }
        if (timeChanged) {
            workoutToUpdate.setEndTime(workoutToUpdate.getDateTime().plusMinutes(duration));
            parts.add("endTime");
        }
        if (newWorkout.getBasePricePerHourSek() != null && !newWorkout.getBasePricePerHourSek().equals(basePricePerHourSek)){
            basePricePerHourSek = newWorkout.getBasePricePerHourSek();
            newPriceCalc = true;
            parts.add("basePricePerHourSek");
        }
        if (newMultiplier) {
            skillMultiplier = getMultiplier(workoutToUpdate.getInstructor(), workoutToUpdate.getTypeOfWorkout());
            newPriceCalc = true;
        }
        if (newPriceCalc) {
            workoutToUpdate.setPriceSek(
                    ((basePricePerHourSek * skillMultiplier) / 60.0)
                            * duration);
            parts.add("calculated price");
        }
        if(newWorkout.getCanceled() != null && newWorkout.getCanceled() != workoutToUpdate.isCanceled())  {
            if(!newWorkout.getCanceled()) {
                F_LOG.warn("{} tried to un-cancel a canceled workout, that is not allowed.", role);
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "A canceled workout can not be un-canceled."
                );
            }
            cancelWorkout(workoutToUpdate);
            workoutToUpdate.setCanceled(newWorkout.getCanceled());
            parts.add("and canceled it.");
        }
        if (parts.isEmpty()){
            F_LOG.warn("{} tried to update workout {} but no fields were changed.", role, workoutToUpdate.getId());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No fields to update. At least one field must be changed."
            );
        }
        checkAvailability(workoutToUpdate);
        String updated = String.join(", ", parts);
        F_LOG.info("{} updated workout {} in the following fields: {}.", role, workoutToUpdate, updated);
        return workoutRepository.save(workoutToUpdate);
    }


    // Logiken är: Du får bara deleta en träning om den inte har några bokningar. Bokningarna rensas troligtvis efter
    // ett specificerat tidsintervall (5 år?) och då kan de träningar som var associerade med dem också deletas.
    // Försöker man deleta en träning med bokningar blir den istället automatiskt inställd.
    @Transactional
    @Override
    public String deleteWorkout(Integer id) {
        String role = authInfo.getRole();
        Workout workout = workoutRepository.findById(id).orElseThrow(() -> {
            F_LOG.warn("{} tried to delete a workout with id {} that doesn't exist.", role, id);
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", id)
            );
        });
        List<Booking> bookings = workout.getBookings();
        if (!bookings.isEmpty()) {
            F_LOG.warn("{} tried to delete a workout, with id: {}, that has bookings associated with it. It will be canceled instead.", role, id);
            return String.format("Workout has bookings associated with it and can not be deleted. It will be canceled instead: " + cancelWorkout(workout));
        }
        workoutRepository.deleteById(id);
        F_LOG.info("{} deleted workout with id: {}", role , id);
        return String.format("Workout with Id: %s has been successfully deleted.", id);
    }


    @Transactional
    @Override
    public String cancelWorkout(Workout workout) {
        if (workout.getId() == null) {
            F_LOG.warn("{} tried to cancel a workout without an id.", authInfo.getRole());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Workout id must be provided for cancellation"
            );
        }
        if (workout.isCanceled()){
            F_LOG.info("{} tried to canceled a workout with id: {}, but it was already canceled.", authInfo.getRole(), workout.getId());
            return String.format("Workout with Id: %s is already canceled.", workout.getId());
        }
        workout.getBookings().forEach(booking -> booking.setCanceled(true));
        workout.setMaxParticipants(0);
        workout.setFreeSpots(0);
        workout.setCanceled(true);
        workoutRepository.save(workout);
        F_LOG.info("{} canceled workout with id: {}, and its associated bookings.", authInfo.getRole(), workout.getId());
        return String.format("Workout with Id: %s, and its associated bookings, were canceled.", workout.getId());
    }


    private void checkAvailability(Workout workout) {
        int bufferTimeLocation = 5;
        int bufferTimeInstructor = 10;

        boolean locationUnavailable = workoutRepository.existsByLocationAndCanceledFalseAndIdNotAndDateTimeLessThanAndEndTimeGreaterThan(
                workout.getLocation(), workout.getId() == null ? -1 : workout.getId(), workout.getEndTime().plusMinutes(bufferTimeLocation),
                workout.getDateTime().minusMinutes(bufferTimeLocation));

        if (locationUnavailable) {
            F_LOG.warn("{} tried to schedule a workout with an unavailable location.", authInfo.getRole());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The location is unavailable at that time."
            );
        }
        boolean instructorUnavailable = workoutRepository.existsByInstructorAndCanceledFalseAndIdNotAndDateTimeLessThanAndEndTimeGreaterThan(
                workout.getInstructor(), workout.getId() == null ? -1 : workout.getId(), workout.getEndTime().plusMinutes(bufferTimeInstructor),
                workout.getDateTime().minusMinutes(bufferTimeInstructor));

        if(instructorUnavailable) {
            F_LOG.warn("{} tried to schedule a workout with an unavailable instructor.", authInfo.getRole());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
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
