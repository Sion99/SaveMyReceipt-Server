package savemyreceipt.server.DTO.receipt;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReceiptUpdateRequestDto {

    private Long id;

    private String category;

    private String description;

    private String note;

    private Long price;

    private LocalDate purchaseDate;

    public ReceiptUpdateRequestDto(Long id, String category, String description, String note, Long price) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.note = note;
        this.price = price;
    }
}
