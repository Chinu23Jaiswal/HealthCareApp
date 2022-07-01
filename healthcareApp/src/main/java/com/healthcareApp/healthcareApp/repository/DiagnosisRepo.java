package com.healthcareApp.healthcareApp.repository;

import com.healthcareApp.healthcareApp.entity.DoctorDiagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiagnosisRepo extends JpaRepository<DoctorDiagnosis, Long> {

    List<DoctorDiagnosis> findByPatientId(String id);

    List<DoctorDiagnosis> findByDoctorId(String id);
}
