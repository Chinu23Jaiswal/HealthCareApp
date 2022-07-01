package com.healthcareApp.healthcareApp.repository;

import com.healthcareApp.healthcareApp.entity.Appointment;
import com.healthcareApp.healthcareApp.entity.Illness;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppintmentRepo extends JpaRepository<Appointment, Long> {
}
