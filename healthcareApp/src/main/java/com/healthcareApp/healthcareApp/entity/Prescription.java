package com.healthcareApp.healthcareApp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "prescription")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean morning;
    private boolean afternoon;
    private boolean night;
    private String numberOfDays;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private DoctorDiagnosis diagnosis;
}
