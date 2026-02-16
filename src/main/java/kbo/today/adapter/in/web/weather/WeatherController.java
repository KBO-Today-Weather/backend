package kbo.today.adapter.in.web.weather;

import kbo.today.adapter.in.web.weather.dto.WeatherResponse;
import kbo.today.domain.weather.usecase.GetStadiumWeatherUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Tag(name = "Weather", description = "날씨 관련 API")
@RestController
@RequestMapping("/api/v1/stadiums")
@RequiredArgsConstructor
public class WeatherController {

    private final GetStadiumWeatherUseCase getStadiumWeatherUseCase;

    @Operation(summary = "구장 날씨 조회", description = "구장 ID로 해당 구장의 날씨 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "구장을 찾을 수 없음"),
        @ApiResponse(responseCode = "400", description = "구장 위치 정보가 없음")
    })
    @GetMapping("/{stadiumId}/weather")
    public Mono<ResponseEntity<WeatherResponse>> getStadiumWeather(@PathVariable Long stadiumId) {
        return getStadiumWeatherUseCase.getByStadiumId(stadiumId)
            .map(WeatherResponse::from)
            .map(ResponseEntity::ok);
    }
}

