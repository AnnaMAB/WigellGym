package org.example.wigellgym.services;

import org.example.wigellgym.dto.BookingDTO;
import org.example.wigellgym.dto.WorkoutDTO;

import java.util.List;

public interface BookingService {

    List<BookingDTO> getMyBookings();
    BookingDTO makeBooking(WorkoutDTO workoutToBook);
    BookingDTO cancelBooking(BookingDTO booking);

    List<BookingDTO> getCanceledBookings();
    List<BookingDTO> getUpcomingBookings();
    List<BookingDTO> getOldBookings();

}
