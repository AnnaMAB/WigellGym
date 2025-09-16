package org.example.wigellgym.services;

import org.example.wigellgym.entities.Booking;

import java.util.List;

public interface BookingService {

    List<Booking> getMyBookings();
    Booking makeBooking(Booking booking);
    Booking cancelBooking(Booking booking);

    List<Booking> getCanceledBookings();
    List<Booking> getUpcomingBookings();
    List<Booking> getOldBookings();

}
