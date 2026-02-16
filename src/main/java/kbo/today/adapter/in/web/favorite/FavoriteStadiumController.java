package kbo.today.adapter.in.web.favorite;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kbo.today.adapter.in.web.favorite.dto.FavoriteStadiumResponse;
import lombok.RequiredArgsConstructor;
import kbo.today.domain.favorite.FavoriteStadium;
import kbo.today.domain.favorite.usecase.AddFavoriteStadiumCommand;
import kbo.today.domain.favorite.usecase.AddFavoriteStadiumUseCase;
import kbo.today.domain.favorite.usecase.DeleteFavoriteStadiumCommand;
import kbo.today.domain.favorite.usecase.DeleteFavoriteStadiumUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Favorite", description = "즐겨찾기 관련 API")
@RestController
@RequestMapping("/api/v1/users/{userId}/favorites/stadiums")
@RequiredArgsConstructor
public class FavoriteStadiumController {

    private final AddFavoriteStadiumUseCase addFavoriteStadiumUseCase;
    private final DeleteFavoriteStadiumUseCase deleteFavoriteStadiumUseCase;

    @Operation(summary = "구장 즐겨찾기 등록", description = "사용자가 특정 구장을 즐겨찾기로 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "즐겨찾기 등록 성공"),
        @ApiResponse(responseCode = "404", description = "사용자 또는 구장을 찾을 수 없음"),
        @ApiResponse(responseCode = "403", description = "다른 사용자의 리소스에 접근 불가")
    })
    @PreAuthorize("#userId.equals(authentication.principal.userId)")
    @PostMapping("/{stadiumId}")
    public ResponseEntity<FavoriteStadiumResponse> addFavoriteStadium(
        @PathVariable Long userId,
        @PathVariable Long stadiumId
    ) {
        AddFavoriteStadiumCommand command = new AddFavoriteStadiumCommand(userId, stadiumId);
        FavoriteStadium favorite = addFavoriteStadiumUseCase.add(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(FavoriteStadiumResponse.from(favorite));
    }

    @Operation(summary = "구장 즐겨찾기 해제", description = "사용자가 특정 구장의 즐겨찾기를 해제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "즐겨찾기 해제 성공 (또는 이미 즐겨찾기가 아님)"),
        @ApiResponse(responseCode = "404", description = "사용자 또는 구장을 찾을 수 없음 (등록 시점)"),
        @ApiResponse(responseCode = "403", description = "다른 사용자의 리소스에 접근 불가")
    })
    @PreAuthorize("#userId.equals(authentication.principal.userId)")
    @DeleteMapping("/{stadiumId}")
    public ResponseEntity<Void> deleteFavoriteStadium(
        @PathVariable Long userId,
        @PathVariable Long stadiumId
    ) {
        DeleteFavoriteStadiumCommand command = new DeleteFavoriteStadiumCommand(userId, stadiumId);
        deleteFavoriteStadiumUseCase.delete(command);
        return ResponseEntity.noContent().build();
    }
}

