package spring.spring_question_board.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.spring_question_board.DataNotFoundException;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }

    // Social Login overloading
    public SiteUser create(String registrationId, String userName,
                           String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRegisterId(registrationId);
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    public SiteUser getUserByEmail(String email) {
        Optional<SiteUser> siteUser = this.userRepository.findByEmail(email);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser by email not found");
        }
    }

    public void updatePassword(SiteUser siteUser, String newPassword) {
        siteUser.setPassword(this.passwordEncoder.encode(newPassword));
        this.userRepository.save(siteUser);
    }

    public boolean isMatch(String rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public SiteUser socialLogin(String registrationId, String username, String email) {
        Optional<SiteUser> siteUser = this.userRepository.findByEmail(email);
        return siteUser.orElseGet(() -> this.create(registrationId, username, email, ""));
    }
}
