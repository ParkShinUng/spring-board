package spring.spring_question_board.answer;

import groovyjarjarpicocli.CommandLine;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import spring.spring_question_board.CommonUtil;
import spring.spring_question_board.DataNotFoundException;
import spring.spring_question_board.question.Question;
import spring.spring_question_board.user.SiteUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Specification<Answer> hasVoter(SiteUser siteUser) {
        return new Specification<Answer>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Answer> aRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                return cb.isMember(siteUser, aRoot.get("voter"));
            }
        };
    }

    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);

        return answer;
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("Answer not found");
        }
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);
        this.answerRepository.save(answer);
    }

    public Page<Answer> getListByQuestion(int page, Question question) {
        Pageable pageable = CommonUtil.getPageable(page, 5,"createDate");
        return this.answerRepository.findByQuestion(question, pageable);
    }

    public Page<Answer> getListByAuthor(int page, SiteUser siteUser) {
        Pageable pageable = CommonUtil.getPageable(page, 5, "createDate");
        return this.answerRepository.findByAuthor(siteUser, pageable);
    }

    public Page<Answer> getListByVoter(int page, SiteUser siteUser) {
        Pageable pageable = CommonUtil.getPageable(page, 5, "createDate");
        Specification<Answer> spec = this.hasVoter(siteUser);
        return this.answerRepository.findAll(spec, pageable);
    }

}
