package savemyreceipt.server.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import savemyreceipt.server.domain.Receipt;

import java.util.List;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> findByGroupId(Long groupId);

    List<Receipt> findByUserId(Long userId);
}
