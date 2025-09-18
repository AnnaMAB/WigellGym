package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, WorkoutRepository workoutRepository) {
        this.bookingRepository = bookingRepository;
        this.workoutRepository = workoutRepository;
    }

    private String getAuthUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public List<Booking> getMyBookings() {          //KLAR?
        F_LOG.info("USER retrieved all their bookings");
        return bookingRepository.findAllByCustomerUsername((getAuthUsername()));
    }

    @Transactional
    @Override                               //TODO----EURO//TODO-----LOG
    public Booking makeBooking(Workout workoutToBook) {
        Workout workout = workoutRepository.findById(workoutToBook.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No workout exists with id: %d.", workoutToBook.getId())
                ));
        if(workout.getFreeSpots() == 0) {
            F_LOG.warn("USER tried to book a workout with no free spots");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("No free spots at the requested workout")
            );
        }
        Booking booking = new Booking();
        booking.setCustomerUsername(getAuthUsername());
        booking.setBookingDate(LocalDate.now());
        booking.setWorkoutDate(workout.getDate());
        booking.setWorkout(workout);
        booking.setTotalPrice(workout.getPriceSek());
        booking.setCancelled(false);
        Booking savedBooking = bookingRepository.save(booking);
        workout.setFreeSpots(workout.getFreeSpots() - 1);
        workout.getBookings().add(savedBooking);
        workoutRepository.save(workout);
        return savedBooking;
    }

    @Transactional
    @Override                               //KLAR?//TODO-----LOG
    public Booking cancelBooking(Booking bookingToCancel) {
        Booking booking = bookingRepository.findById(bookingToCancel.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("No booking exists with id: %d.", bookingToCancel.getId())
                ));
        if(!getAuthUsername().equals(booking.getCustomerUsername())) {
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
        F_LOG.info("ADMIN retrieved all canceled bookings");
        return bookingRepository.findByCancelledTrue();
    }

    @Override                                       //KLAR?
    public List<Booking> getUpcomingBookings() {
        F_LOG.info("ADMIN retrieved all upcoming bookings");
        return bookingRepository.findByCancelledFalseAndWorkoutDateGreaterThanEqual(LocalDate.now());
    }

    @Override                                        //KLAR?
    public List<Booking> getOldBookings() {
        F_LOG.info("ADMIN retrieved all previous bookings");
        return bookingRepository.findByCancelledTrueOrWorkoutDateBefore(LocalDate.now());
    }
}
