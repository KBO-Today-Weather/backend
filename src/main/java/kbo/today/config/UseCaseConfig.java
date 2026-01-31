package kbo.today.config;

import kbo.today.domain.favorite.port.FavoriteStadiumRepositoryPort;
import kbo.today.domain.favorite.usecase.AddFavoriteStadiumUseCase;
import kbo.today.domain.favorite.usecase.DeleteFavoriteStadiumUseCase;
import kbo.today.domain.favorite.usecase.impl.AddFavoriteStadiumInteractor;
import kbo.today.domain.favorite.usecase.impl.DeleteFavoriteStadiumInteractor;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.stadium.usecase.GetStadiumUseCase;
import kbo.today.domain.stadium.usecase.impl.GetStadiumInteractor;
import kbo.today.domain.user.port.JwtTokenPort;
import kbo.today.domain.user.port.PasswordEncoderPort;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.CreateUserUseCase;
import kbo.today.domain.user.usecase.LoginUseCase;
import kbo.today.domain.user.usecase.impl.CreateUserInteractor;
import kbo.today.domain.user.usecase.impl.LoginInteractor;
import kbo.today.domain.team.port.TeamQueryPort;
import kbo.today.domain.team.usecase.GetTeamsUseCase;
import kbo.today.domain.team.usecase.impl.GetTeamsInteractor;
import kbo.today.domain.weather.port.WeatherApiPort;
import kbo.today.domain.weather.usecase.GetStadiumWeatherUseCase;
import kbo.today.domain.weather.usecase.impl.GetStadiumWeatherInteractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateUserUseCase createUserUseCase(
        UserRepositoryPort userRepositoryPort,
        PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateUserInteractor(userRepositoryPort, passwordEncoderPort);
    }

    @Bean
    public LoginUseCase loginUseCase(
        UserRepositoryPort userRepositoryPort,
        PasswordEncoderPort passwordEncoderPort,
        JwtTokenPort jwtTokenPort
    ) {
        return new LoginInteractor(userRepositoryPort, passwordEncoderPort, jwtTokenPort);
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

    @Bean
    public AddFavoriteStadiumUseCase addFavoriteStadiumUseCase(
        FavoriteStadiumRepositoryPort favoriteStadiumRepositoryPort,
        UserRepositoryPort userRepositoryPort,
        StadiumRepositoryPort stadiumRepositoryPort
    ) {
        return new AddFavoriteStadiumInteractor(
            favoriteStadiumRepositoryPort,
            userRepositoryPort,
            stadiumRepositoryPort
        );
    }

    @Bean
    public DeleteFavoriteStadiumUseCase deleteFavoriteStadiumUseCase(
        FavoriteStadiumRepositoryPort favoriteStadiumRepositoryPort
    ) {
        return new DeleteFavoriteStadiumInteractor(favoriteStadiumRepositoryPort);
    }

    @Bean
    public GetTeamsUseCase getTeamsUseCase(TeamQueryPort teamQueryPort) {
        return new GetTeamsInteractor(teamQueryPort);
    }
}
