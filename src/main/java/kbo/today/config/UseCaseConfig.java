package kbo.today.config;

import kbo.today.adapter.out.security.JwtTokenService;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.stadium.usecase.GetStadiumUseCase;
import kbo.today.domain.stadium.usecase.impl.GetStadiumInteractor;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.CreateUserUseCase;
import kbo.today.domain.user.usecase.LoginUseCase;
import kbo.today.domain.user.usecase.impl.CreateUserInteractor;
import kbo.today.domain.user.usecase.impl.LoginInteractor;
import kbo.today.domain.weather.port.WeatherApiPort;
import kbo.today.domain.weather.usecase.GetStadiumWeatherUseCase;
import kbo.today.domain.weather.usecase.impl.GetStadiumWeatherInteractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
        UserRepositoryPort userRepositoryPort,
        PasswordEncoder passwordEncoder
    ) {
        return new CreateUserInteractor(userRepositoryPort, passwordEncoder);
    }

    @Bean
    public LoginUseCase loginUseCase(
        UserRepositoryPort userRepositoryPort,
        PasswordEncoder passwordEncoder,
        JwtTokenService jwtTokenService
    ) {
        return new LoginInteractor(userRepositoryPort, passwordEncoder, jwtTokenService);
    }

    @Bean
    public GetStadiumUseCase getStadiumUseCase(StadiumRepositoryPort stadiumRepositoryPort) {
        return new GetStadiumInteractor(stadiumRepositoryPort);
    }

    @Bean
    public GetStadiumWeatherUseCase getStadiumWeatherUseCase(
        StadiumRepositoryPort stadiumRepositoryPort,
        WeatherApiPort weatherApiPort
    ) {
        return new GetStadiumWeatherInteractor(stadiumRepositoryPort, weatherApiPort);
    }
}
