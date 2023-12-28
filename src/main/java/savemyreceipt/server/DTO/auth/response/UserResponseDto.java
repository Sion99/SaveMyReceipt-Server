package savemyreceipt.server.DTO.auth.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String email;
    private final String name;

    @Builder
    public UserResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
