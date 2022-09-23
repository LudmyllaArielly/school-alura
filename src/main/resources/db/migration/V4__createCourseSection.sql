CREATE TABLE Course_section (
    course_id BIGINT NOT NULL,
    section_id BIGINT NOT NULL,
);

ALTER TABLE Course_section ADD CONSTRAINT fk_course_id
FOREIGN KEY (course_id) REFERENCES course(id);

ALTER TABLE Course_section ADD CONSTRAINT fk_section_id
FOREIGN KEY (section_id) REFERENCES `section`(id);
