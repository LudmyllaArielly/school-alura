package br.com.alura.school.video;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotBlank
    private String video;

    @Deprecated
    public Video() {
    }

    public Video(String video) {
        this.video = video;
    }

    public Long getId() {
        return id;
    }

    public String getVideo() {
        return video;
    }
}
