package kbo.today.adapter.out.persistence.favorite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kbo.today.adapter.out.persistence.stadium.StadiumJpaEntity;
import kbo.today.adapter.out.persistence.user.UserJpaEntity;
import kbo.today.domain.favorite.FavoriteStadium;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_favorite_stadiums")
@EntityListeners(AuditingEntityListener.class)
public class FavoriteStadiumJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id", nullable = false)
    private StadiumJpaEntity stadium;

    protected FavoriteStadiumJpaEntity() {}

    public FavoriteStadiumJpaEntity(UserJpaEntity user, StadiumJpaEntity stadium) {
        this.user = user;
        this.stadium = stadium;
    }

    public static FavoriteStadiumJpaEntity from(
        FavoriteStadium favoriteStadium,
        UserJpaEntity user,
        StadiumJpaEntity stadium
    ) {
        FavoriteStadiumJpaEntity entity = new FavoriteStadiumJpaEntity(user, stadium);
        if (favoriteStadium.getId() != null) {
            entity.id = favoriteStadium.getId();
        }
        if (favoriteStadium.getCreatedAt() != null) {
            entity.createdAt = favoriteStadium.getCreatedAt();
        }
        if (favoriteStadium.getUpdatedAt() != null) {
            entity.updatedAt = favoriteStadium.getUpdatedAt();
        }
        if (favoriteStadium.getDeletedAt() != null) {
            entity.deletedAt = favoriteStadium.getDeletedAt();
        }
        return entity;
    }

    public FavoriteStadium toDomain() {
        return FavoriteStadium.fromPersistence(
            id,
            user.getId(),
            stadium.getId(),
            createdAt,
            updatedAt,
            deletedAt
        );
    }

    public Long getId() {
        return id;
    }

    public UserJpaEntity getUser() {
        return user;
    }

    public StadiumJpaEntity getStadium() {
        return stadium;
    }
}

