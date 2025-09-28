package org.example.wigellgym.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id", nullable = false)
    @JsonIgnoreProperties({"workouts", "secretInfo"})
    private Instructor instructor;
    @Column(nullable = false)
    private Integer maxParticipants;
    @Column(nullable = false)
    private Integer freeSpots;
    @Column(nullable = false)
    private Double priceSek;
    @Column
    private Double instructorSkillPriceMultiplier;
    @Column(nullable = false)
    private Double basePricePerHour;
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;
    @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Booking> bookings;
    @JoinColumn(nullable = false)
    private boolean cancelled = false;

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

    public Double getBasePricePerHour() {
        return basePricePerHour;
    }

    public void setBasePricePerHour(Double basePricePerHour) {
        this.basePricePerHour = basePricePerHour;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime (LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public Double getInstructorSkillPriceMultiplier() {
        return instructorSkillPriceMultiplier;
    }

    public void setInstructorSkillPriceMultiplier(Double instructorSkillPriceMultiplier) {
        this.instructorSkillPriceMultiplier = instructorSkillPriceMultiplier;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                ", instructorSkillPriceMultiplier=" + instructorSkillPriceMultiplier +
                ", basePricePerHour=" + basePricePerHour +
                ", dateTime=" + dateTime +
                ", endTime=" + endTime +
                ", bookings=" + bookings +
                ", cancelled=" + cancelled +
                '}';
    }
}
