package com.healthcareApp.healthcareApp.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "diagnosis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDiagnosis {
    @Id
    private String appointmentId;

    private String doctorId;
    private String doctorName;
    private String doctorEmail;

    private String patientId;
    private String patientName;
    private String patientContact;
    private String patientEmail;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "diagnosis", cascade = {CascadeType.ALL})
    @JsonManagedReference
    private List<Prescription> prescription;
}
