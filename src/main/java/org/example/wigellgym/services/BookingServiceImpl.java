package org.example.wigellgym.services;

import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<Booking> getMyBookings() {
        return List.of();
    }

    @Override
    public Booking makeBooking(Booking booking) {
        return null;
    }

    @Override
    public Booking cancelBooking(Booking booking) {
        return null;
    }

    @Override
    public List<Booking> getCanceledBookings() {
        return List.of();
    }

    @Override
    public List<Booking> getUpcomingBookings() {
        return List.of();
    }

    @Override
    public List<Booking> getOldBookings() {
        return List.of();
    }
}
