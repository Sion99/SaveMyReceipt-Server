package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import savemyreceipt.server.DTO.group.request.GroupRequestDto;
import savemyreceipt.server.DTO.group.response.GroupResponseDto;
import savemyreceipt.server.DTO.receipt.response.ReceiptResponseDto;
import savemyreceipt.server.DTO.user.response.UserDetailResponseDto;
import savemyreceipt.server.Enums.Authority;
import savemyreceipt.server.domain.Group;
import savemyreceipt.server.domain.Receipt;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.CustomException;
import savemyreceipt.server.infrastructure.GroupRepository;
import savemyreceipt.server.infrastructure.ReceiptRepository;
import savemyreceipt.server.infrastructure.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ReceiptRepository receiptRepository;

    public GroupResponseDto createGroup(String username, GroupRequestDto groupRequestDto) {
        checkAdmin(username);
        return GroupResponseDto.convertToDto(groupRepository.save(groupRequestDto.toEntity()));
    }

    public List<UserDetailResponseDto> getUserList(String username) {
        checkAdmin(username);
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(UserDetailResponseDto::convertToDto)
            .collect(Collectors.toList());
    }

    public List<GroupResponseDto> getGroupList(String username) {
        checkAdmin(username);
        List<Group> groups = groupRepository.findAll();
        return groups.stream()
            .map(GroupResponseDto::convertToDto)
            .collect(Collectors.toList());
    }

    public List<ReceiptResponseDto> getReceiptList(String username) {
        checkAdmin(username);
        List<Receipt> receipts = receiptRepository.findAll();
        return receipts.stream()
            .map(ReceiptResponseDto::convertToDto)
            .collect(Collectors.toList());
    }

    public void checkAdmin(String username) {
        User user = userRepository.getUserByEmail(username);
        if (!user.getAuthority().equals(Authority.ROLE_ADMIN)) {
            throw new CustomException(ErrorStatus.NOT_ADMIN, ErrorStatus.NOT_ADMIN.getMessage());
        }
    }
}
