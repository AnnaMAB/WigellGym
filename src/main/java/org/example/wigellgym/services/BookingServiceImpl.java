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

import java.time.LocalDateTime;
import java.util.List;

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
    public List<Booking> getMyBookings() {
        F_LOG.info("USER retrieved all their bookings.");
        return bookingRepository.findAllByCustomerUsername((authInfo.getAuthUsername()));
    }

    @Transactional
    @Override
    public Booking makeBooking(Workout workoutToBook) {
        Workout workout = workoutRepository.findById(workoutToBook.getId()).orElseThrow(() -> {
            F_LOG.warn("USER tried to book a workout with id {} that doesn't exist.", workoutToBook.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No workout exists with id: %d.", workoutToBook.getId())
            );
        });
        boolean alreadyBooked = bookingRepository.existsByWorkoutAndCustomerUsernameAndCanceledFalse(workout, authInfo.getAuthUsername());
        if (alreadyBooked) {
            F_LOG.warn("USER tried to book a workout that they already have a booking for.");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "You have already booked the requested workout."
            );
        }
        if (workout.getDateTime().isBefore(LocalDateTime.now().plusHours(1))) {
            F_LOG.warn("USER tried to book a workout that has already happened or is too close to workout.");
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Too late to book workout."
            );
        }
        if(workout.getFreeSpots() == 0) {
            F_LOG.warn("USER tried to book a workout with no free spots.");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No free spots at the requested workout."
            );
        }
        double euroRate;
        try {
            euroRate = conversionService.getConversionRate();
        } catch (IllegalStateException e) {
            F_LOG.warn("USER tried to book a workout. Unable to reach Euro conversion API. Booking not made.");
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Unable to reach Euro conversion API. Booking cannot be made."
            );
        }
        Booking booking = new Booking();
        booking.setCustomerUsername(authInfo.getAuthUsername());
        booking.setBookingDate(LocalDateTime.now());
        booking.setWorkout(workout);
        booking.setTotalPriceSek(workout.getPriceSek());
        booking.setTotalPriceEuro(workout.getPriceSek()*euroRate);
        booking.setCanceled(false);
        Booking savedBooking = bookingRepository.save(booking);
        workout.setFreeSpots(workout.getFreeSpots() - 1);
        workout.getBookings().add(savedBooking);
        workoutRepository.save(workout);
        F_LOG.info("USER made a booking, with id {}, for workout with id {}.", savedBooking.getId(), workout.getId());
        return savedBooking;
    }

    @Transactional
    @Override
    public Booking cancelBooking(Booking bookingToCancel) {
        Booking booking = bookingRepository.findById(bookingToCancel.getId()).orElseThrow(() -> {
            F_LOG.warn("USER tried to cancel a booking with id {} that doesn't exist.", bookingToCancel.getId());
            return new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("No booking exists with id: %d.", bookingToCancel.getId())
            );
        });
        if(!authInfo.getAuthUsername().equals(booking.getCustomerUsername())) {
            F_LOG.warn("USER tried to cancel a booking, with id {}, that they are not the customer for.", booking.getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You do not have permission to access this booking."
            );
        }
        if(booking.isCanceled()) {
            F_LOG.warn("USER tried to cancel a booking, with id {}, that is already canceled.", booking.getId());
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "The workout is already canceled."
            );
        }
        if(booking.getWorkout().getDateTime().isBefore(LocalDateTime.now().plusDays(1))) {
            F_LOG.warn("USER tried to cancel a booking, with id {}, that has already passed or is to close to the workout date.", booking.getId());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "The workout date is to close for cancellation."
            );
        }
        booking.setCanceled(true);
        booking.setTotalPriceEuro(0.0);
        booking.setTotalPriceSek(0.0);
        F_LOG.info("USER canceled booking with id {} for workout {}.", booking.getId(), booking.getWorkout().getId());
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getCanceledBookings() {
        F_LOG.info("ADMIN retrieved all canceled bookings.");
        return bookingRepository.findByCanceledTrue();
    }

    @Override
    public List<Booking> getUpcomingBookings() {
        F_LOG.info("ADMIN retrieved all upcoming bookings.");
        return bookingRepository.findByCanceledFalseAndWorkout_DateTimeGreaterThanEqual(LocalDateTime.now());
    }

    @Override
    public List<Booking> getOldBookings() {
        F_LOG.info("ADMIN retrieved all previous bookings.");
        return bookingRepository.findByCanceledTrueOrWorkout_DateTimeBefore(LocalDateTime.now());
    }
}
