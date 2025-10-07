package org.example.wigellgym.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.example.wigellgym.dto.InstructorView;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Instructor implements InstructorView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Integer id;
    @Column(length = 12, nullable = false)
    private String name;
    @Column(length = 42)
    private String secretInfo;
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"instructor", "priceSEK", "preliminaryPriceEuro", "typeOfWorkout","maxParticipants", "bookings", "freeSpots"})
    private List<Workout> workouts = new ArrayList<>();
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Speciality> speciality = new ArrayList<>();


    public Instructor() {

    }

    public Instructor(String name, String secretInfo) {
        this.name = name;
        this.secretInfo = secretInfo;
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

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }

    public List<Speciality> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(List<Speciality> speciality) {
        this.speciality = speciality;
    }

    public String getSecretInfo() {
        return secretInfo;
    }

    public void setSecretInfo(String secretInfo) {
        this.secretInfo = secretInfo;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", secretInfo='" + secretInfo + '\'' +
                ", workouts=" + workouts +
                ", speciality=" + speciality +
                '}';
    }
}
