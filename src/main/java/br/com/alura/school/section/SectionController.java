package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.enrollment.Enrollment;
import br.com.alura.school.enrollment.EnrollmentRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class SectionController {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SectionController(SectionRepository sectionRepository, CourseRepository courseRepository, UserRepository userRepository, EnrollmentRepository enrollmentRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<SectionResponse> newSection(@PathVariable("code") String code, @RequestBody @Valid NewSectionRequest newSectionRequest) {

        Course course = checkIfCourseExits(newSectionRequest, code);

        User user = checkIfAuthorIsInstructor(newSectionRequest);

        course.addSection(newSectionRequest.toEntity(user));
        Course save = courseRepository.save(course);

        URI location = URI.create(format("/courses/%s/sections/%s", code, newSectionRequest.getCode()));
        return ResponseEntity.created(location).body(new SectionResponse(newSectionRequest.toEntity(user)));
    }

    @GetMapping("/courses/sectionByVideosReport")
    public ResponseEntity<Set<VideoReportResponse>> getVideosReport() {
        List<Course> courses = courseRepository.findAll();

        Set<VideoReportResponse> courseVideoResponse = new HashSet<>();
        searchAttributesOfTheCourseIsFromTheSection(courses, courseVideoResponse);

        if (courseVideoResponse.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        courseVideoResponse.stream().sorted(Comparator.comparing(VideoReportResponse::getTotalVideos).reversed());
        return ResponseEntity.ok(courseVideoResponse);
    }

    private void searchAttributesOfTheCourseIsFromTheSection(List<Course> courses, Set<VideoReportResponse> courseVideoResponse) {

        for (int i = 0; i < courses.size(); i++) {

            for (int j = 0; j < courses.get(i).getSections().size(); j++) {

                Optional<Enrollment> checksIfTheCourseHasEnrollment = enrollmentRepository.findCourseById(courses.get(i).getId());

                if (checksIfTheCourseHasEnrollment.isPresent()) {

                    String name = courses.get(i).getName();
                    String title = courses.get(i).getSections().get(j).getTitle();
                    String author = courses.get(i).getSections().get(j).getAuthor().getUsername();
                    Integer videos = courses.get(i).getSections().get(j).getVideos().size();

                    courseVideoResponse.add(new VideoReportResponse(name, title, author, videos));
                }
            }
        }
    }

    private Course checkIfCourseExits(NewSectionRequest newSectionRequest, String code) {
        Course course = courseRepository.findByCode(code)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return course;
    }

    private User checkIfUserExits(NewSectionRequest newSectionRequest) {
        User user = userRepository.findByUsername(newSectionRequest.getAuthor())
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

