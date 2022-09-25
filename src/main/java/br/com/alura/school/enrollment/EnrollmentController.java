package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

@RestController
public class EnrollmentController {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentController(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{code}/enroll")
    public ResponseEntity<Void> enrollment(@PathVariable("code") String code, @Valid @RequestBody NewEnrollmentRequest newEnrollmentRequest) {

        User user = checkIfUserExits(newEnrollmentRequest);
        Course course = checkIfCourseExits(code);

        Optional<Enrollment> enrollment = enrollmentRepository.findByCourseIdAndUserId(course.getId(), user.getId());

        if (enrollment.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        enrollmentRepository.save(new Enrollment(course, LocalDate.now(), user));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private User checkIfUserExits(NewEnrollmentRequest newEnrollmentRequest) {
        User user = userRepository.findByUsername(newEnrollmentRequest.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("User %s not found", newEnrollmentRequest.getUsername())));
        return user;
    }

    private Course checkIfCourseExits(String code) {
        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("There is no course with this code %s", code)));
        return course;
    }
}
