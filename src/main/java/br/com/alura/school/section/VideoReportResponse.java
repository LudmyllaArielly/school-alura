package br.com.alura.school.section;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VideoReportResponse {

    @JsonProperty
    private String courseName;

    @JsonProperty
    private String sectionTitle;

    @JsonProperty
    private String authorName;

    @JsonProperty
    private Integer totalVideos;

    public VideoReportResponse(String courseName, String sectionTitle, String authorName, Integer totalVideos) {
        this.courseName = courseName;
        this.sectionTitle = sectionTitle;
        this.authorName = authorName;
        this.totalVideos = totalVideos;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Integer getTotalVideos() {
        return totalVideos;
    }
}
