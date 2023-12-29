package savemyreceipt.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import savemyreceipt.server.common.domain.AuditingTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Receipt extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long id;

    // 영수증 원본 주소
    private String imageUri;

    // 항목
    private String category;

    // 세부내용
    private String description;

    // 기타내용, 비고
    private String note;

    // 거래날짜
    private LocalDate purchaseDate;

    // 가격
    private Long price;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Receipt(String imageUri, String category, String description, String note, LocalDate purchaseDate, Long price,
                   User user, Group group) {
        this.imageUri = imageUri;
        this.category = category;
        this.description = description;
        this.note = note;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.user = user;
        this.group = group;
    }
}
