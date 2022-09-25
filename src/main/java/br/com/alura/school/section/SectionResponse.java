package br.com.alura.school.section;

import com.fasterxml.jackson.annotation.JsonProperty;

class SectionResponse {

    @JsonProperty
    private final String code;

    @JsonProperty
    private final String title;

    @JsonProperty
    private final String author;

    SectionResponse(Section section) {
        this.code = section.getCode();
        this.title = section.getTitle();
        this.author = section.getAuthor().getUsername();
    }

}
