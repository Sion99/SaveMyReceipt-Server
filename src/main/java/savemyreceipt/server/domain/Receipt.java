package savemyreceipt.server.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import savemyreceipt.server.DTO.receipt.ReceiptUpdateRequestDto;
import savemyreceipt.server.common.domain.AuditingTimeEntity;
import savemyreceipt.server.util.gcp.ReceiptInfo;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE receipt SET is_deleted = true WHERE receipt_id = ?")
@Where(clause = "is_deleted = false")
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

    private boolean isChecked = false;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

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

    public void autoFill(ReceiptInfo receiptInfo) {
        this.price = receiptInfo.getTotalPrice();
        this.purchaseDate = receiptInfo.getPurchaseDate();
    }

    public void update(ReceiptUpdateRequestDto receiptUpdateRequestDto) {
        this.category = receiptUpdateRequestDto.getCategory();
        this.description = receiptUpdateRequestDto.getDescription();
        this.note = receiptUpdateRequestDto.getNote();
        this.price = receiptUpdateRequestDto.getPrice();
        check();
    }

    private void check() {
        this.isChecked = true;
    }
}
