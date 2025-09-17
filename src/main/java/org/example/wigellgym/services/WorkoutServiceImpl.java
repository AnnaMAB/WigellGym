package org.example.wigellgym.services;

import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Autowired
    public WorkoutServiceImpl(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

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

        return workoutRepository.save(workout);
    }

    @Override
    public void deleteWorkout(Integer id) {
        workoutRepository.deleteById(id);
    }
}
