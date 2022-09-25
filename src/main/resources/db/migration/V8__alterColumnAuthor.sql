ALTER TABLE section RENAME COLUMN author TO user_id;

ALTER TABLE section ALTER COLUMN user_id BIGINT NULL;

ALTER TABLE section ADD CONSTRAINT fk_section_user_id
FOREIGN KEY (user_id) REFERENCES `user`(id);




