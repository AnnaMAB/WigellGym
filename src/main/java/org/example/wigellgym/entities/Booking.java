package org.example.wigellgym.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "GYM_BOOKING")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;
    @Column(length = 40, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;
    @Column(length = 40, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime workoutDate;
    @Column(length = 40, nullable = false)
    private String customerUsername;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonIgnoreProperties({"id", "priceSEK", "preliminaryPriceEuro", "maxParticipants", "freeSpots", "date"})
    private Workout workout;
    @Column(length = 40, nullable = false)
    private Double totalPriceSek;
    @Column(length = 40, nullable = false)
    private Double totalPriceEuro;
    @JoinColumn(nullable = false)
    private boolean cancelled;

    public Booking() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDateTime getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(LocalDateTime workoutDate) {
        this.workoutDate = workoutDate;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingDate=" + bookingDate +
                ", workoutDate=" + workoutDate +
                ", customerUsername='" + customerUsername + '\'' +
                ", workout=" + workout +
                ", totalPriceSek=" + totalPriceSek +
                ", totalPriceEuro=" + totalPriceEuro +
                ", cancelled=" + cancelled +
                '}';
    }
}
