package savemyreceipt.server.DTO.group.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import savemyreceipt.server.domain.Group;

@Getter
@AllArgsConstructor
public class GroupRequestDto {

    private String name;
    private String department;
    private String description;

    public Group toEntity() {
        return Group.builder()
            .name(this.name)
            .department(this.department)
            .description(this.description)
            .build();
    }
}
