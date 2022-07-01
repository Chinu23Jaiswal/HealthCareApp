package com.healthcareApp.healthcareApp.services;

import com.healthcareApp.healthcareApp.entity.*;

import java.util.List;
import java.util.Map;

public interface UserService {
    Doctor saveDoctor(Doctor doctor);
    Object getUser(String id, String email);
    Patient savePatient(Patient patient);

    List<SelectSpecialityDoctorDao> findDoctorsBySpeciality(String speciality);
    Map<String, String> saveAppointment(Appointment appointment);

    MailingDao sendMail(Illness illness);
    List<DoctorDiagnosis> findDiagnosisForId(String id, String check);
    DiagnosisMail sendPrescription(DoctorDiagnosis diagnosis);
}
