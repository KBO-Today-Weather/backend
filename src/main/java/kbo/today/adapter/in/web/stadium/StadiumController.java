package kbo.today.adapter.in.web.stadium;

import java.util.List;
import kbo.today.adapter.in.web.stadium.dto.StadiumResponse;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.usecase.GetStadiumUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Stadium", description = "구장 관련 API")
@RestController
@RequestMapping("/api/v1/stadiums")
public class StadiumController {

    private final GetStadiumUseCase getStadiumUseCase;

    public StadiumController(GetStadiumUseCase getStadiumUseCase) {
        this.getStadiumUseCase = getStadiumUseCase;
    }

    @Operation(summary = "구장 목록 조회", description = "모든 구장 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<StadiumResponse>> getAllStadiums() {
        List<Stadium> stadiums = getStadiumUseCase.getAll();
        List<StadiumResponse> responses = stadiums.stream()
            .map(StadiumResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "구장 상세 조회", description = "구장 ID로 구장 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "구장을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StadiumResponse> getStadiumById(@PathVariable Long id) {
        return getStadiumUseCase.getById(id)
            .map(StadiumResponse::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

