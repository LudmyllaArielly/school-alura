package br.com.alura.school.section;

import br.com.alura.school.user.User;
import br.com.alura.school.video.Video;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String code;

    @Size(min = 5)
    @NotBlank
    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(name = "section_video",
            joinColumns = @JoinColumn(name = "section_id"),
            inverseJoinColumns = @JoinColumn(name = "video_id"))
    private List<Video> videos = new ArrayList<>();


    public Section() {
    }

    public Section(String code, String title, User author) {
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

    public User getAuthor(){
        return author;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public boolean addVideo(Video video){
        return getVideos().add(video);
    }
}
