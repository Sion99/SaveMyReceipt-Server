package savemyreceipt.server.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import savemyreceipt.server.domain.Group;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.NotFoundException;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByNameContaining(String keyword);

    Optional<Group> findById(Long groupId);

    default Group getGroupById(Long groupId) {
        return this.findById(groupId).orElseThrow(
            () -> new NotFoundException(ErrorStatus.GROUP_NOT_FOUND,
                ErrorStatus.GROUP_NOT_FOUND.getMessage()));
    }
}
