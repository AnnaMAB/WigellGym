package org.example.wigellgym.services;

import org.example.wigellgym.dto.InstructorView;
import org.example.wigellgym.entities.Instructor;

import java.util.List;

public interface InstructorService {

    List<InstructorView> getInstructors();
    Instructor addInstructor(Instructor instructor);

}
