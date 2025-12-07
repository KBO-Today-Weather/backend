package kbo.today.domain.user.usecase.impl;

import kbo.today.common.exception.DuplicateEmailException;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.CreateUserCommand;
import kbo.today.domain.user.usecase.CreateUserUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CreateUserInteractor implements CreateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserInteractor(UserRepositoryPort userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(CreateUserCommand command) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(command.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + command.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(command.getPassword());
        User user = User.create(
            command.getEmail(),
            encodedPassword,
            command.getNickname()
        );
        return userRepository.save(user);
    }

}
