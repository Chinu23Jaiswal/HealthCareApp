package com.healthcareApp.healthcareApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisMail {
    private String appointmentId;

    private String doctorId;
    private String doctorName;
    private String doctorEmail;

    private String patientId;
    private String patientName;
    private long patientContact;
    private String patientEmail;

    private List<Prescription> prescription;
}
