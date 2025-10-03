package org.example.wigellgym.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

public class BookingDTO {

    private Integer id;
    private boolean canceled;
    private String customerUsername;
    @JsonIgnoreProperties({"priceSEK", "maxParticipants", "freeSpots", "basePricePerHourSek", "durationInMinutes"})
    private WorkoutDTO workoutDTO;
    private Double totalPriceSek;
    private Double totalPriceEuro;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;

    public BookingDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public WorkoutDTO getWorkoutDTO() {
        return workoutDTO;
    }

    public void setWorkoutDTO(WorkoutDTO workoutDTO) {
        this.workoutDTO = workoutDTO;
    }

    public Double getTotalPriceSek() {
        return totalPriceSek;
    }

    public void setTotalPriceSek(Double totalPriceSek) {
        this.totalPriceSek = totalPriceSek;
    }

    public Double getTotalPriceEuro() {
        return totalPriceEuro;
    }

    public void setTotalPriceEuro(Double totalPriceEuro) {
        this.totalPriceEuro = totalPriceEuro;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
                "id=" + id +
                ", canceled=" + canceled +
                ", customerUsername='" + customerUsername + '\'' +
                ", workoutDTO=" + workoutDTO +
                ", totalPriceSek=" + totalPriceSek +
                ", totalPriceEuro=" + totalPriceEuro +
                ", bookingDate=" + bookingDate +
                '}';
    }
}
