package spring.spring_question_board.oauth2;

import lombok.Getter;
import spring.spring_question_board.user.SiteUser;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String username;
    private String email;
    private String password;

    public SessionUser(SiteUser user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
