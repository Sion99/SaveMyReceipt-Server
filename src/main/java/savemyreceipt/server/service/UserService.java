package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import savemyreceipt.server.DTO.receipt.response.ReceiptResponseDto;
import savemyreceipt.server.DTO.user.response.UserDetailResponseDto;
import savemyreceipt.server.domain.Receipt;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.infrastructure.ReceiptRepository;
import savemyreceipt.server.infrastructure.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReceiptRepository receiptRepository;

    public UserDetailResponseDto getUserDetail(String email) {
        User user = userRepository.getUserByEmail(email);
        return UserDetailResponseDto.convertToDto(user);
    }

    public List<ReceiptResponseDto> getUserReceipts(String email) {
        User user = userRepository.getUserByEmail(email);
        List<Receipt> receipts = receiptRepository.findByUserId(user.getId());
        return receipts.stream()
            .map(ReceiptResponseDto::convertToDto)
            .collect(Collectors.toList());
    }
}
