package savemyreceipt.server.DTO.user.response;

import lombok.Builder;
import lombok.Getter;
import savemyreceipt.server.Enums.Authority;

@Getter
@Builder
public class UserDetailResponseDto {

    private String email;
    private String name;
    private Authority authority;
}
