package savemyreceipt.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import savemyreceipt.server.service.UserService;

import java.util.Collections;
import java.util.Map;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User user) {
        return Collections.singletonMap("name", user.getAttribute("name"));
    }
}
