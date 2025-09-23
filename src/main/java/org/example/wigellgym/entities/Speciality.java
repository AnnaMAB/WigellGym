package org.example.wigellgym.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 40, nullable = false)
    private String type;
    @Column(nullable = false)
    private Integer certificateLevel;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "instructor_id", nullable = true)
    @JsonIgnore
    private Instructor instructor;

    public Speciality() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCertificateLevel() {
        return certificateLevel;
    }

    public void setCertificateLevel(Integer certificateLevel) {
        this.certificateLevel = certificateLevel;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Speciality{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", certificateLevel=" + certificateLevel +
                ", instructor=" + instructor +
                '}';
    }
}
