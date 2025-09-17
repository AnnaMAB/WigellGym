package org.example.wigellgym.services;

import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;

import java.util.List;

public interface BookingService {

    List<Booking> getMyBookings();
    Booking makeBooking(Workout workoutToBook);
    Booking cancelBooking(Booking booking);

    List<Booking> getCanceledBookings();
    List<Booking> getUpcomingBookings();
    List<Booking> getOldBookings();

}
