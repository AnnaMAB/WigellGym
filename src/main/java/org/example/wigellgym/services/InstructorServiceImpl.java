package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
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
    private final AuthInfo authInfo;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository, AuthInfo authInfo) {
        this.instructorRepository = instructorRepository;
        this.authInfo = authInfo;
    }

    @Override                              //Klar
    public List<Instructor> getInstructors() {
        F_LOG.info("{} displayed the list of all instructors", authInfo.getRole());
        return instructorRepository.findAll();
    }

    @Transactional
    @Override                               //Klar
    public Instructor addInstructor(Instructor instructor) {
        if(instructor.getName() == null|| instructor.getName().isEmpty()) {
            F_LOG.warn("ADMIN tried to add an instructor with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name required"
            );
        }
        if (instructor.getSpeciality() == null){
            F_LOG.warn("ADMIN tried to add an instructor with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Speciality required"
            );
        }
        Instructor savedInstructor = instructorRepository.save(instructor);
        F_LOG.info("ADMIN added an instructor with id {}", savedInstructor.getId());
        return savedInstructor;
    }
}
