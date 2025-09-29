package org.example.wigellgym.repositories;

import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByCustomerUsername(String customerUsername);
    List<Booking> findByCanceledTrue();
    List<Booking> findByCanceledFalseAndWorkoutDateGreaterThanEqual(LocalDateTime now);
    List<Booking> findByCanceledTrueOrWorkoutDateBefore(LocalDateTime now);

    boolean existsByWorkoutAndCustomerUsername(Workout workout, String authUsername);
}
