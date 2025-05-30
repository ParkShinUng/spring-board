package spring.spring_question_board.question;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import spring.spring_question_board.answer.Answer;
import spring.spring_question_board.category.Category;
import spring.spring_question_board.user.SiteUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
    // 일반적으로 엔티티 생성 시에는 Setter 메서드 미사용 하는 것을 권장.
    // why?) DB와 바로 연결되어 데이터를 자유롭게 변경할 수 있는 Setter 메서드를 허용하는 것은 안전하지 않기 때문.
    // 생성자에 의해서만 엔티티의 값을 저장할 수 있게 하고, 데이터 변경은 메서드를 추가로 작성하면 됨.
    // 추후 수정 필요

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;

    @ManyToOne
    private Category category;

    @Column(columnDefinition = "integer default 0")
    @NotNull
    private Integer viewCount = 0;
}
