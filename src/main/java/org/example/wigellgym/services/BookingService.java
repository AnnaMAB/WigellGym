package org.example.wigellgym.services;

import org.example.wigellgym.dto.BookingDTO;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;

import java.util.List;

public interface BookingService {

    List<BookingDTO> getMyBookings();
    BookingDTO makeBooking(Workout workoutToBook);
    BookingDTO cancelBooking(Booking booking);

    List<BookingDTO> getCanceledBookings();
    List<BookingDTO> getUpcomingBookings();
    List<BookingDTO> getOldBookings();

}
