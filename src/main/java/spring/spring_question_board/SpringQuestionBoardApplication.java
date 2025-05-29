package spring.spring_question_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:secrets.properties")
@SpringBootApplication
public class SpringQuestionBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringQuestionBoardApplication.class, args);
	}

}
