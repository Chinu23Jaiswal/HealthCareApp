package com.healthcareApp.healthcareApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Illness {
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private String illness;
}
