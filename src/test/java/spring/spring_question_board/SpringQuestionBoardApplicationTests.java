package spring.spring_question_board;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.spring_question_board.category.Category;
import spring.spring_question_board.category.CategoryService;
import spring.spring_question_board.question.QuestionRepository;
import spring.spring_question_board.question.QuestionService;
import spring.spring_question_board.user.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringQuestionBoardApplicationTests {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Transactional
	@Test
	void testJpa() {
//		for (int i = 1; i <= 300; i++) {
//			String subject = String.format("This is Test Subject:[%03d]", i);
//			String content = "Test Content";
//			this.questionService.create(subject, content, null, null);
//		}
	}

	@Transactional
	@Test
	void testCreateCategoryQuestion() {
//		String subject = String.format("This is Test Subject - [%s]", LocalDateTime.now());
//		String content = String.format("Test Content - [%s]", LocalDateTime.now());
//
//		this.categoryService.create("게시판1");
//		this.questionService.create(subject, content, this.categoryService.getCategoryByName("게시판1"), this.userService.getUser("tlsdnd001"));
	}

}
