package kbo.today.domain.game;

import jakarta.persistence.*;
import kbo.today.domain.common.BaseEntity;
import kbo.today.domain.team.Team;

import java.time.LocalDateTime;

@Entity
@Table(name = "games")
public class Game extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;
    
    @Column(nullable = false)
    private LocalDateTime gameDate;
    
    private Integer homeScore;
    private Integer awayScore;
    
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    private String stadium;
    
    protected Game() {}
    
    public Game(Team homeTeam, Team awayTeam, LocalDateTime gameDate, String stadium) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameDate = gameDate;
        this.stadium = stadium;
        this.status = GameStatus.SCHEDULED;
    }
    
    public Team getHomeTeam() {
        return homeTeam;
    }
    
    public Team getAwayTeam() {
        return awayTeam;
    }
    
    public LocalDateTime getGameDate() {
        return gameDate;
    }
    
    public Integer getHomeScore() {
        return homeScore;
    }
    
    public Integer getAwayScore() {
        return awayScore;
    }
    
    public GameStatus getStatus() {
        return status;
    }
    
    public String getStadium() {
        return stadium;
    }
    
    public void updateScore(Integer homeScore, Integer awayScore) {
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }
    
    public void finish() {
        this.status = GameStatus.FINISHED;
    }
}