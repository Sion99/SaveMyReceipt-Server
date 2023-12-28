package savemyreceipt.server.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import savemyreceipt.server.Enums.Authority;
import savemyreceipt.server.common.domain.AuditingTimeEntity;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    private String oauth2Id;

    private String spreadSheetToken;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String email, String name, String password, String oauth2Id, String spreadSheetToken,
                Authority authority) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.oauth2Id = oauth2Id;
        this.spreadSheetToken = spreadSheetToken;
        this.authority = authority;
    }

    public void changeAuthority(Authority authority) {
        this.authority = authority;
    }
}
