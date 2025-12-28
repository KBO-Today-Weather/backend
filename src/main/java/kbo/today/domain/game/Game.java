package kbo.today.domain.game;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.player.GameBattingRecord;
import kbo.today.domain.player.GamePitchingRecord;
import kbo.today.domain.team.Team;

import java.util.ArrayList;
import java.util.List;
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
    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameBattingRecord> battingRecords;
    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamePitchingRecord> pitchingRecords;
    
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
    
    public List<GameBattingRecord> getBattingRecords() {
        return battingRecords != null ? battingRecords : new ArrayList<>();
    }
    
    public List<GamePitchingRecord> getPitchingRecords() {
        return pitchingRecords != null ? pitchingRecords : new ArrayList<>();
    }
}