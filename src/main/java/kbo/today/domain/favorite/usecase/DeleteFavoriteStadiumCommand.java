package kbo.today.domain.favorite.usecase;

public class DeleteFavoriteStadiumCommand {

    private final Long userId;
    private final Long stadiumId;

    public DeleteFavoriteStadiumCommand(Long userId, Long stadiumId) {
        this.userId = userId;
        this.stadiumId = stadiumId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStadiumId() {
        return stadiumId;
    }
}

