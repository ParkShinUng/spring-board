package spring.spring_question_board.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.spring_question_board.DataNotFoundException;

import java.security.SecureRandom;
import java.util.Random;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final JavaMailSender javaMailSender;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signup_form";
        }

        if (!userCreateForm.getPassword().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
            return "signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword());
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "/login_form";
    }

    @GetMapping("/find-account")
    public String findAccount(Model model) {
        model.addAttribute("sendConfirm", false);
        model.addAttribute("error", false);
        return "/find_account_form";
    }

    @PostMapping("/find-account")
    public String findAccount(Model model, @RequestParam(value="email") String email) {
        try {
            SiteUser siteUser = this.userService.getUserByEmail(email);

            String newPassword = TempPasswordGenerator.generateTempPassword();
            String mailContent = String.format("%s 님의 계정 비밀번호가 임시 비밀번호로 초기화 되었습니다.\n " +
                    "임시 비밀번호는 %s 입니다.\n 로그인 후 새로운 비밀번호로 변경하시기 바랍니다.",
                    siteUser.getUsername(), newPassword);

            StringBuffer sb = new StringBuffer();
            sb.append(mailContent);

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("[Board] 임시 비밀번호 발급 정보");
            simpleMailMessage.setText(sb.toString());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    javaMailSender.send(simpleMailMessage);
                }
            }).start();

            model.addAttribute("sendConfirm", true);
            model.addAttribute("userEmail", email);
            model.addAttribute("error", false);

            this.userService.updatePassword(siteUser, newPassword);
        } catch (DataNotFoundException e) {
            model.addAttribute("sendConfirm", false);
            model.addAttribute("error", false);
        }

        return "find_account";
    }

    public static class TempPasswordGenerator {
        private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        private static final String NUMBER = "0123456789";
        private static final String OTHER_CHAR = "!@#$%^&*()_+=[]?";

        private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
        private static final int PASSWORD_LENGTH = 6;

        public static String generateTempPassword() {
            if (PASSWORD_LENGTH < 1) throw new IllegalArgumentException("Password length must be at least 1");

            StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
            Random random = new SecureRandom();
            for (int num = 0; num < PASSWORD_LENGTH; num++) {
                int randomCharAt = random.nextInt(PASSWORD_ALLOW_BASE.length());
                char randomChar = PASSWORD_ALLOW_BASE.charAt(randomCharAt);
                sb.append(randomChar);
            }

            return sb.toString();
        }
    }
}
