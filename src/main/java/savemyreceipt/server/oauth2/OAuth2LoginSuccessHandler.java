package savemyreceipt.server.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.DTO.auth.TokenDto;
import savemyreceipt.server.Enums.Authority;
import savemyreceipt.server.exception.SuccessStatus;
import savemyreceipt.server.jwt.TokenProvider;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인에 성공했습니다.");
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        redisTemplate.opsForValue().set("RefreshToken:" + authentication.getName(),
            tokenDto.getRefreshToken(),
            tokenProvider.getRefreshTokenExpireTime());
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(mapper.writeValueAsString(
            ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, tokenDto)
        ));

    }
}
