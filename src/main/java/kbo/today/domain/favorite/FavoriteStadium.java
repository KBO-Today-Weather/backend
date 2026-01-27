package kbo.today.domain.favorite;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;

public class FavoriteStadium extends BaseEntity {

    private Long userId;
    private Long stadiumId;

    protected FavoriteStadium() {
        super();
    }

    protected FavoriteStadium(
        Long id,
        Long userId,
        Long stadiumId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
    ) {
        super(id, createdAt, updatedAt, deletedAt);
        this.userId = Objects.requireNonNull(userId);
        this.stadiumId = Objects.requireNonNull(stadiumId);
    }

    public static FavoriteStadium create(Long userId, Long stadiumId) {
        return new FavoriteStadium(null, userId, stadiumId, null, null, null);
    }

    public static FavoriteStadium fromPersistence(
        Long id,
        Long userId,
        Long stadiumId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
    ) {
        return new FavoriteStadium(id, userId, stadiumId, createdAt, updatedAt, deletedAt);
    }

    public FavoriteStadium withId(Long id) {
        return new FavoriteStadium(
            id,
            this.userId,
            this.stadiumId,
            this.getCreatedAt(),
            this.getUpdatedAt(),
            this.getDeletedAt()
        );
    }

    public Long getUserId() {
        return userId;
    }

    public Long getStadiumId() {
        return stadiumId;
    }
}

