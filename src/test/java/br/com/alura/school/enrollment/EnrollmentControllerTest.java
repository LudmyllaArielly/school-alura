package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EnrollmentControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void should_add_new_enrollment() throws Exception {
        User user = userRepository.save(new User("alex", "alex@gmail.com"));
        courseRepository.save(new Course("java-1", "Spring boot-1", "Spring boot-1"));

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("alex");

        mockMvc.perform(post("/courses/java-1/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void should_return_bad_request_when_sending_request_without_username() throws Exception {
        User user = userRepository.save(new User("ana", "ana@gmail.com"));
        courseRepository.save(new Course("java-2", "Spring boot-2", "Spring boot-2"));

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("");

        mockMvc.perform(post("/courses/java-2/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_accept_non_existent_course() throws Exception {
        User user = userRepository.save(new User("jose", "jose@gmail.com"));

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("jose");

        mockMvc.perform(post("/courses/java-3/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_not_accept_duplicate_user() throws Exception {
        User user = userRepository.save(new User("maria", "maria@gmail.com"));

        Course course = courseRepository.save(new Course("java-4", "Spring boot-4", "Spring boot-4"));

        enrollmentRepository.save(new Enrollment(course, LocalDate.now(), user));

        NewEnrollmentRequest newEnrollmentRequest = new NewEnrollmentRequest("maria");

        mockMvc.perform(post("/courses/java-4/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newEnrollmentRequest)))
                .andExpect(status().isBadRequest());
    }

}
