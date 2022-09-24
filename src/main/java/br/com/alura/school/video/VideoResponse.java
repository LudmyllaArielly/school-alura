package br.com.alura.school.video;

import com.fasterxml.jackson.annotation.JsonProperty;

class VideoResponse {

    @JsonProperty
    private final String video;

    VideoResponse(String video) {
        this.video = video;

    }

}
