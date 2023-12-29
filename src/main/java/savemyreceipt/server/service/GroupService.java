package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import savemyreceipt.server.DTO.group.response.GroupResponseDto;
import savemyreceipt.server.DTO.receipt.response.ReceiptResponseDto;
import savemyreceipt.server.DTO.user.response.UserDetailResponseDto;
import savemyreceipt.server.domain.Group;
import savemyreceipt.server.domain.Receipt;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.CustomException;
import savemyreceipt.server.exception.model.NotFoundException;
import savemyreceipt.server.infrastructure.GroupRepository;
import savemyreceipt.server.infrastructure.ReceiptRepository;
import savemyreceipt.server.infrastructure.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final ReceiptRepository receiptRepository;

    public GroupResponseDto getGroup(String username) {
        Group group = userRepository.getUserByEmail(username).getGroup();
        if (group == null) {
            throw new NotFoundException(ErrorStatus.GROUP_NOT_FOUND, ErrorStatus.GROUP_NOT_FOUND.getMessage());
        }
        return GroupResponseDto.convertToDto(group);
    }

    @Transactional
    public GroupResponseDto joinGroup(String username, Long groupId) {
        Group group = groupRepository.getGroupById(groupId);
        User user = userRepository.getUserByEmail(username);
        if (user.getGroup() != null) {
            throw new CustomException(ErrorStatus.ALREADY_IN_GROUP, ErrorStatus.ALREADY_IN_GROUP.getMessage());
        }
        user.joinGroup(group);
        return GroupResponseDto.convertToDto(group);
    }

    public List<GroupResponseDto> searchGroup(String username, String keyword) {
        List<Group> groups = groupRepository.findByNameContaining(keyword);
        return groups.stream()
            .map(GroupResponseDto::convertToDto)
            .collect(Collectors.toList());
    }

    public List<UserDetailResponseDto> getGroupMembers(String username) {
        User user = userRepository.getUserByEmail(username);
        Group group = user.getGroup();
        if (group == null) {
            throw new NotFoundException(ErrorStatus.GROUP_NOT_FOUND, ErrorStatus.GROUP_NOT_FOUND.getMessage());
        }
        List<User> users = userRepository.findByGroupId(group.getId());
        return users.stream()
            .map(UserDetailResponseDto::convertToDto)
            .collect(Collectors.toList());
    }

    public List<ReceiptResponseDto> getGroupReceipts(String username) {
        User user = userRepository.getUserByEmail(username);
        Group group = user.getGroup();
        if (group == null) {
            throw new NotFoundException(ErrorStatus.GROUP_NOT_FOUND, ErrorStatus.GROUP_NOT_FOUND.getMessage());
        }
        List<Receipt> receipts = receiptRepository.findByGroupId(group.getId());
        return receipts.stream()
            .map(ReceiptResponseDto::convertToDto)
            .collect(Collectors.toList());
    }
}
