package savemyreceipt.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import savemyreceipt.server.DTO.user.response.UserDetailResponseDto;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.infrastructure.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDetailResponseDto getUserDetail(String email) {
        User user = userRepository.getUserByEmail(email);
        return UserDetailResponseDto.builder()
            .email(user.getEmail())
            .name(user.getName())
            .authority(user.getAuthority())
            .build();
    }
}
