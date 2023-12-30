package savemyreceipt.server.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import savemyreceipt.server.domain.Receipt;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.NotFoundException;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findByGroupId(Long groupId);

    List<Receipt> findByUserId(Long userId);

    Optional<Receipt> findById(Long id);

    default Receipt getReceiptById(Long id) {
        return this.findById(id).orElseThrow(
            () -> new NotFoundException(ErrorStatus.RECEIPT_NOT_FOUND,
                ErrorStatus.RECEIPT_NOT_FOUND.getMessage())
        );
    }
}
