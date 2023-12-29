package savemyreceipt.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.DTO.group.request.GroupRequestDto;
import savemyreceipt.server.DTO.group.response.GroupResponseDto;
import savemyreceipt.server.DTO.user.response.UserDetailResponseDto;
import savemyreceipt.server.exception.SuccessStatus;
import savemyreceipt.server.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Admin", description = "관리자 관련 API")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/user")
    public ApiResponseDto<List<UserDetailResponseDto>> getUserList(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_USER_DETAIL_SUCCESS, adminService.getUserList(user.getUsername()));
    }

    @PostMapping("/create-group")
    public ApiResponseDto<GroupResponseDto> createGroup(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestBody GroupRequestDto groupRequestDto) {
        return ApiResponseDto.success(SuccessStatus.CREATE_GROUP_SUCCESS, adminService.createGroup(user.getUsername(), groupRequestDto));
    }
}
