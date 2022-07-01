package com.healthcareApp.healthcareApp.repository;

import com.healthcareApp.healthcareApp.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepo extends JpaRepository<Patient, String> {
        Patient findByEmail(String email);
        Optional<Patient> findByIdOrEmail(String id, String email);


        }