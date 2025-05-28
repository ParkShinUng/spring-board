package spring.spring_question_board.category;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import spring.spring_question_board.question.Question;

import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Question> categoryQuestionList;
}
