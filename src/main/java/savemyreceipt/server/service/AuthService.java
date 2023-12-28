package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import savemyreceipt.server.DTO.auth.TokenDto;
import savemyreceipt.server.DTO.auth.request.LoginRequestDto;
import savemyreceipt.server.DTO.auth.request.SignUpRequestDto;
import savemyreceipt.server.DTO.auth.response.UserResponseDto;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.CustomException;
import savemyreceipt.server.exception.model.NotFoundException;
import savemyreceipt.server.infrastructure.UserRepository;
import savemyreceipt.server.jwt.TokenProvider;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public UserResponseDto signup(SignUpRequestDto signUpRequestDto) {

        log.info("signUpRequestDto: {}", signUpRequestDto);
        // 핸드폰번호가 존재하거나 아이디가 존재하면 에러
        // 핸드폰 번호 또는 아이디가 이미 존재하는지 확인
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new NotFoundException(ErrorStatus.ALREADY_EXISTING_EMAIL,
                ErrorStatus.ALREADY_EXISTING_EMAIL.getMessage());
        }

        User user = signUpRequestDto.toUser(passwordEncoder);
        userRepository.save(user);
        return UserResponseDto.builder()
            .email(user.getEmail())
            .name(user.getName())
            .build();
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        log.info("loginRequestDto: {}", loginRequestDto);

        try {
            // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
            UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
            log.info("authenticationToken: {}", authenticationToken);

            // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
            //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
            log.info("authentication: {}", authentication);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
            log.info("tokenDto: {}", tokenDto);

            // 4. RefreshToken 저장
            redisTemplate.opsForValue().set("RefreshToken:" + authentication.getName(),
                tokenDto.getRefreshToken(),
                tokenProvider.getRefreshTokenExpireTime(),
                TimeUnit.MILLISECONDS);
            log.info("redisTemplate: {}", redisTemplate);

            return tokenDto;
        } catch (AuthenticationException e) {
            throw new NotFoundException(ErrorStatus.WRONG_LOGIN_INFO,
                ErrorStatus.WRONG_LOGIN_INFO.getMessage());
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.INTERNAL_SERVER_ERROR,
                ErrorStatus.INTERNAL_SERVER_ERROR.getMessage());
        }
    }
}
