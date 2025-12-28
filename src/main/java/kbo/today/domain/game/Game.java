package kbo.today.domain.game;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.player.GameBattingRecord;
import kbo.today.domain.player.GamePitchingRecord;
import kbo.today.domain.team.Team;

public class Game extends BaseEntity {
    
    private Team homeTeam;
    private Team awayTeam;
    private LocalDateTime gameDate;
    private Integer homeScore;
    private Integer awayScore;
    private GameStatus status;
    private String stadium;
    private List<GameBattingRecord> battingRecords;
    private List<GamePitchingRecord> pitchingRecords;
    
    protected Game() {
        super();
    }
    
    protected Game(Long id, Team homeTeam, Team awayTeam, LocalDateTime gameDate, String stadium,
                 Integer homeScore, Integer awayScore, GameStatus status,
                 List<GameBattingRecord> battingRecords, List<GamePitchingRecord> pitchingRecords,
                 LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.homeTeam = Objects.requireNonNull(homeTeam);
        this.awayTeam = Objects.requireNonNull(awayTeam);
        this.gameDate = Objects.requireNonNull(gameDate);
        this.stadium = stadium;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = status != null ? status : GameStatus.SCHEDULED;
        this.battingRecords = battingRecords != null ? battingRecords : new ArrayList<>();
        this.pitchingRecords = pitchingRecords != null ? pitchingRecords : new ArrayList<>();
    }
    
    public static Game create(Team homeTeam, Team awayTeam, LocalDateTime gameDate, String stadium) {
        return new Game(null, homeTeam, awayTeam, gameDate, stadium,
                       null, null, GameStatus.SCHEDULED,
                       new ArrayList<>(), new ArrayList<>(),
                       null, null, null);
    }
    
    public static Game fromPersistence(Long id, Team homeTeam, Team awayTeam, LocalDateTime gameDate, String stadium,
                                       Integer homeScore, Integer awayScore, GameStatus status,
                                       List<GameBattingRecord> battingRecords, List<GamePitchingRecord> pitchingRecords,
                                       LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new Game(id, homeTeam, awayTeam, gameDate, stadium,
                       homeScore, awayScore, status,
                       battingRecords, pitchingRecords,
                       createdAt, updatedAt, deletedAt);
    }
    
    public Game withId(Long id) {
        return new Game(id, this.homeTeam, this.awayTeam, this.gameDate, this.stadium,
                       this.homeScore, this.awayScore, this.status,
                       this.battingRecords, this.pitchingRecords,
                       this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
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