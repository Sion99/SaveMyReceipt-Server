package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import savemyreceipt.server.DTO.group.response.GroupResponseDto;
import savemyreceipt.server.domain.Group;
import savemyreceipt.server.infrastructure.GroupRepository;
import savemyreceipt.server.infrastructure.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupResponseDto getGroup(String username) {
        Group group = userRepository.getUserByEmail(username).getGroup();
        return GroupResponseDto.convertToDto(group);
    }

    public List<GroupResponseDto> searchGroup(String username, String keyword) {
        List<Group> groups = groupRepository.findByNameContaining(keyword);
        return groups.stream()
            .map(GroupResponseDto::convertToDto)
            .collect(Collectors.toList());
    }
}
