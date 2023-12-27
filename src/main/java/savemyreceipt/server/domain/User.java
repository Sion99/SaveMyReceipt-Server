package savemyreceipt.server.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import savemyreceipt.server.Enums.Authority;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String oauth2Id;

    private String spreadSheetToken;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String email, String name, String oauth2Id, String spreadSheetToken,
                Authority authority) {
        this.email = email;
        this.name = name;
        this.oauth2Id = oauth2Id;
        this.spreadSheetToken = spreadSheetToken;
        this.authority = authority;
    }

    public void changeAuthority(Authority authority) {
        this.authority = authority;
    }
}