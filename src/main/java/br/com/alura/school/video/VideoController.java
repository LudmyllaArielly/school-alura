package br.com.alura.school.video;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static java.lang.String.format;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
public class VideoController {

    private final VideoRepository videoRepository;
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    public VideoController(VideoRepository videoRepository, SectionRepository sectionRepository, CourseRepository courseRepository) {
        this.videoRepository = videoRepository;
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/courses/{courseCode}/sections/{sectionCode}")
    ResponseEntity<VideoResponse> newVideo(@PathVariable("courseCode") String courseCode, @PathVariable("sectionCode") String sectionCode, @Valid @RequestBody NewVideoRequest newVideoRequest) {

        Section section = checkIfSectionExits(sectionCode);

        Course course = checkIfCourseExits(courseCode);

        checkIfVideoExits(newVideoRequest);

        section.addVideo(newVideoRequest.toEntity());
        Section save = sectionRepository.save(section);

        URI location = URI.create(format("/courses/%s", course.getCode()));
        return ResponseEntity.created(location).body(new VideoResponse(newVideoRequest.toEntity().getVideo()));
    }

    private Section checkIfSectionExits(String sectionCode){
        Section section = sectionRepository.findByCode(sectionCode)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", sectionCode)));
        return section;
    }

    private Course checkIfCourseExits(String courseCode) {
        Course course = courseRepository.findByCode(courseCode)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", courseCode)));
        return course;
    }

    private void checkIfVideoExits(NewVideoRequest newVideoRequest) {
        Optional<Video> findByVideo = videoRepository.findByVideo(newVideoRequest.getVideo());

        if (findByVideo.isPresent() && findByVideo.get().getVideo().equals(newVideoRequest.getVideo())) {
            throw new ResponseStatusException(BAD_REQUEST, "The video already exists.");
        }
    }
}
