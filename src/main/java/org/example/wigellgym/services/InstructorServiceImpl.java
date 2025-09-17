package org.example.wigellgym.services;

import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override                                                   //Lista instruktörer
    public List<Instructor> getInstructors() {
        return instructorRepository.findAll();
    }

    @Override                                                   // Lägg till instruktör
    public Instructor addInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }
}
