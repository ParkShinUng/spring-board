package spring.spring_question_board.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    Page<Question> findAll(Pageable pageable);
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    // 직접 쿼리 작성 방식
//    @Query("select "
//            + "distinct q "
//            + "from Question q"
//            + "left outer join SiteUser u1 on q.author=u1"
//            + "left outer join Answer a on a.question=q"
//            + "left outer join SiteUser u2 on a.author=u2"
//            + "where"
//            + "    q.subject like %:keyword% "
//            + "    or q.content like %:keyword% "
//            + "    or u1.username like %:keyword% "
//            + "    or a.content like %:keyword% "
//            + "    or u2.username like %:keyword% ")
//    Page<Question> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
