package com.healthcareApp.healthcareApp.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindDoctorBySpeciality {
    private String patientId;
    private String speciality;
}
