package savemyreceipt.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import savemyreceipt.server.DTO.ApiResponseDto;
import savemyreceipt.server.DTO.receipt.ReceiptUpdateRequestDto;
import savemyreceipt.server.exception.SuccessStatus;
import savemyreceipt.server.service.ReceiptService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/receipt")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Receipt", description = "영수증 관련 API")
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping("/upload")
    public ApiResponseDto<String> upload(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam(name = "image") MultipartFile image) throws IOException {
        return ApiResponseDto.success(SuccessStatus.IMAGE_UPLOAD_SUCCESS, receiptService.upload(user.getUsername(), image));
    }

    @PostMapping("/update")
    public ApiResponseDto<?> update(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestBody ReceiptUpdateRequestDto receiptUpdateRequestDto) {
        receiptService.update(user.getUsername(), receiptUpdateRequestDto);
        return ApiResponseDto.success(SuccessStatus.RECEIPT_INFO_UPDATE_SUCCESS, SuccessStatus.RECEIPT_INFO_UPDATE_SUCCESS.getMessage());
    }
}
