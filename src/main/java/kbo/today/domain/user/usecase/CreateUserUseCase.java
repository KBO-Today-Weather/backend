package kbo.today.domain.user.usecase;

import kbo.today.domain.user.domain.User;

public interface CreateUserUseCase {
    User create(CreateUserCommand command);
}
