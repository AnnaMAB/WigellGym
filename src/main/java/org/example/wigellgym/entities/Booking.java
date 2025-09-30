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
    @JoinColumn(nullable = false)
    private boolean canceled;
    @Column(length = 40, nullable = false)
    private String customerUsername;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "workout_id", nullable = false)
    @JsonIgnoreProperties({"priceSEK", "maxParticipants", "freeSpots"})
    private Workout workout;
    @Column(length = 40, nullable = false)
    private Double totalPriceSek;
    @Column(length = 40, nullable = false)
    private Double totalPriceEuro;
    @Column(length = 40, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime bookingDate;


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

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", bookingDate=" + bookingDate +
                ", customerUsername='" + customerUsername + '\'' +
                ", workout=" + workout +
                ", totalPriceSek=" + totalPriceSek +
                ", totalPriceEuro=" + totalPriceEuro +
                ", canceled=" + canceled +
                '}';
    }
}
