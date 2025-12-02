package kbo.today.domain.user.usecase.impl;

import kbo.today.adapter.out.security.JwtTokenService;
import kbo.today.common.exception.InvalidCredentialsException;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.LoginCommand;
import kbo.today.domain.user.usecase.LoginUseCase;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginInteractor implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public LoginInteractor(
        UserRepositoryPort userRepository,
        PasswordEncoder passwordEncoder,
        JwtTokenService jwtTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public String login(LoginCommand command) {
        User user = userRepository.findByEmail(command.getEmail())
            .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(command.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return jwtTokenService.generateToken(
            user.getId(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}

