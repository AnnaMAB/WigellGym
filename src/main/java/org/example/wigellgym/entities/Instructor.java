package org.example.wigellgym.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Integer id;
    @Column(length = 12, nullable = false)
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"instructor", "priceSEK", "typeOfWorkout","participants", "bookings"})
    private List<Workout> workouts;
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Speciality> speciality = new ArrayList<>();


    public Instructor() {

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

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workouts=" + workouts +
                ", speciality=" + speciality +
                '}';
    }
}
