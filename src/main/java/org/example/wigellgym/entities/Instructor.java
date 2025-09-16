package org.example.wigellgym.entities;

/*--Instruktörer ska ha namn och specialitet. */

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "instructor_id")
    private Integer id;
    @Column(length = 12, nullable = false)
    private String name;
    @Column(length = 12, nullable = false)
    private List<String> speciality;


}
