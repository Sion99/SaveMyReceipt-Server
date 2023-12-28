package savemyreceipt.server.DTO.auth.request;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import savemyreceipt.server.Enums.Authority;
import savemyreceipt.server.domain.User;

@Data
public class SignUpRequestDto {

    private String email;
    private String password;
    private String name;

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
            .email(email)
            .password(passwordEncoder.encode(password))
            .name(name)
            .authority(Authority.ROLE_USER)
            .build();
    }
}
