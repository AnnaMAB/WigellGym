package org.example.wigellgym.entities;

/*--Bokningar ska inkludera kund, träningspass, datum och totalpris (SEK och Euro). */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;
    @Column(length = 40, nullable = false)
    private LocalDate bookingDate;    @Column(length = 40, nullable = false)
    private LocalDate workoutDate;
    @Column(length = 40, nullable = false)
    private String customerUsername;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonIgnoreProperties({"bookings"})
    private Workout workout;
    @Column(length = 40, nullable = false)
    private Double totalPrice;
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

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(LocalDate workoutDate) {
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
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
                ", totalPrice=" + totalPrice +
                ", cancelled=" + cancelled +
                '}';
    }
}
