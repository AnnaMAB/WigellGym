package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.repositories.BookingRepository;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final WorkoutRepository workoutRepository;
    private final ConversionServiceImpl conversionService;
    private final AuthInfo authInfo;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, WorkoutRepository workoutRepository, ConversionServiceImpl conversionService, AuthInfo authInfo) {
        this.conversionService = conversionService;
        this.bookingRepository = bookingRepository;
        this.workoutRepository = workoutRepository;
        this.authInfo = authInfo;
    }


    @Override
    public List<Booking> getMyBookings() {          //KLAR?
        F_LOG.info("USER retrieved all their bookings");
        return bookingRepository.findAllByCustomerUsername((authInfo.getAuthUsername()));
    }

    @Transactional
    @Override
    public Booking makeBooking(Workout workoutToBook) {
        Optional<Workout> optionalWorkout = workoutRepository.findById(workoutToBook.getId());
        Workout workout = optionalWorkout.orElseThrow(() -> {
            F_LOG.warn("USER tried to book a workout with id {} that doesn't exist.", workoutToBook.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", workoutToBook.getId())
            );
        });
        if(workout.getFreeSpots() == 0) {
            F_LOG.warn("USER tried to book a workout with no free spots");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No free spots at the requested workout"
            );
        }
        Booking booking = new Booking();
        booking.setCustomerUsername(authInfo.getAuthUsername());
        booking.setBookingDate(LocalDate.now());
        booking.setWorkoutDate(workout.getDate());
        booking.setWorkout(workout);
        booking.setTotalPriceSek(workout.getPriceSek());
        booking.setTotalPriceEuro(workout.getPriceSek()*conversionService.getConversionRate());
        booking.setCancelled(false);
        Booking savedBooking = bookingRepository.save(booking);
        workout.setFreeSpots(workout.getFreeSpots() - 1);
        workout.getBookings().add(savedBooking);
        workoutRepository.save(workout);
        F_LOG.info("USER made a booking, with id {}, for workout with id {}.", savedBooking.getId(), workout.getId());
        return savedBooking;
    }

    @Transactional
    @Override                               //KLAR?
    public Booking cancelBooking(Booking bookingToCancel) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingToCancel.getId());
        Booking booking = optionalBooking.orElseThrow(() -> {
            F_LOG.warn("USER tried to cancel a booking with id {} that doesn't exist.", bookingToCancel.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", bookingToCancel.getId())
            );
        });
        if(!authInfo.getAuthUsername().equals(booking.getCustomerUsername())) {
            F_LOG.warn("USER tried to cancel a booking, with id {}, that they are not the customer for.", booking.getId());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You do not have permission to access this page."
            );
        }
        if(booking.getBookingDate().isBefore(LocalDate.now().minusDays(1))) {
            F_LOG.warn("USER tried to cancel a booking, with id {}, to close to the workout date.", booking.getId());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "The workout date is to close for cancellation."
            );
        }
        if(booking.isCancelled()) {
            F_LOG.warn("USER tried to cancel a booking, with id {}, that is already canceled.", booking.getId());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "The workout is already canceled."
            );
        }
        booking.setCancelled(true);
        booking.setTotalPriceEuro(0.0);
        booking.setTotalPriceSek(0.0);
        F_LOG.info("USER canceled booking with id {} for workout {}.", booking.getId(), booking.getWorkout().getId());
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
