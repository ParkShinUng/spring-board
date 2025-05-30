package spring.spring_question_board.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private String registerId;

    @Builder
    public SiteUser() { }
    public SiteUser(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
