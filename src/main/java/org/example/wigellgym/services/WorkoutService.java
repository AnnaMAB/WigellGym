package org.example.wigellgym.services;

import org.example.wigellgym.entities.Workout;

import java.util.Map;
import java.util.Set;

public interface WorkoutService {

    Map<String, Set<String>> getAllWorkouts();

    Workout addWorkout(Workout workout);
    Workout updateWorkout(Workout workout);
    void deleteWorkout(Integer id);

}
