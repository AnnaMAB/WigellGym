package org.example.wigellgym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Instructor;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.external.ConversionApiClient;
import org.example.wigellgym.mapper.DtoConverter;
import org.example.wigellgym.repositories.BookingRepository;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.example.wigellgym.services.BookingServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@WithMockUser(roles = {"USER"})
class CustomerControllerBookingServiceImplTest {


    private ObjectMapper objectMapper;

    private Workout workout;
    private WorkoutDTO workoutDTO;
    private Instructor instructor;

    @MockitoBean
    private BookingRepository bookingRepositoryMock;

    @MockitoBean
    private WorkoutRepository workoutRepositoryMock;

    @MockitoBean
    private ConversionApiClient conversionServiceMock;

    @MockitoBean
    private AuthInfo authInfoMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DtoConverter dtoConverter;

    @Autowired
    private BookingServiceImpl bookingService;

    @Autowired
    private CustomerController controller;


    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        workoutDTO = new WorkoutDTO();
        workoutDTO.setId(1);

        instructor = new Instructor();
        instructor.setId(1);
        instructor.setName("Xero");

        workout = new Workout();
        workout.setId(1);
        workout.setDateTime(LocalDateTime.parse("2095-10-27T17:30:00"));
        workout.setFreeSpots(5);
        workout.setPriceSek(200.0);
        workout.setInstructor(instructor);
        workout.setCanceled(false);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void bookWorkout_ShouldAddBookingForWorkout () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workoutDTO.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        when(conversionServiceMock.getConversionRate()).thenReturn(0.1);
        when(bookingRepositoryMock.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(authInfoMock.getAuthUsername()).thenReturn("The right username");
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.customerUsername").value("The right username"))
                .andExpect(jsonPath("$.bookingDate").value(Matchers.containsString(LocalDate.now().toString())))
                .andExpect(jsonPath("$.totalPriceSek").value(200.0))
                .andExpect(jsonPath("$.canceled").value(false))
                .andExpect(jsonPath("$.workoutDTO.id").value(1))
                .andExpect(jsonPath("$.workoutDTO.dateTime").value("2095-10-27 17:30"))

                .andExpect(jsonPath("$.workoutDTO.instructorDTO.name").value("Xero"))

        ;
        verify(bookingRepositoryMock, times(1)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(1)).save(any(Workout.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfIdIsNull () throws Exception {
        //Arrange
        workoutDTO.setId(null);
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Workout id must be provided for booking"));
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfIdNotFound () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workoutDTO.getId())).thenReturn(Optional.empty());
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No workout exists with id: " + workoutDTO.getId() + "."));
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfIsCanceled () throws Exception {
        //Arrange
        workout.setCanceled(true);
        when(workoutRepositoryMock.findById(workoutDTO.getId())).thenReturn(Optional.of(workout));
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("You can't book a canceled workout."));;
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfAlreadyBooked () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workoutDTO.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(true);;
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("You have already booked the requested workout."));
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfIsDateToSoon () throws Exception {
        //Arrange
        workout.setDateTime(LocalDateTime.now().minusDays(2));
        when(workoutRepositoryMock.findById(workoutDTO.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Too late to book workout."));;
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfNoFreeSpots () throws Exception {
        //Arrange
        workout.setFreeSpots(0);
        when(workoutRepositoryMock.findById(workoutDTO.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("No free spots at the requested workout."));;
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }

    @Test
    void bookWorkout_ShouldThrowIfConversionFails () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workoutDTO.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        when(conversionServiceMock.getConversionRate())
                .thenThrow(new IllegalStateException("Failed to fetch conversion rate"));
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workoutDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value(503))
                .andExpect(jsonPath("$.message").value("Unable to reach Euro conversion API. Booking cannot be made."));
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }


}
