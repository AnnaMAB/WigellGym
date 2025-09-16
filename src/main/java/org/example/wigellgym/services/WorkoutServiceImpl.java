package org.example.wigellgym.services;

import org.example.wigellgym.entities.Workout;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    @Override
    public List<Workout> getAllWorkouts() {
        return List.of();
    }

    @Override
    public Workout addWorkout(Workout workout) {
        return null;
    }

    @Override
    public Workout updateWorkout(Workout workout) {
        return null;
    }

    @Override
    public void deleteWorkout(Integer id) {

    }
}
