package org.example.wigellgym.services;

import org.example.wigellgym.entities.Instructor;

import java.util.List;

public interface InstructorService {

    List<Instructor> getInstructors();
    Instructor addInstructor(Instructor instructor);

}
