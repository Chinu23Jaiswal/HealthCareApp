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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    @Autowired
    private UserService userService;

//    private final KafkaTemplate<String, Illness> kafkaTemplate;
@Autowired
private MessagingService<DiagnosisMail> messagingService;


    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor) {

        userService.saveDoctor(doctor);
        log.info("Doctor has been saved in the database");
        return new ResponseEntity<>("Doctor Registered Successfully with id" + doctor.getId(), HttpStatus.CREATED);

    }

//    @PostMapping("/reg")
//    public String saveDoc(@RequestBody Illness illness) {
//        kafkaTemplate.send("topic", illness);
//        return "Welcome to registration";
//    }


    @PostMapping("/list")
    public ResponseEntity<?> getUserDetails(@RequestBody JwtRequest jwtRequest) {
        String id = jwtRequest.getUserId();
        log.info("Listing doctors");
        Doctor userDetails = (Doctor) userService.getUser(id, jwtRequest.getEmail());
        if (userDetails != null) {
            return new ResponseEntity<>(ValidityChecker.generateUserData(userDetails), HttpStatus.OK);
        }
        throw new UnableToProcessException("Doctor detail not found");
    }

    @PostMapping("/diagnosis")
    public ResponseEntity<?> getDoctorDiagnosisList(@RequestBody GetDiagnosisForIdDao getDiagnosisForIdDTO) {
        return ResponseEntity.ok(userService.findDiagnosisForId(getDiagnosisForIdDTO.getId(), "DOC"));
    }

    @PostMapping("/sendDiagnosis")
    public ResponseEntity<?> sendDiagnosis(@RequestBody DoctorDiagnosis diagnosis) {

        DiagnosisMail diagnosisMail = userService.sendPrescription(diagnosis);

        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("acknowledgement", "Email sent to " + diagnosisMail.getPatientEmail());
        messageMap.put("message", "Prescription has been sent. Thank you Dr." + diagnosisMail.getDoctorName());
        messagingService.sendMessageToTopic("topic", diagnosisMail);
        log.info("Email sent to the patient");
        return ResponseEntity.ok().body(messageMap);
    }




}

