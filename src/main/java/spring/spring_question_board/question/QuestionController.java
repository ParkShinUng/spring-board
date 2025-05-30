package spring.spring_question_board.question;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import spring.spring_question_board.answer.Answer;
import spring.spring_question_board.answer.AnswerForm;
import spring.spring_question_board.answer.AnswerService;
import spring.spring_question_board.category.Category;
import spring.spring_question_board.category.CategoryService;
import spring.spring_question_board.user.SiteUser;
import spring.spring_question_board.user.UserService;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        Page<Question> paging = this.questionService.getPagingQuestionList(page, keyword);
        List<Category> categoryList = this.categoryService.getList();

        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category_list", categoryList);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         AnswerForm answerForm) {
        this.questionService.viewUp(id);
        Question question = this.questionService.getQuestion(id);
        Page<Answer> paging = this.answerService.getListByQuestion(page, question);
        List<Category> categoryList = this.categoryService.getList();

        model.addAttribute("paging", paging);
        model.addAttribute("question", question);
        model.addAttribute("category_list", categoryList);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm, Model model) {
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("category_list", categoryList);
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            System.out.println("Question Create Error");
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Category category = this.categoryService.getCategoryByName(questionForm.getCategory());
        this.questionService.create(questionForm.getSubject(), questionForm.getContent(), category, siteUser);

        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id,
                                 Principal principal, Model model) {
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("category_list", categoryList);

        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
                                 @PathVariable("id") Integer id, Model model) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("category_list", categoryList);

        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id, Model model) {
        Question question = this.questionService.getQuestion(id);
        System.out.println(question.getAuthor().getUsername());
        System.out.println(principal.getName());
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("category_list", categoryList);

        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id, Model model) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        List<Category> categoryList = this.categoryService.getList();
        model.addAttribute("category_list", categoryList);

        this.questionService.vote(question, siteUser);
        return String.format("redirect:/question/detail/%s", id);
    }
}
