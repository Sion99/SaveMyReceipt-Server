package savemyreceipt.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.DTO.group.response.GroupResponseDto;
import savemyreceipt.server.exception.SuccessStatus;
import savemyreceipt.server.service.GroupService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Group", description = "그룹(교회) 관련 API")
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public ApiResponseDto<GroupResponseDto> getGroup(@Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_SUCCESS, groupService.getGroup(user.getUsername()));
    }

    @GetMapping("/search")
    public ApiResponseDto<List<GroupResponseDto>> searchGroup(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam String keyword) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_SUCCESS, groupService.searchGroup(user.getUsername(), keyword));
    }
}
