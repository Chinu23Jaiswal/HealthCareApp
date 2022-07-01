package com.healthcareApp.healthcareApp.config;

import com.healthcareApp.healthcareApp.entity.Doctor;
import com.healthcareApp.healthcareApp.entity.Patient;
import com.healthcareApp.healthcareApp.entity.UserDTO;
import com.healthcareApp.healthcareApp.exceptions.UnableToProcessException;

import javax.print.Doc;

public class ValidityChecker {
    public static boolean isDoctorValid(Doctor doctor) {
        if (doctor.getDocName()!=null) {
            if (doctor.getPassword()!=null) {
                if (doctor.getEmail()!=null){
                       return true;
                } else {
                    throw new UnableToProcessException("Please enter email field to register");
                }
            } else {
                throw new UnableToProcessException("Password cannot be empty");
            }
        }
        throw new UnableToProcessException("Username cannot be empty");
    }

    public static boolean isPatientValid(Patient patient) {
        if (patient.getPatName()!=null) {
            if (patient.getPassword()!=null) {
                if (patient.getEmail()!=null){
                    return true;
                } else {
                    throw new UnableToProcessException("Please enter email field to register");
                }
            } else {
                throw new UnableToProcessException("Password cannot be empty");
            }
        }
        throw new UnableToProcessException("Username cannot be empty");
    }
    public static UserDTO generateUserData(Doctor doctor) {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(doctor.getAuthorities().toString().replace("[", "").replace("]", ""));
        userDTO.setDateOfBirth(doctor.getDateOfBirth());
        userDTO.setEmail(doctor.getEmail());
        userDTO.setId(doctor.getId());
        userDTO.setName(doctor.getDocName());
        userDTO.setPhoneNumber(doctor.getPhnNo());
        return userDTO;
    }

    public static UserDTO generateUserData(Patient patient) {
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(patient.getAuthorities().toString().replace("[", "").replace("]", ""));
        userDTO.setEmail(patient.getEmail());
        userDTO.setId(patient.getId());
        userDTO.setName(patient.getPatName());
        userDTO.setPhoneNumber(patient.getPhoneNumber());
        return userDTO;
    }

}
