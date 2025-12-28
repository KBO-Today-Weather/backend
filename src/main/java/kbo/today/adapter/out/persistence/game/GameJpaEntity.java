package kbo.today.adapter.out.persistence.game;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import kbo.today.adapter.out.persistence.team.TeamJpaEntity;
import kbo.today.domain.game.Game;
import kbo.today.domain.game.GameStatus;
import kbo.today.domain.team.Team;

@Entity
@Table(name = "games")
@EntityListeners(AuditingEntityListener.class)
public class GameJpaEntity {
    
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
    @JoinColumn(name = "home_team_id")
    private TeamJpaEntity homeTeam;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private TeamJpaEntity awayTeam;
    
    @Column(nullable = false)
    private LocalDateTime gameDate;
    
    private Integer homeScore;
    private Integer awayScore;
    
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    private String stadium;
    
    protected GameJpaEntity() {}
    
    public GameJpaEntity(TeamJpaEntity homeTeam, TeamJpaEntity awayTeam, LocalDateTime gameDate, String stadium) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameDate = gameDate;
        this.stadium = stadium;
        this.status = GameStatus.SCHEDULED;
    }
    
    public static GameJpaEntity from(Game game, TeamJpaEntity homeTeamEntity, TeamJpaEntity awayTeamEntity) {
        GameJpaEntity entity = new GameJpaEntity(homeTeamEntity, awayTeamEntity, game.getGameDate(), game.getStadium());
        entity.homeScore = game.getHomeScore();
        entity.awayScore = game.getAwayScore();
        entity.status = game.getStatus();
        if (game.getId() != null) {
            entity.id = game.getId();
        }
        if (game.getCreatedAt() != null) {
            entity.createdAt = game.getCreatedAt();
        }
        if (game.getUpdatedAt() != null) {
            entity.updatedAt = game.getUpdatedAt();
        }
        if (game.getDeletedAt() != null) {
            entity.deletedAt = game.getDeletedAt();
        }
        return entity;
    }
    
    public Game toDomain() {
        Team homeTeamDomain = homeTeam != null ? homeTeam.toDomain() : null;
        Team awayTeamDomain = awayTeam != null ? awayTeam.toDomain() : null;
        
        return Game.fromPersistence(
            id,
            homeTeamDomain,
            awayTeamDomain,
            gameDate,
            stadium,
            homeScore,
            awayScore,
            status,
            null, // battingRecords는 별도로 로드
            null, // pitchingRecords는 별도로 로드
            createdAt,
            updatedAt,
            deletedAt
        );
    }
    
    public Long getId() {
        return id;
    }
    
    public TeamJpaEntity getHomeTeam() {
        return homeTeam;
    }
    
    public TeamJpaEntity getAwayTeam() {
        return awayTeam;
    }
    
    public LocalDateTime getGameDate() {
        return gameDate;
    }
    
    public Integer getHomeScore() {
        return homeScore;
    }
    
    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }
    
    public Integer getAwayScore() {
        return awayScore;
    }
    
    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }
    
    public GameStatus getStatus() {
        return status;
    }
    
    public void setStatus(GameStatus status) {
        this.status = status;
    }
    
    public String getStadium() {
        return stadium;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}

