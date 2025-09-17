package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    @Override                               //TODO----ALLA???
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    @Override                               //TODO--DOING--Lägg till träningspass
    public Workout addWorkout(Workout workout) {
/*              ", name='" + name + '\'' +
                ", typeOfWorkout='" + typeOfWorkout + '\'' +
                ", location='" + location + '\'' +
                ", instructor=" + instructor +



                ", maxParticipants=" + maxParticipants +
                ", freeSpots=" + freeSpots +
                ", priceSek=" + priceSek +
                ", date=" + date +
                ", bookings=" + bookings +
                '}';*/
        if(workout.getName() == null|| workout.getName().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Name required")
            );
        }
        if(workout.getTypeOfWorkout() == null|| workout.getTypeOfWorkout().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Type of workout required")
            );
        }
        if(workout.getLocation() == null|| workout.getLocation().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Location required")
            );
        }
        if (workout.getInstructor() == null){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Instructor required")
            );
        }

        return workoutRepository.save(workout);
    }

    @Override                               //TODO---Uppdatera träningspass:
    public Workout updateWorkout(Workout workout) {

        return workoutRepository.save(workout);
    }

    @Transactional
    @Override                                   //KLAR?
    public void deleteWorkout(Integer id) {
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No workout exists with id: %d.", id)
                ));
        workoutRepository.deleteById(id);
    }
}
