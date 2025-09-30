package org.example.wigellgym.services;

import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Workout;

import java.util.Map;
import java.util.Set;

public interface WorkoutService {

    Map<String, Set<String>> getAllWorkouts();

    Workout addWorkout(WorkoutDTO workoutDto);
    Workout updateWorkout(WorkoutDTO newWorkout);
    String deleteWorkout(Integer id);
    String cancelWorkout(Workout workout);
}
