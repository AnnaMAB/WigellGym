package org.example.wigellgym.controller;

import org.example.wigellgym.dto.InstructorView;
import org.example.wigellgym.services.InstructorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@RequestMapping("/wigellgym")
public class CommonController {

    private final InstructorServiceImpl instructorService;

    @Autowired
    public CommonController(InstructorServiceImpl instructorService) {
        this.instructorService = instructorService;

    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/instructors")
    public ResponseEntity<List<InstructorView>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getInstructors());
    }

}
