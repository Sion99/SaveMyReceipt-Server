package savemyreceipt.server.oauth2.dto;

import savemyreceipt.server.domain.User;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getOAuth2Id() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    public User toEntity() {
        return User.builder()
            .name("임시 사용자")
            .email(this.getEmail())
            .oauth2Id(this.getOAuth2Id())
            .build();
    }
}
