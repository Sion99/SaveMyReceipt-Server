package savemyreceipt.server.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import savemyreceipt.server.Enums.Authority;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.infrastructure.UserRepository;
import savemyreceipt.server.oauth2.dto.GoogleOAuth2UserInfo;
import savemyreceipt.server.oauth2.dto.OAuth2UserInfo;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    /**
     * DefaultOAuth2UserService 객체를 생성하여, loadUser(userRequest)를 통해 DefaultOAuth2User 객체를 생성 후 반환
     * DefaultOAuth2UserService의 loadUser()는 소셜 로그인 API의 사용자 정보 제공 URI로 요청을 보내서
     * 사용자 정보를 얻은 후, 이를 통해 DefaultOAuth2User 객체를 생성 후 반환한다.
     * 결과적으로, OAuth2User는 OAuth 서비스에서 가져온 유저 정보를 담고 있는 유저
     */

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        log.info("userRequest: {}", userRequest);

        // Google에서 반환된 사용자 정보 추출하여 GoogleOAuth2UserInfo 객체 생성
        GoogleOAuth2UserInfo googleOAuth2UserInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        log.info("googleOAuth2UserInfo: {}", googleOAuth2UserInfo);

        // User 엔티티 생성 및 저장 또는 업데이트
        User user = getUser(googleOAuth2UserInfo);
        log.info("user: {}", user);

        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getAuthority().name())),
            oAuth2User.getAttributes(),
            "sub",
            user.getAuthority()
        );
    }

    private User getUser(GoogleOAuth2UserInfo googleOAuth2UserInfo) {
        User findUser = userRepository.findByOauth2Id(
            googleOAuth2UserInfo.getOAuth2Id()).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return registerUser(googleOAuth2UserInfo);
        }

        if (findUser == null) {
            return registerUser(googleOAuth2UserInfo);
        }

        return findUser;
    }

    private User registerUser(OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
            .email(oAuth2UserInfo.getEmail())
            .name(oAuth2UserInfo.getName())
            .oauth2Id(oAuth2UserInfo.getOAuth2Id())
            .authority(Authority.ROLE_USER)
            .build();

        return userRepository.save(user);
    }
}
