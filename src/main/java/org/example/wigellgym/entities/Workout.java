package org.example.wigellgym.entities;

/*--Träningspass ska ha namn, typ (t.ex. yoga, styrke), max antal deltagare, pris och instruktör. */

import jakarta.persistence.*;

@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_id")
    private Integer id;


}
