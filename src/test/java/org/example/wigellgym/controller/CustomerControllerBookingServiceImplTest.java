package org.example.wigellgym.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.wigellgym.configs.AuthInfo;
import org.example.wigellgym.entities.Booking;
import org.example.wigellgym.entities.Workout;
import org.example.wigellgym.repositories.BookingRepository;
import org.example.wigellgym.repositories.WorkoutRepository;
import org.example.wigellgym.services.BookingServiceImpl;
import org.example.wigellgym.services.ConversionServiceImpl;
import org.example.wigellgym.services.WorkoutServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
    private ConversionServiceImpl conversionServiceMock;

    @Mock
    private AuthInfo authInfoMock;

    @InjectMocks
    private BookingServiceImpl bookingService;


    @InjectMocks
    private CustomerController customerController; // controller uses real service

    private Workout workout;
    private Booking booking;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(CustomerController).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCustomerBookings() {
    }

    @Test
    void bookWorkout() {
    }

    @Test
    void cancelBooking() {
    }
}