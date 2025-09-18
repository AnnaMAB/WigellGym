package org.example.wigellgym.repositories;

import org.example.wigellgym.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByCustomerUsername(String customerUsername);
    List<Booking> findByCancelledTrue();
    List<Booking> findByCancelledFalseAndWorkoutDateGreaterThanEqual(LocalDate now);
    List<Booking> findByCancelledTrueOrWorkoutDateBefore(LocalDate now);
}
