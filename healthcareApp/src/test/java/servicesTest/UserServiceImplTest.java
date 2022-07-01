package servicesTest;

import com.healthcareApp.healthcareApp.entity.Doctor;
import com.healthcareApp.healthcareApp.entity.Patient;
import com.healthcareApp.healthcareApp.exceptions.UnableToProcessException;
import com.healthcareApp.healthcareApp.repository.DoctorRepo;
import com.healthcareApp.healthcareApp.repository.PatientRepo;
import com.healthcareApp.healthcareApp.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private PatientRepo patientRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void test_valid_patient_loadUserByUsername() {
        String id = "PAT101";

        Patient patient = new Patient(id, "test@email.com", "password", "test", "M", 1234567890L,20);

        when(patientRepo.findById(id)).thenReturn(Optional.of(patient));
        assertEquals("PAT101", userService.loadUserByUsername(id).getUsername());
    }

    @Test
    void test_valid_doctor_loadUserByUsername() {
        String id = "DOC101";

        Doctor doctor = new Doctor(id, "hitesh", "password", "test", "F",
                1234567890L, "MBBS", "ENT", LocalDate.of(2020, 6, 6));

        when(doctorRepo.findById(id)).thenReturn(Optional.of(doctor));
        assertEquals("DOC101", userService.loadUserByUsername(id).getUsername());
    }

    @Test
    void test_inValid_emptyId_loadUserByUsername() {
        UsernameNotFoundException thrown = Assertions
                .assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(null), "User Id not found");

        assertEquals("User Id not found", thrown.getMessage());
    }

    @Test
    void test_inValid_patientNotFound_loadUserByUsername() {
        String id = "PAT101";

        when(patientRepo.findById(id)).thenReturn(Optional.empty());
        UsernameNotFoundException thrown = Assertions
                .assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(id), "Patient not found");
        assertEquals("Patient not found", thrown.getMessage());
    }

    @Test
    void test_inValid_doctorNotFound_loadUserByUsername() {
        String id = "DOC101";
        when(doctorRepo.findById(id)).thenReturn(Optional.empty());
        UsernameNotFoundException thrown = Assertions
                .assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(id), "Doctor not found");
        assertEquals("Doctor not found", thrown.getMessage());
    }

    @Test
    void test_valid_savePatient() {
        String id = "PAT101";
        Patient patient = new Patient(id, "test@email.com", "password", "test", "M", 1234567890L,20);
        userService.savePatient(patient);
        verify(patientRepo).save(patient);
    }

    @Test
    void test_invalidUsername_savePatient() {
        String id = "PAT101";
        Patient patient = new Patient(id, "test@email.com", "password", null, "M", 1234567890L,20);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.savePatient(patient), "Username cannot be empty");

        Assertions.assertEquals("Username cannot be empty", thrown.getMessage());
    }

    @Test
    void test_mandatoryEmailEmpty_savePatient() {
        String id = "PAT101";
        Patient patient = new Patient(id, null, "password", "null", "M", 1234567890L,20);
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.savePatient(patient), "Please enter email field to register");

        Assertions.assertEquals("Please enter email field to register", thrown.getMessage());
    }

    @Test
    void test_valid_saveDoctor() {
        String id = "DOC101";
        Doctor doctor = new Doctor(id, "test@gmail.com", "password", "test", "F",
                1234567890L, "MBBS", "ENT", LocalDate.of(2020, 6, 6));
        userService.saveDoctor(doctor);
        verify(doctorRepo).save(doctor);
    }

    @Test
    void test_invalidUsername_saveDoctor() {
        String id = "DOC101";
        Doctor doctor = new Doctor(id, null, "password", "test", "F",
                1234567890L, "MBBS", "ENT", LocalDate.of(2020, 6, 6));
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveDoctor(doctor), "Username cannot be empty");

        Assertions.assertEquals("Username cannot be empty", thrown.getMessage());
    }

    @Test
    void test_mandatoryEmailEmpty_saveDoctor() {
        String id = "DOC101";
        Doctor doctor = new Doctor(id, "Doc", "password", "test", "F",
                1234567890L, null, "ENT", LocalDate.of(2020, 6, 6));
        UnableToProcessException thrown = Assertions
                .assertThrows(UnableToProcessException.class, () -> userService.saveDoctor(doctor), "Please enter email field to register");

        Assertions.assertEquals("Please enter email field to register", thrown.getMessage());
    }

}
