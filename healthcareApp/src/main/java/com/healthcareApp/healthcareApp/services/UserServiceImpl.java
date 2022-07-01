package com.healthcareApp.healthcareApp.services;

import com.healthcareApp.healthcareApp.entity.*;
import com.healthcareApp.healthcareApp.exceptions.UnableToProcessException;
import com.healthcareApp.healthcareApp.repository.AppintmentRepo;
import com.healthcareApp.healthcareApp.repository.DiagnosisRepo;
import com.healthcareApp.healthcareApp.repository.DoctorRepo;
import com.healthcareApp.healthcareApp.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.healthcareApp.healthcareApp.config.ValidityChecker.isDoctorValid;
import static com.healthcareApp.healthcareApp.config.ValidityChecker.isPatientValid;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private  DoctorRepo doctorRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private AppintmentRepo appintmentRepo;

    @Autowired
    private DiagnosisRepo diagnosisRepo;


    @Override
    public Doctor saveDoctor(Doctor doctor) {
        isDoctorValid(doctor);
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        return doctorRepo.save(doctor);
    }

    @Override
    public Object getUser(String id, String email) {
        if (id != null && !id.trim().isEmpty()) {
            id = id.trim().toUpperCase();
            if (id.startsWith("PAT")) {
                return patientRepo.findByIdOrEmail(id, email).orElse(null);
            } else if (id.startsWith("DOC")) {
                return doctorRepo.findByIdOrEmail(id, email).orElse(null);
            }
        }
    return null;
    }

    @Override
    public Patient savePatient(Patient patient) {
        isPatientValid(patient);
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        return patientRepo.save(patient);
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        if (id != null && !id.trim().isEmpty()) {
            id = id.trim().toUpperCase();

            if (id.startsWith("PAT")) {
                Optional<Patient> patientDTO = patientRepo.findById(id);
                return patientDTO.orElseThrow(() -> new UsernameNotFoundException("Patient not found"));
            } else if (id.startsWith("DOC")) {
                Optional<Doctor> doctorDTO = doctorRepo.findById(id);
                return doctorDTO.orElseThrow(() -> new UsernameNotFoundException("Doctor not found"));
            }
        }
        throw new UsernameNotFoundException("User Id not found");
    }

    @Override
    public List<SelectSpecialityDoctorDao> findDoctorsBySpeciality(String speciality) {
        if (speciality!=null) {
            List<Doctor> doctors = doctorRepo.findBySpeciality(speciality);
            List<SelectSpecialityDoctorDao> specialityDoctorDaoList = new ArrayList<>();

            if (doctors != null && !doctors.isEmpty()) {
                for (Doctor doctor : doctors) {
                    SelectSpecialityDoctorDao specialityDTO = new SelectSpecialityDoctorDao();
                    specialityDTO.setName(doctor.getDocName());
                    specialityDTO.setId(doctor.getId());
                    specialityDTO.setSpeciality(doctor.getSpeciality());
                    specialityDTO.setExperience(doctor.getExperience());
                    specialityDoctorDaoList.add(specialityDTO);
                }
            }
            return specialityDoctorDaoList;
        }
        throw new UnableToProcessException("Please enter valid speciality");
    }

    @Override
    public Map<String, String> saveAppointment(Appointment appointment) {
        if (appointment.getDoctorId()!=null && appointment.getPatientId()!=null) {
            Optional<Patient> patient = patientRepo.findById(appointment.getPatientId());
            if (patient.isPresent()) {
                Optional<Doctor> doctor = doctorRepo.findById(appointment.getDoctorId());
                if (doctor.isPresent()) {
                    appintmentRepo.save(appointment);
                    Map<String, String> messageMap = new HashMap<>();
                    String message = String.format("Dear %s, Thanks for choosing Med App. You have booked %s for your medical appointment.", patient.get().getPatName(), "Dr." + doctor.get().getDocName());
                    messageMap.put("message", message);
                    return messageMap;
                }
                throw new UnableToProcessException("Doctor ID not found");
            }
            throw new UnableToProcessException("Patient ID not found");
        }
        throw new UnableToProcessException("Please enter valid Patient/Doctor ID");
    }

    @Override
    public MailingDao sendMail(Illness illness) {
        if (illness.getDoctorId()!=null && illness.getPatientId()!=null && illness.getAppointmentId()!=null) {
            Appointment appointment = appintmentRepo.findById(Long.valueOf(illness.getAppointmentId())).orElseThrow(() -> new UnableToProcessException("Appointment Id not found"));
            if (appointment.getDoctorId().equals(illness.getDoctorId()) && appointment.getPatientId().equals(illness.getPatientId())) {

               // log.info("Appointment status updated..");
                Patient patient = patientRepo.findById(illness.getPatientId()).orElseThrow(() -> new UnableToProcessException("Patient Id not found"));
                Doctor doctor = doctorRepo.findById(illness.getDoctorId()).orElseThrow(() -> new UnableToProcessException("Doctor Id not found"));

                // adding required to model class
                MailingDao illnessMail = new MailingDao();
                illnessMail.setAppointmentId(illness.getAppointmentId());
                illnessMail.setDoctorEmail(doctor.getEmail());
                illnessMail.setDoctorName(doctor.getDocName());
                illnessMail.setPatientIllness(illness.getIllness());
                illnessMail.setPatientAge(patient.getAge());
                illnessMail.setPatientContact(patient.getPhoneNumber());
                illnessMail.setPatientEmail(patient.getEmail());
                illnessMail.setPatientName(patient.getPatName());

                return illnessMail;
            }
            throw new UnableToProcessException("Patient ID not found");
        }
        throw new UnableToProcessException("Please enter valid Patient/Doctor ID");
    }

    @Override
    public List<DoctorDiagnosis> findDiagnosisForId(String id, String check) {
        if (id!=null) {
            id = id.trim().toUpperCase();
            List<DoctorDiagnosis> diagnosisList = new ArrayList<>();
            if (id.startsWith(check) && "PAT".equals(check)) {
                diagnosisList.addAll(diagnosisRepo.findByPatientId(id));
            } else if (id.startsWith(check) && "DOC".equals(check)) {
                diagnosisList.addAll(diagnosisRepo.findByDoctorId(id));
            } else {
                throw new UnableToProcessException("Invalid ID. Please try again with valid ID");
            }
            return diagnosisList;
        }

        throw new UnableToProcessException("Please enter valid ID");
    }

    @Override
    public DiagnosisMail sendPrescription(DoctorDiagnosis diagnosis) {
        if (diagnosis.getDoctorId()!=null && diagnosis.getPatientId()!=null
                && diagnosis.getAppointmentId()!=null && diagnosis.getPrescription() != null && !diagnosis.getPrescription().isEmpty()) {
            Appointment appointment = appintmentRepo.findById(Long.valueOf(diagnosis.getAppointmentId())).orElseThrow(() -> new UnableToProcessException("Appointment Id not found"));
            if (appointment.getDoctorId().equals(diagnosis.getDoctorId()) && appointment.getPatientId().equals(diagnosis.getPatientId())) {

                diagnosisRepo.save(diagnosis);

                Patient patient = patientRepo.findById(diagnosis.getPatientId()).orElseThrow(() -> new UnableToProcessException("Patient Id not found"));
                Doctor doctor = doctorRepo.findById(diagnosis.getDoctorId()).orElseThrow(() -> new UnableToProcessException("Doctor Id not found"));

                DiagnosisMail diagnosisMail = new DiagnosisMail();
                diagnosisMail.setAppointmentId(diagnosis.getAppointmentId());

                diagnosisMail.setPrescription(diagnosis.getPrescription());
                diagnosisMail.setDoctorId(diagnosis.getDoctorId());
                diagnosisMail.setDoctorName(doctor.getDocName());
                diagnosisMail.setDoctorEmail(doctor.getEmail());

                diagnosisMail.setPatientId(diagnosis.getPatientId());
                diagnosisMail.setPatientName(patient.getPatName());
                diagnosisMail.setPatientContact(patient.getPhoneNumber());
                diagnosisMail.setPatientEmail(patient.getEmail());

                return diagnosisMail;
            }
            throw new UnableToProcessException("Patient ID and Doctor ID is not matching for the given Appointment ID");
        }
        throw new UnableToProcessException("Please enter all the required details");
    }

}


