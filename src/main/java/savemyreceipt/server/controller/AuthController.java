package savemyreceipt.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.DTO.auth.TokenDto;
import savemyreceipt.server.DTO.auth.request.LoginRequestDto;
import savemyreceipt.server.DTO.auth.request.SignUpRequestDto;
import savemyreceipt.server.DTO.auth.response.UserResponseDto;
import savemyreceipt.server.exception.SuccessStatus;
import savemyreceipt.server.service.AuthService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponseDto<UserResponseDto> signup(@RequestBody SignUpRequestDto signUpRequestDto) {
        return ApiResponseDto.success(SuccessStatus.SIGNUP_SUCCESS, authService.signup(signUpRequestDto));
    }

    @PostMapping("/login")
    public ApiResponseDto<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ApiResponseDto.success(SuccessStatus.LOGIN_SUCCESS, authService.login(loginRequestDto));
    }

    @PostMapping("/reissue")
    public ApiResponseDto<TokenDto> reissue(@Parameter(hidden = true) HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        return ApiResponseDto.success(SuccessStatus.REISSUE_SUCCESS, authService.reissue(refreshToken));
    }
}
