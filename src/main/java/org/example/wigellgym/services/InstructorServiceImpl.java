package org.example.wigellgym.services;

import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.dto.InstructorView;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.mapper.DtoConverter;
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
    private final DtoConverter dtoConverter;

    @Autowired
    public InstructorServiceImpl(InstructorRepository instructorRepository, AuthInfo authInfo, DtoConverter dtoConverter) {
        this.instructorRepository = instructorRepository;
        this.authInfo = authInfo;
        this.dtoConverter = dtoConverter;
    }

    @Override
    public List<InstructorView> getInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        List<InstructorView> instructorViews = new ArrayList<>();
        String role = authInfo.getRole();

        if ("ADMIN".equals(role)) {
            instructorViews.addAll(instructors);
            F_LOG.info("{} displayed the list of all instructors.", role);
        } else {
            for (Instructor instructor : instructors) {
                instructorViews.add(dtoConverter.makeInstructorDTO(instructor));
            }
            F_LOG.info("{} displayed the redacted list of all instructors.", role);
        }
        return instructorViews;
    }

    @Transactional
    @Override
    public Instructor addInstructor(Instructor instructor) {
        String role = authInfo.getRole();
        if(instructor.getName() == null|| instructor.getName().isEmpty()) {
            F_LOG.warn("{} tried to add an instructor with missing or invalid fields.", role);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Name required"
            );
        }
        if (!instructor.getSpeciality().isEmpty()) {            //En tom Speciality-lista skapas alltid
            instructor.getSpeciality().forEach(s -> s.setInstructor(instructor));
        }
        Instructor savedInstructor = instructorRepository.save(instructor);
        F_LOG.info("{} added an instructor with id {}.", role, savedInstructor.getId());
        return savedInstructor;
    }

}
