package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class SectionController {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public SectionController(SectionRepository sectionRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<SectionResponse> newSection(@PathVariable("code") String code, @RequestBody @Valid NewSectionRequest newSectionRequest) {

        Course course = checkIfCourseExits(newSectionRequest, code);

        checkIfAuthorIsInstructor(newSectionRequest);

        course.addSection(newSectionRequest.toEntity());
        Course save = courseRepository.save(course);

        URI location = URI.create(format("/courses/%s", newSectionRequest.getCode()));
        return ResponseEntity.created(location).body(new SectionResponse(newSectionRequest.toEntity()));
    }

    private Course checkIfCourseExits(NewSectionRequest newSectionRequest, String code) {
        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return course;
    }

    private User checkIfUserExits(NewSectionRequest newSectionRequest) {
        User user = userRepository.findByUsername(newSectionRequest.toEntity().getAuthor())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Author not found")));
        return user;
    }

    private User checkIfAuthorIsInstructor(NewSectionRequest newSectionRequest) {
        User user = checkIfUserExits(newSectionRequest);

        UserRole role = UserRole.INSTRUCTOR;
        boolean instructor = !user.getRole().equals(role);

        if (instructor) {
            throw new ResponseStatusException(BAD_REQUEST, "Author does not have instructor permission.");
        }
        return user;
    }

}

