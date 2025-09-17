package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.repositories.BookingRepository;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkoutRepository workoutRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, WorkoutRepository workoutRepository) {
        this.bookingRepository = bookingRepository;
        this.workoutRepository = workoutRepository;
    }

    private String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public List<Booking> getMyBookings() {          //KLAR?
        return bookingRepository.findAllByCustomerUsername((getUsername()));
    }

    @Transactional
    @Override                               //TODO----EURO
    public Booking makeBooking(Workout workoutToBook) {
        Workout workout = workoutRepository.findById(workoutToBook.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No workout exists with id: %d.", workoutToBook.getId())
                ));
        if(workout.getFreeSpots() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("No free spots at the requested workout")
            );
        }
        Booking booking = new Booking();
        booking.setCustomerUsername(getUsername());
        booking.setBookingDate(LocalDate.now());
        booking.setWorkoutDate(workout.getDate());
        booking.setWorkout(workout);
        booking.setTotalPrice(workout.getPriceSek());
        booking.setCancelled(false);
        workout.setFreeSpots(workout.getFreeSpots() - 1);
        workoutRepository.save(workout);
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override                               //KLAR?
    public Booking cancelBooking(Booking bookingToCancel) {
        Booking booking = bookingRepository.findById(bookingToCancel.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No booking exists with id: %d.", bookingToCancel.getId())
                ));
        if(!getUsername().equals(booking.getCustomerUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    String.format("You do not have permission to access this page.")
            );
        }
        if(booking.getBookingDate().isBefore(LocalDate.now().minusDays(1))) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    String.format("The workout date is to close for cancellation.")
            );
        }
        if(booking.isCancelled()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    String.format("The workout is already canceled.")
            );
        }
        booking.setCancelled(true);
        return bookingRepository.save(booking);
    }

    @Override                                      //KLAR?
    public List<Booking> getCanceledBookings() {
        return bookingRepository.findByCancelledTrue();
    }

    @Override                                       //KLAR?
    public List<Booking> getUpcomingBookings() {
        return bookingRepository.findByWorkoutDateAfter(LocalDate.now());
    }

    @Override                                        //KLAR?
    public List<Booking> getOldBookings() {
        return bookingRepository.findByWorkoutDateGreaterThanEqual(LocalDate.now());
    }
}
