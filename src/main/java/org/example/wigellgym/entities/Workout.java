package org.example.wigellgym.entities;

/*--Träningspass ska ha namn, typ (t.ex. yoga, styrke), max antal deltagare, pris och instruktör. */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_id")
    private Integer id;
    @Column(length = 40, nullable = false)
    private String name;
    @Column(length = 40, nullable = false)
    private String typeOfWorkout;
    @Column(length = 40, nullable = false)
    private String location;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id", nullable = false)
    @JsonIgnoreProperties({"workouts", "speciality"})
    private Instructor instructor;
    @Column(nullable = false)
    private Integer maxParticipants;
    @Column(nullable = false)
    private Integer freeSpots;
    @Column(nullable = false)
    private Double priceSek;
    @Column(nullable = false)
    private Double preliminaryPriceEuro;
    @Column(nullable = false)
    private LocalDate date;
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"date", "totalPrice", "workout"})
    private List<Booking> bookings;

    public Workout() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeOfWorkout() {
        return typeOfWorkout;
    }

    public void setTypeOfWorkout(String typeOfWorkout) {
        this.typeOfWorkout = typeOfWorkout;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(Integer freeSpots) {
        this.freeSpots = freeSpots;
    }

    public Double getPriceSek() {
        return priceSek;
    }

    public void setPriceSek(Double priceSEK) {
        this.priceSek = priceSEK;
    }

    public Double getPreliminaryPriceEuro() {
        return preliminaryPriceEuro;
    }

    public void setPreliminaryPriceEuro(Double preliminaryPriceEuro) {
        this.preliminaryPriceEuro = preliminaryPriceEuro;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", typeOfWorkout='" + typeOfWorkout + '\'' +
                ", location='" + location + '\'' +
                ", instructor=" + instructor +
                ", maxParticipants=" + maxParticipants +
                ", freeSpots=" + freeSpots +
                ", priceSek=" + priceSek +
                ", preliminaryPriceEuro=" + preliminaryPriceEuro +
                ", date=" + date +
                ", bookings=" + bookings +
                '}';
    }
}
