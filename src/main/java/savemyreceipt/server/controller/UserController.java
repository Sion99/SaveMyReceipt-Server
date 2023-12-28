package savemyreceipt.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.DTO.user.response.UserDetailResponseDto;
import savemyreceipt.server.exception.SuccessStatus;
import savemyreceipt.server.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponseDto<UserDetailResponseDto> getUserDetail(@Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_USER_DETAIL_SUCCESS, userService.getUserDetail(user.getUsername()));
    }

}
