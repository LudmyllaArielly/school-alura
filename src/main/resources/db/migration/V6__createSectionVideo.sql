CREATE TABLE section_video (
    section_id BIGINT NOT NULL,
    video_id   BIGINT NOT NULL
);

ALTER TABLE section_video ADD CONSTRAINT fk_section_section_id
FOREIGN KEY (section_id) REFERENCES `section`(id);

ALTER TABLE section_video ADD CONSTRAINT fk_section_video_id
FOREIGN KEY (video_id) REFERENCES video(id);


