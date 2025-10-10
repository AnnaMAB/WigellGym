package org.example.wigellgym.mapper;

import org.example.wigellgym.dto.BookingDTO;
import org.example.wigellgym.dto.InstructorDTO;
import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Workout;
import org.springframework.stereotype.Component;

@Component
public class DtoConverter {

    public InstructorDTO makeInstructorDTO(Instructor instructor) {
        InstructorDTO dto = new InstructorDTO();
        dto.setId(instructor.getId());
        dto.setName(instructor.getName());
        dto.setSpeciality(instructor.getSpeciality());
        return dto;
    }


    public WorkoutDTO makeWorkoutDTO(Workout workout) {
        WorkoutDTO dto = new WorkoutDTO();
        dto.setId(workout.getId());
        dto.setName(workout.getName());
        dto.setTypeOfWorkout(workout.getTypeOfWorkout());
        dto.setLocation(workout.getLocation());
        dto.setInstructorDTO(makeInstructorDTO(workout.getInstructor()));
        dto.setMaxParticipants(workout.getMaxParticipants());
        dto.setDateTime(workout.getDateTime());
        dto.setEndTime(workout.getEndTime());
        dto.setCanceled(workout.isCanceled());

        return dto;
    }


    public BookingDTO makeBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setCustomerUsername(booking.getCustomerUsername());
        dto.setCanceled(booking.isCanceled());
        dto.setTotalPriceSek(booking.getTotalPriceSek());
        dto.setTotalPriceEuro(booking.getTotalPriceEuro());
        dto.setBookingDate(booking.getBookingDate());
        dto.setWorkoutDTO(makeWorkoutDTO(booking.getWorkout()));
        return dto;
    }

}
