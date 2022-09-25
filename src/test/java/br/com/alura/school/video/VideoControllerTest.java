package br.com.alura.school.video;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class VideoControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_add_new_video() throws Exception {

        User user = userRepository.save(new User("jose", "jose@gmail.com", UserRole.INSTRUCTOR));
        courseRepository.save(new Course("java-1", "Spring boot-1", "Spring boot-1"));
        sectionRepository.save(new Section("section-1", "Spring-1", user));

        NewVideoRequest newVideoRequest = new NewVideoRequest("https://youtu.be/sZAxLRMxEUo2");

        mockMvc.perform(post("/courses/java-1/sections/section-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-1/sections/section-1"));
    }

    @Test
    void should_not_duplicate_video() throws Exception {

        User user = userRepository.save(new User("julia", "julia@gmail.com", UserRole.INSTRUCTOR));
        courseRepository.save(new Course("java-2", "Spring boot-2", "Spring boot-2"));
        Section section = new Section("section-2", "Spring-2", user);

        Video video = new Video("https://youtu.be/sZAxLRMxEUo1");
        section.addVideo(video);
        sectionRepository.save(section);

        NewVideoRequest newVideoRequest = new NewVideoRequest("https://youtu.be/sZAxLRMxEUo1");

        mockMvc.perform(post("/courses/java-2/sections/section-2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_bad_when_sending_invalid_video_request() throws Exception {
        User user = userRepository.save(new User("alex", "alex@gmail.com", UserRole.INSTRUCTOR));

        courseRepository.save(new Course("java-3", "Spring boot-3", "Spring boot-3"));

        sectionRepository.save(new Section("section-3", "Spring-3", user));

        NewVideoRequest newVideoRequest = new NewVideoRequest("");

        mockMvc.perform(post("/courses/java-3/sections/section-3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newVideoRequest)))
                .andExpect(status().isBadRequest());
    }

}
