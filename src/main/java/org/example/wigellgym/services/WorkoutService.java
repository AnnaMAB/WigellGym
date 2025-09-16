package org.example.wigellgym.services;

import org.example.wigellgym.entities.Workout;

import java.util.List;

public interface WorkoutService {

    List<Workout> getAllWorkouts();

    Workout addWorkout(Workout workout);
    Workout updateWorkout(Workout workout);
    void deleteWorkout(Integer id);

}
