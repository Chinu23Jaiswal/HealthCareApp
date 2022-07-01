package com.healthcareApp.healthcareApp.repository;


import com.healthcareApp.healthcareApp.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepo extends JpaRepository<Doctor, String> {
    Doctor findByEmail(String email);
    List<Doctor> findBySpeciality(String speciality);
    Optional<Doctor> findByIdOrEmail(String id, String email);


}
