package org.example.wigellgym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.dto.WorkoutDTO;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.external.ConversionApiClient;
import org.example.wigellgym.repositories.BookingRepository;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.example.wigellgym.services.BookingServiceImpl;
import org.example.wigellgym.services.WorkoutServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerControllerBookingServiceImplTest {


    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private BookingRepository bookingRepositoryMock;

    @Mock
    private WorkoutRepository workoutRepositoryMock;

    @Mock
    private WorkoutServiceImpl workoutServiceMock;

    @Mock
    private ConversionApiClient conversionServiceMock;

    @Mock
    private AuthInfo authInfoMock;

    @InjectMocks
    private BookingServiceImpl bookingService;



    private Workout workout;
    private Booking booking;
    private WorkoutDTO workoutDTO;


    @BeforeEach
    void setUp() {
        CustomerController customerController = new CustomerController(workoutServiceMock, bookingService);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());



        workout = new Workout();
        workout.setId(1);
        workout.setDateTime(LocalDateTime.now().plusDays(2));
        workout.setFreeSpots(5);
        workout.setPriceSek(200.0);

        booking = new Booking();
        booking.setId(1);
        booking.setWorkout(workout);
        booking.setCanceled(false);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void bookWorkout_ShouldAddBookingForWorkout () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workout.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        when(conversionServiceMock.getConversionRate()).thenReturn(0.1);
        when(bookingRepositoryMock.save(any(Booking.class))).thenReturn(booking);
        when(workoutServiceMock.makeWorkoutDTO(workout)).thenReturn(workoutDTO);
        when(authInfoMock.getAuthUsername()).thenReturn("The right username");
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId())).
                andExpect(jsonPath("$.customerUsername").value("The right username"));

        /// ASERT mer saker som inte ligger i if-sats
        verify(bookingRepositoryMock, times(1)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(1)).save(any(Workout.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfIdIsNull () throws Exception {
        //Arrange
        workout.setId(null);
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isBadRequest());
                verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfIdNotFound () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workout.getId())).thenReturn(Optional.empty());
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isNotFound());
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfAlreadyBooked () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workout.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(true);;
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isConflict());
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfIsDateToSoon () throws Exception {
        //Arrange
        workout.setDateTime(LocalDateTime.now().minusDays(2));
        when(workoutRepositoryMock.findById(workout.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isUnprocessableEntity());
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }


    @Test
    void bookWorkout_ShouldThrowIfNoFreeSpots () throws Exception {
        //Arrange
        workout.setFreeSpots(0);
        when(workoutRepositoryMock.findById(workout.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isConflict());
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }

    @Test
    void bookWorkout_ShouldThrowIfConversionFails () throws Exception {
        //Arrange
        when(workoutRepositoryMock.findById(workout.getId())).thenReturn(Optional.of(workout));
        when(bookingRepositoryMock.existsByWorkoutAndCustomerUsernameAndCanceledFalse(any(), any()))
                .thenReturn(false);
        when(conversionServiceMock.getConversionRate())
                .thenThrow(new IllegalStateException("Failed to fetch conversion rate"));
        // Act - Perform the action (HTTP request) - (when) - Operate
        // Assert - Verify the results - (then) - Check
        mockMvc.perform(post("/wigellgym/bookworkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workout)))
                .andExpect(status().isServiceUnavailable());
        verify(bookingRepositoryMock, times(0)).save(any(Booking.class));
        verify(workoutRepositoryMock, times(0)).save(any(Workout.class));
    }





/*

    @Test
    void cancelWorkout_ShouldCancelBooking() throws Exception {
        //Arrange
        when(authInfoMock.getAuthUsername()).thenReturn("The Right User");
        when(bookingRepositoryMock.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepositoryMock.save(any(Booking.class))).thenReturn(booking);
        when(workoutServiceMock.makeWorkoutDTO(workout)).thenReturn(workoutDTO);
        //Act
        mockMvc.perform(put("/wigellgym/cancelworkout")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))







    }
*/




}