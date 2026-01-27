package kbo.today.domain.favorite.usecase;

public class AddFavoriteStadiumCommand {

    private final Long userId;
    private final Long stadiumId;

    public AddFavoriteStadiumCommand(Long userId, Long stadiumId) {
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

