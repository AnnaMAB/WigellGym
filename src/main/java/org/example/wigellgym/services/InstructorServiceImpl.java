package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.dto.InstructorUserDTO;
import org.example.wigellgym.dto.InstructorView;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
    public List<InstructorView> getInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        List<InstructorView> instructorViews = new ArrayList<>();
        String role = authInfo.getRole();

        if ("ADMIN".equals(role)) {
            instructorViews.addAll(instructors);
            F_LOG.info("ADMIN displayed the list of all instructors");
        } else {
            for (Instructor instructor : instructors) {
                InstructorUserDTO dto = new InstructorUserDTO();
                dto.setName(instructor.getName());
                dto.setSpeciality(instructor.getSpeciality());
                instructorViews.add(dto);
            }
            F_LOG.info("USER displayed the redacted list of all instructors");
        }
        return instructorViews;
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
