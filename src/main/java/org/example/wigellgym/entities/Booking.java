package org.example.wigellgym.entities;

/*--Bokningar ska inkludera kund, träningspass, datum och totalpris (SEK och Euro). */

import jakarta.persistence.*;

@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;




}
