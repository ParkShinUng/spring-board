package spring.spring_question_board.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.spring_question_board.question.Question;
import spring.spring_question_board.question.QuestionService;

import java.util.List;

@RequestMapping("/category")
@RequiredArgsConstructor
@Controller
public class CategoryController {
    private final CategoryService categoryService;
    private final QuestionService questionService;

    @GetMapping("/{category}")
    public String categoryQuestionList(Model model, @PathVariable("category") String categoryName,
                                       @RequestParam(value="page", defaultValue="0") int page) {
        Category category = this.categoryService.getCategoryByName(categoryName);
        List<Category> categoryList = this.categoryService.getList();
        Page<Question> paging = this.questionService.getPagingCategoryQuestionList(category, page);

        model.addAttribute("category_name", categoryName);
        model.addAttribute("category_list", categoryList);
        model.addAttribute("paging", paging);
        return "category_question_list";
    }
}
