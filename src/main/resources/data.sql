insert into User (id, username, email, role) values (1, 'alex', 'alex@email.com', 'INSTRUCTOR');
insert into User (id, username, email, role) values (2,'lucas', 'lucas@email.com', 'INSTRUCTOR');
insert into User (id, username, email, role) values (3,'ana', 'ana@email.com', 'STUDENT');

insert into Course (id, code, name, description) values (1, 'java-1', 'Java OO', 'Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.');
insert into Course (id, code, name, description) values (2, 'java-2', 'Java Collections', 'Java Collections: Lists, Sets, Maps and more.');

insert into `Section`(id, code, title, user_id) values (1, 'java - 1', 'Object orientation', 1);
insert into `Section`(id, code, title, user_id) values (2, 'java - 2', 'Conditionals', 2);

insert into video(id, video) values (1, 'https://youtu.be/90NcVNsKGik');
insert into video(id, video) values (2, 'https://youtu.be/sTX0UEplF54');
insert into video(id, video) values (3, 'https://youtu.be/dG2Kg8DS4u4');

insert into section_video(section_id, video_id) values (1, 1);
insert into section_video(section_id, video_id) values (2, 2);
insert into section_video(section_id, video_id) values (2, 3);

insert into course_section (course_id, section_id) values (2, 1);
insert into course_section (course_id, section_id) values (1, 2);

insert into enrollment(id, course_id, user_id, enroll_date) values (2, 2, 2, '2022-09-22');
insert into enrollment(id, course_id, user_id, enroll_date) values (1, 1, 1, '2022-09-19');

