package savemyreceipt.server.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import savemyreceipt.server.domain.User;
import savemyreceipt.server.exception.ErrorStatus;
import savemyreceipt.server.exception.model.NotFoundException;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauth2Id(String oauth2Id);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    default User getUserByEmail(String email) {
        return this.findByEmail(email).orElseThrow(
            () -> new NotFoundException(ErrorStatus.USER_NOT_FOUND,
                ErrorStatus.USER_NOT_FOUND.getMessage()));
    }
}
