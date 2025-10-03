package org.example.wigellgym.services;

import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.repositories.InstructorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceImplTest {


    @Mock
    private InstructorRepository instructorRepositoryMock;
    @Mock
    private AuthInfo authInfoMock;
    @Mock
    private Logger loggerMock;

    private Instructor instructor;

    @InjectMocks
    private InstructorServiceImpl instructorService;


    @BeforeEach
    void setUp() {
        instructor = new Instructor();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstructors() {
    }

    @Test
    void addInstructor_ShouldSaveAndReturnInstructor (Instructor instructor) {
    }
    @Test
    void addInstructor_ShouldTrowExceptionWhenNameIsNull (Instructor instructor) {
    }

    @Test
    void addInstructor_ShouldTrowExceptionWhenNameIsNull () {
    }
}