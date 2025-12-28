package kbo.today.domain.user.usecase.impl;

import kbo.today.common.exception.InvalidCredentialsException;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.port.JwtTokenPort;
import kbo.today.domain.user.port.PasswordEncoderPort;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.LoginCommand;
import kbo.today.domain.user.usecase.LoginUseCase;

public class LoginInteractor implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public LoginInteractor(
        UserRepositoryPort userRepository,
        PasswordEncoderPort passwordEncoder,
        JwtTokenPort jwtTokenPort
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    public String login(LoginCommand command) {
        User user = userRepository.findByEmail(command.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return jwtTokenPort.generateToken(
            user.getId(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}

