package spring.spring_question_board;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.spring_question_board.question.QuestionRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringQuestionBoardApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Transactional
	@Test
	void testJpa() {

	}

}
