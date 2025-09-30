package org.example.wigellgym.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.wigellgym.repositories.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class ScheduleServiceImpl implements ScheduleService {


    private static final Logger F_LOG = LogManager.getLogger("functionality");
    private InstructorRepository instructorRepository;

    @Autowired
    public ScheduleServiceImpl(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;

    }

    public Map getSchedule(){
        Map<LocalDateTime, LocalDateTime> instructorBooked = new HashMap();
//        instructorRepository.findAll().forEach(instructor -> {})

        return instructorBooked;
    }




}
