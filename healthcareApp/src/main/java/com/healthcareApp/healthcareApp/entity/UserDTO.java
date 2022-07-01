package com.healthcareApp.healthcareApp.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserDTO {
    private String id;
    private long phoneNumber;
    private LocalDate dateOfBirth;
    private String role;

    private String name;
    private String email;
}
