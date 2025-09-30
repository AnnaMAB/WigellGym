package org.example.wigellgym.controller;

import org.example.wigellgym.dto.InstructorView;
import org.example.wigellgym.repositories.InstructorRepository;
import org.example.wigellgym.services.InstructorServiceImpl;
import org.example.wigellgym.services.ScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/wigellgym")
public class CommonController {

    private final InstructorServiceImpl instructorService;
    private final ScheduleServiceImpl scheduleService;

    @Autowired
    public CommonController(InstructorServiceImpl instructorService, ScheduleServiceImpl scheduleService) {
        this.instructorService = instructorService;
        this.scheduleService = scheduleService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/instructors")
    public ResponseEntity<List<InstructorView>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getInstructors());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/schedule")
    public ResponseEntity<Map<LocalDateTime, LocalDateTime>> getSchedule() {
        return ResponseEntity.ok(scheduleService.getSchedule());
    }

}
