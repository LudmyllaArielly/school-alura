package br.com.alura.school.enrollment;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class NewEnrollmentRequest {

    @Size(max = 30, message = "{username.max.size}")
    @NotBlank(message = "{username.not.blank}")
    @JsonProperty("username")
    private String username;

    @JsonCreator
    public NewEnrollmentRequest(@JsonProperty("username") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }


    public static Enrollment toEntity(Course course, User user) {
        return new Enrollment(course, LocalDate.now(), user);
    }
}
