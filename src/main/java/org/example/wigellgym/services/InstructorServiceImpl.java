package org.example.wigellgym.services;

import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override                              //Klar
    public List<Instructor> getInstructors() {
        return instructorRepository.findAll();
    }

    @Override                               //Klar
    public Instructor addInstructor(Instructor instructor) {
        if(instructor.getName() == null|| instructor.getName().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Name required")
            );
        }
        if (instructor.getSpeciality() == null|| instructor.getName().isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Speciality required")
            );
        }
        return instructorRepository.save(instructor);
    }
}
