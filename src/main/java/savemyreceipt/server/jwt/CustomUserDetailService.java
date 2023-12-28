package savemyreceipt.server.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import savemyreceipt.server.infrastructure.UserRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
    }

    // DB에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(savemyreceipt.server.domain.User user) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
            user.getAuthority().toString());

        return new User(
            user.getEmail(),
            user.getPassword(),
            Collections.singleton(grantedAuthority)
        );
    }
}
