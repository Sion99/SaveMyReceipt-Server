package savemyreceipt.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.DTO.group.response.GroupResponseDto;
import savemyreceipt.server.DTO.receipt.response.ReceiptResponseDto;
import savemyreceipt.server.DTO.user.response.UserDetailResponseDto;
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

    @PostMapping("/join/{groupId}")
    public ApiResponseDto<?> joinGroup(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable("groupId") Long groupId) {
        return ApiResponseDto.success(SuccessStatus.JOIN_GROUP_SUCCESS, groupService.joinGroup(user.getUsername(), groupId));
    }

    @GetMapping("/search")
    public ApiResponseDto<List<GroupResponseDto>> searchGroup(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam String keyword) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_SUCCESS, groupService.searchGroup(user.getUsername(), keyword));
    }

    @GetMapping("/members")
    public ApiResponseDto<List<UserDetailResponseDto>> getGroupMembers(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_MEMBER_SUCCESS, groupService.getGroupMembers(user.getUsername()));
    }

    @GetMapping("/receipts")
    public ApiResponseDto<List<ReceiptResponseDto>> getGroupReceipts(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_RECEIPT_SUCCESS, groupService.getGroupReceipts(user.getUsername()));
    }

}
