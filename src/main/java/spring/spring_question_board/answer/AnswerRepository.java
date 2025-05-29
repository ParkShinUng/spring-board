package spring.spring_question_board.answer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.spring_question_board.question.Question;
import spring.spring_question_board.user.SiteUser;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    Page<Answer> findAll(Pageable pageable);
    Page<Answer> findAll(Specification<Answer> spec, Pageable pageable);
    Page<Answer> findByQuestion(Question question, Pageable pageable);
    Page<Answer> findByAuthor(SiteUser siteUser, Pageable pageable);
}
