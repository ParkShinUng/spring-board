package spring.spring_question_board.question;

import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.spring_question_board.DataNotFoundException;
import spring.spring_question_board.answer.Answer;
import spring.spring_question_board.answer.AnswerRepository;
import spring.spring_question_board.user.SiteUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public Specification<Question> search(String keyword) {
        return new Specification<>() {
            private static final long serialVersionID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> questionRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);       // 중복 제거
                Join<Question, SiteUser> u1 = questionRoot.join("author", JoinType.LEFT);
                Join<Question, Answer> a = questionRoot.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(questionRoot.get("subject"), "%" + keyword + "%"),     // 제목
                        cb.like(questionRoot.get("content"), "%" + keyword + "%"),          // 내용
                        cb.like(u1.get("username"), "%" + keyword + "%"),           // 질문 작성자
                        cb.like(a.get("content"), "%" + keyword + "%"),             // 답변 내용
                        cb.like(u2.get("username"), "%" + keyword + "%"));          // 답변 작성자
            }
        };
    }

    public Page<Question> getList(int page, String keyword) {
        List<Sort.Order> sortList = new ArrayList<>();
        sortList.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortList));
        Specification<Question> spec = search(keyword);
        return this.questionRepository.findAll(spec, pageable);
//        return this.questionRepository.findAllByKeyword(keyword, pageable);       // 직접 쿼리 작성 방식
    }

    public Page<Answer> getList(int page) {
        List<Sort.Order> sortList = new ArrayList<>();
        sortList.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortList));
        return this.answerRepository.findAll(pageable);
    }

    public List<Question> getList() {
        return this.questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);

        this.questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());

        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

}
