package spring.spring_question_board;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.spring_question_board.category.Category;
import spring.spring_question_board.category.CategoryService;
import spring.spring_question_board.question.QuestionRepository;
import spring.spring_question_board.question.QuestionService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringQuestionBoardApplicationTests {

	@Autowired
	private QuestionService questionService;

//	@Autowired
//	private CategoryService categoryService;

	@Test
	void testJpa() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("This is Test Subject:[%03d]", i);
			String content = "Test Content";
			this.questionService.create(subject, content, null, null);
		}
	}

	@Test
	void testCreateCategoryQuestion() {
		String subject = String.format("This is Test Subject - [%s]", LocalDateTime.now());
		String content = String.format("Test Content - [%s]", LocalDateTime.now());
		Category category = new Category();
		category.setName("Board2");

		this.questionService.create(subject, content, category, null);
	}

}
