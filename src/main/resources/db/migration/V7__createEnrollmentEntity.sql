CREATE TABLE enrollment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    enroll_date DATE NOT NULL
);

ALTER TABLE enrollment ADD CONSTRAINT fk_enrollment_course_id
FOREIGN KEY (course_id) REFERENCES course(id);

ALTER TABLE enrollment ADD CONSTRAINT fk_enrollment_user_id
FOREIGN KEY (user_id) REFERENCES `user`(id);


