package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.enrollment.Enrollment;
import br.com.alura.school.enrollment.EnrollmentRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import br.com.alura.school.video.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SectionControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void should_add_new_section() throws Exception {

        userRepository.save(new User("alex", "alex@gmail.com", UserRole.INSTRUCTOR));
        courseRepository.save(new Course("java-1", "Spring boot-1", "Spring boot-1"));

        NewSectionRequest newSectionRequest = new NewSectionRequest("section-1", "Spring-1", "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-1/sections/section-1"));
    }

    @Test
    void should_not_allow_non_instructor_user() throws Exception {
        userRepository.save(new User("ana", "ana@gmail.com"));
        courseRepository.save(new Course("java-2", "Spring boot-2", "Spring boot-2"));

        NewSectionRequest newSectionRequest = new NewSectionRequest("section-2", "Spring-2", "ana");

        mockMvc.perform(post("/courses/java-2/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_if_username_does_not_exist() throws Exception {
        courseRepository.save(new Course("java-3", "Spring boot-3", "Spring boot-3"));

        NewSectionRequest newSectionRequest = new NewSectionRequest("section-3", "Spring-3", "jose");

        mockMvc.perform(post("/courses/java-3/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_not_allow_duplication_of_code() throws Exception {
        User user = userRepository.save(new User("louis", "louis@gmail.com", UserRole.INSTRUCTOR));
        courseRepository.save(new Course("java-4", "Spring boot-4", "Spring boot-4"));

        sectionRepository.save(new Section("section-4", "Spring-4", user));

        NewSectionRequest newSectionRequest = new NewSectionRequest("section-4", "Spring-4", "louis");

        mockMvc.perform(post("/courses/java-4/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_if_course_does_not_exist() throws Exception {
        User user = userRepository.save(new User("joao", "joao@gmail.com", UserRole.INSTRUCTOR));

        NewSectionRequest newSectionRequest = new NewSectionRequest("section-5", "Spring-5", "joao");

        mockMvc.perform(post("/courses/java-5/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_get_sections_by_video_report() throws Exception {
        User user = userRepository.save(new User("luiza", "luiza@gmail.com"));
        Course course = new Course("java-6", "Spring boot-6", "Spring boot-6");
        courseRepository.save(course);

        //Enrollment enrollment = NewEnrollmentRequest.toEntity( course, user,);
        enrollmentRepository.save(new Enrollment(course, LocalDate.now(), user));

        Section section1 = new Section("section-6", "Spring-6", user);
        Section section2 = new Section("section-7", "Spring-7", user);

        course.addSection(section1);
        course.addSection(section2);

        Video video1 = new Video("https://youtu.be/8qhaDBCJh6I");
        Video video2 = new Video("https://youtu.be/8qhaDBCJhFtI");
        section2.addVideo(video1);
        section2.addVideo(video2);

        sectionRepository.saveAll(List.of(section1, section2));

        mockMvc.perform(get("/courses/sectionByVideosReport")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].courseName", is("Java OOP")))
                .andExpect(jsonPath("$[0].sectionTitle", is("Java - What is OOP")))
                .andExpect(jsonPath("$[0].authorName", is("louis")))
                .andExpect(jsonPath("$[0].totalVideos", is(2)))
                .andExpect(jsonPath("$[1].courseName", is("Java OOP")))
                .andExpect(jsonPath("$[1].sectionTitle", is("Classes and Objects")))
                .andExpect(jsonPath("$[1].authorName", is("louis")))
                .andExpect(jsonPath("$[1].totalVideos", is(0)));

    }


}
