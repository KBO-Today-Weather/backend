package kbo.today.domain.user.port;

import java.util.Optional;
import kbo.today.domain.user.domain.User;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}
