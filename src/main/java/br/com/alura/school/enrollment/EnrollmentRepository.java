package br.com.alura.school.enrollment;

import br.com.alura.school.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByCourseIdAndUserId(Long courseId, Long userId);
    List<Enrollment> findByUser(User user);
    Optional<Enrollment> findCourseById(Long courseId);
}