package org.example.wigellgym.services;

import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.dto.InstructorDTO;
import org.example.wigellgym.dto.InstructorView;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InstructorServiceImplTest {


    @Mock
    private InstructorRepository instructorRepositoryMock;
    @Mock
    private AuthInfo authInfoMock;

    private List<Instructor> instructors = new ArrayList<>();

    @InjectMocks
    private InstructorServiceImpl instructorService;


    @BeforeEach
    void setUp() {
        Instructor kalle = new Instructor("Kalle", "är en kanin");
        kalle.setId(1);
        Speciality hoppning = new Speciality();
        hoppning.setType("Hoppning");
        hoppning.setCertificateLevel(5);
        kalle.getSpeciality().add(hoppning);

        Instructor puh = new Instructor("Puh", "gillar honung");
        puh.setId(2);
        Speciality yoga = new Speciality();
        yoga.setType("Yoga");
        yoga.setCertificateLevel(4);
        puh.getSpeciality().add(yoga);

        instructors.add(kalle);
        instructors.add(puh);
    }

    @AfterEach
    void tearDown() {
        instructors.clear();
    }

    @Test
    void getInstructors_IfAdminShouldReturnAllInstructors() {
        // Arrange
        when(authInfoMock.getRole()).thenReturn("ADMIN");
        when(instructorRepositoryMock.findAll()).thenReturn(instructors);
        // Act
        List<InstructorView> result = instructorService.getInstructors();
        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(instructors));
        verify(instructorRepositoryMock, times(1)).findAll();
    }

    @Test
    void getInstructors_IfUserShouldReturnAllInstructorDTOs() {
        // Arrange
        when(authInfoMock.getRole()).thenReturn("USER");
        when(instructorRepositoryMock.findAll()).thenReturn(instructors);
        // Act
        List<InstructorView> result = instructorService.getInstructors();
        // Assert
        assertEquals(2, result.size());

        for (int i = 0; i < result.size(); i++) {
            InstructorView view = result.get(i);
            assertTrue(view instanceof InstructorDTO);

            InstructorDTO dto = (InstructorDTO) view;
            Instructor instructor = instructors.get(i);

            assertEquals(instructor.getId(), dto.getId());
            assertEquals(instructor.getName(), dto.getName());
            assertEquals(instructor.getSpeciality(), dto.getSpeciality());
        }
        verify(instructorRepositoryMock, times(1)).findAll();
    }

    @Test
    void addInstructor_ShouldSaveAndReturnInstructor () {
        // Arrange
        Instructor skalman = new Instructor();
        skalman.setName("Skalman");
        skalman.setSecretInfo("är klok");
        Speciality cardio = new Speciality();
        cardio.setType("Cardio");
        cardio.setCertificateLevel(1);
        skalman.getSpeciality().add(cardio);
        when(instructorRepositoryMock.save(skalman)).thenReturn(skalman);
        // Act
        Instructor result = instructorService.addInstructor(skalman);
        // Assert
        assertNotNull(result);
        assertEquals(skalman.getName(), result.getName());
        assertEquals(skalman.getSecretInfo(), result.getSecretInfo());
        assertEquals(skalman.getSpeciality(), result.getSpeciality());
        verify(instructorRepositoryMock, times(1)).save(skalman);
    }

    @Test
    void addInstructor_ShouldAddSpecialityListEvenIfNotExplicitlyAdded() {
        // Arrange
        Instructor skalman = new Instructor();
        skalman.setName("Skalman");
        skalman.setSecretInfo("är klok");
        when(instructorRepositoryMock.save(skalman)).thenReturn(skalman);
        // Act
        Instructor result = instructorService.addInstructor(skalman);
        // Assert
        assertNotNull(result);
        assertEquals(skalman.getName(), result.getName());
        assertEquals(skalman.getSecretInfo(), result.getSecretInfo());
        assertNotNull(skalman.getSpeciality());
        assertTrue(result.getSpeciality().isEmpty());
        verify(instructorRepositoryMock, times(1)).save(skalman);
    }

    @Test
    void addInstructor_ShouldThrowExceptionWhenNameIsNull () {
        // Arrange
        Instructor skalman = new Instructor();
        skalman.setSecretInfo("är klok");
        // Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                instructorService.addInstructor(skalman));
        // Assert
        verify(instructorRepositoryMock, never()).save(any());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Name required", exception.getReason());
    }

    @Test
    void addInstructor_ShouldThrowExceptionWhenNameIsEmpty () {
        // Arrange
        Instructor skalman = new Instructor();
        skalman.setName("");
        skalman.setSecretInfo("är klok");
        // Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                instructorService.addInstructor(skalman));
        // Assert
        verify(instructorRepositoryMock, never()).save(any());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Name required", exception.getReason());
    }

}