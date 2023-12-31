package savemyreceipt.server.DTO.user.response;

import lombok.Builder;
import lombok.Getter;
import savemyreceipt.server.Enums.Authority;
import savemyreceipt.server.domain.User;

@Getter
@Builder
public class UserDetailResponseDto {

    private Long id;
    private String email;
    private String name;
    private String groupName;
    private Authority authority;

    public static UserDetailResponseDto convertToDto(User user) {
        return UserDetailResponseDto.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .groupName(user.getGroup() == null ? null : user.getGroup().getName())
            .authority(user.getAuthority())
            .build();
    }
}
