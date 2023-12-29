package savemyreceipt.server.DTO.receipt.response;

import lombok.Builder;
import lombok.Getter;
import savemyreceipt.server.domain.Receipt;

import java.time.LocalDate;

@Getter
@Builder
public class ReceiptResponseDto {

    private Long id;
    private String imageUri;
    private String category;
    private String description;
    private Long price;
    private LocalDate purchaseDate;

    public static ReceiptResponseDto convertToDto(Receipt receipt) {
        return ReceiptResponseDto.builder()
            .id(receipt.getId())
            .imageUri(receipt.getImageUri())
            .category(receipt.getCategory())
            .description(receipt.getDescription())
            .price(receipt.getPrice())
            .purchaseDate(receipt.getPurchaseDate())
            .build();
    }
}
