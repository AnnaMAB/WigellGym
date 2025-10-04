package org.example.wigellgym.services;

import org.apache.logging.log4j.Logger;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.dto.InstructorDTO;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Speciality;
import org.example.wigellgym.repositories.InstructorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class InstructorServiceImplTest {


    @Mock
    private InstructorRepository instructorRepositoryMock;
    @Mock
    private AuthInfo authInfoMock;

    private List<Instructor> instructors;

    private Instructor instructor;

    @InjectMocks
    private InstructorServiceImpl instructorService;


    @BeforeEach
    void setUp() {
        Instructor kalle = new Instructor("Kalle", "är en kanin");
        kalle.setId(1);

        Speciality yoga = new Speciality();
        yoga.setType("Yoga");
        yoga.setCertificateLevel(4);

        kalle.getSpeciality().add(yoga);


        Instructor puh = new Instructor("Puh", "gillar honung");
        puh.setId(2);

        Speciality cardio = new Speciality();
        cardio.setType("Cardio");
        cardio.setCertificateLevel(2);

        puh.getSpeciality().add(cardio);

        instructors.add(kalle);
        instructors.add(puh);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstructors_IfAdminShouldReturnAllInstructors() {
        // Given

        // When

        // Then
    }

    @Test
    void getInstructors_IfUserShouldReturnAllInstructorDTOs() {
    }

    @Test
    void addInstructor_ShouldSaveAndReturnInstructor (Instructor instructor) {
    }

    @Test
    void addInstructor_ShouldAddSpecialityListEvenIfNotExplicitlyAdded(Instructor instructor) {
    }

    @Test
    void addInstructor_ShouldTrowExceptionWhenNameIsNull (Instructor instructor) {
    }

    @Test
    void makeInstructorDTO_ShouldCopyInstructorAndReturnInstructorDTO (Instructor instructor) {
    }


}