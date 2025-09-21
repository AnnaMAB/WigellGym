package org.example.wigellgym.dto;

import org.example.wigellgym.entities.Speciality;

import java.util.ArrayList;
import java.util.List;

public class InstructorUserDTO implements InstructorView{
    private String name;
    private List<Speciality> speciality = new ArrayList<>();

    public InstructorUserDTO() {

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
}
