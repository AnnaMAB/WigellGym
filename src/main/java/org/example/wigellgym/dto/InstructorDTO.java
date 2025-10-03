package org.example.wigellgym.dto;

import org.example.wigellgym.entities.Speciality;

import java.util.ArrayList;
import java.util.List;

public class InstructorDTO implements InstructorView{

    private Integer id;
    private String name;
    private List<Speciality> speciality = new ArrayList<>();

    public InstructorDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Speciality> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(List<Speciality> speciality) {
        this.speciality = speciality;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "InstructorDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", speciality=" + speciality +
                '}';
    }
}
