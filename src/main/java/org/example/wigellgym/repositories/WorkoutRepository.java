package org.example.wigellgym.repositories;


import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Set;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Integer> {

    @Query("SELECT w.name FROM Workout w WHERE w.typeOfWorkout = :type")
    Set<String> findNamesByTypeOfWorkout(String type);

    @Query("SELECT DISTINCT w.typeOfWorkout FROM Workout w")
    Set<String> findTypeOfWorkout();

    boolean existsByInstructorAndCanceledFalseAndDateTimeLessThanAndEndTimeGreaterThan(
            Instructor instructor, LocalDateTime endTime, LocalDateTime startTime
    );

    boolean existsByLocationAndCanceledFalseAndDateTimeLessThanAndEndTimeGreaterThan(
            String location, LocalDateTime endTime, LocalDateTime startTime
    );
}