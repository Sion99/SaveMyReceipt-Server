package savemyreceipt.server.util.gcp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptInfo {
    @JsonProperty("purchase_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @JsonProperty("total_price")
    private Long totalPrice;

    @Builder
    public ReceiptInfo(LocalDate purchaseDate, Long totalPrice) {
        this.purchaseDate = purchaseDate;
        this.totalPrice = totalPrice;
    }
}
