package spring.spring_question_board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    @GetMapping("/sqb")
    @ResponseBody
    public String index() {
        return "Hello. Welcome to SQB";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/question/list";
    }

}
