package br.com.alura.school.section;

import br.com.alura.school.support.validation.Unique;
import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewSectionRequest {

    @Unique(entity = Section.class, field = "code")
    @Size(max = 10, message = "{code.max.size}")
    @NotBlank(message = "{code.not.blank}")
    @JsonProperty
    private final String code;

    @Size(min = 5, max = 30, message = "{title.max.min.size}")
    @NotBlank(message = "{title.not.blank}")
    @JsonProperty
    private final String title;

    @Size(max = 20,  message = "{author.max.size}")
    @NotBlank(message = "{author.not.blank}")
    @JsonProperty
    private final String author;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    NewSectionRequest(@JsonProperty("code") String code, @JsonProperty("title") String title, @JsonProperty("author") String author) {
        this.code = code;
        this.title = title;
        this.author = author;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    Section toEntity(User user) {
        return new Section(code, title, user);
    }
}
