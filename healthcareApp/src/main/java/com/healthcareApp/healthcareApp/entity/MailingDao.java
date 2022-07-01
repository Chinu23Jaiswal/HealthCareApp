package com.healthcareApp.healthcareApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailingDao {
    private String appointmentId;
    private String doctorName;
    private String doctorEmail;
    private String patientName;
    private long patientContact;
    private int patientAge;
    private String patientEmail;
    private String patientIllness;
}
