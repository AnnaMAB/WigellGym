package org.example.wigellgym.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private static final Logger F_LOG = LogManager.getLogger("functionality");

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override                              //Klar
    public List<Instructor> getInstructors() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElse("NO_ROLE");
        F_LOG.info("{} displayed the list of all instructors", role);
        return instructorRepository.findAll();
    }

    @Override                               //Klar
    public Instructor addInstructor(Instructor instructor) {
        if(instructor.getName() == null|| instructor.getName().isEmpty()) {
            F_LOG.warn("ADMIN tried to add an instructor with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Name required")
            );
        }
        if (instructor.getSpeciality() == null){
            F_LOG.warn("ADMIN tried to add an instructor with missing or invalid fields");
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Speciality required")
            );
        }
        Instructor savedInstructor = instructorRepository.save(instructor);
        F_LOG.info("ADMIN added an instructor with id {}", savedInstructor.getId());
        return savedInstructor;
    }
}
