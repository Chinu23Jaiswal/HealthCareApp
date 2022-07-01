package com.healthcareApp.healthcareApp.controller;

import com.healthcareApp.healthcareApp.config.ValidityChecker;
import com.healthcareApp.healthcareApp.entity.*;
import com.healthcareApp.healthcareApp.exceptions.UnableToProcessException;
import com.healthcareApp.healthcareApp.services.MessagingService;
import com.healthcareApp.healthcareApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {
    @Autowired
    private UserService patientService;

    @Autowired
    private MessagingService<MailingDao> messagingService;




    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@RequestBody Patient patient) {
        patientService.savePatient(patient);
        return new ResponseEntity<>("Patient Registered Successfully with id" + patient.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<?> getUserDetails(@RequestBody JwtRequest jwtRequest) {
        String id = jwtRequest.getUserId();
        Patient userDetails = (Patient) patientService.getUser(id, jwtRequest.getEmail());
        if (userDetails != null) {
            return new ResponseEntity<>(ValidityChecker.generateUserData(userDetails), HttpStatus.OK);
        }
        throw new UnableToProcessException("Patient detail not found");
    }

    @PostMapping("/speciality")
    public ResponseEntity<?> getDoctorSpecialityList(@RequestBody FindDoctorBySpeciality findDoctorBySpeciality) {
        return ResponseEntity.ok().body(patientService.findDoctorsBySpeciality(findDoctorBySpeciality.getSpeciality()));
    }

    @PostMapping("/selectDoctor")
    public ResponseEntity<?> bookAppointment(@RequestBody Appointment bookAppointment) {
        return ResponseEntity.ok().body(patientService.saveAppointment(bookAppointment));
    }

    @PostMapping("/sendMail")
    public ResponseEntity<?> sendEmail(@RequestBody Illness illness) {
        MailingDao illnessMail = patientService.sendMail(illness);
        this.messagingService.sendMessageToTopic("topic", illnessMail);
        Map<String, String> map = new HashMap<>();
        map.put("acknowledgement", String.format("Email has been sent to Dr.%s at %s", illnessMail.getDoctorName(), illnessMail.getDoctorEmail()));
        map.put("message", String.format("Hi %s, your concern has been noted. Dr.%s will call you shortly", illnessMail.getPatientName(), illnessMail.getDoctorName()));
        return ResponseEntity.ok().body(map);
    }
}
