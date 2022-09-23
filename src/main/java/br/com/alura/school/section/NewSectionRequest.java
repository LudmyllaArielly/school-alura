package br.com.alura.school.section;

import br.com.alura.school.support.validation.Unique;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

class NewSectionRequest {

    @Unique(entity = Section.class, field = "code")
    @NotBlank
    @JsonProperty
    private final String code;

    @Size(min = 5)
    @NotBlank
    @JsonProperty
    private final String title;

    @NotBlank
    @JsonProperty
    private final String author;

    NewSectionRequest(String code, String title, String author) {
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

    Section toEntity() {
        return new Section(code, title, author);
    }
}
