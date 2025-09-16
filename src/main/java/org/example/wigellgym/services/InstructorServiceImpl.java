package org.example.wigellgym.services;

import org.example.wigellgym.entities.Instructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {


    @Override
    public List<Instructor> getInstructors() {
        return List.of();
    }

    @Override
    public Instructor addInstructor(Instructor instructor) {
        return null;
    }
}
