package kbo.today.domain.player;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.team.Team;

public class Player extends BaseEntity {
    
    private Team team;
    private String name;
    private Integer backNumber;
    private Position position;
    private LocalDate birthDate;
    private Integer height;
    private Integer weight;
    private List<BattingRecord> battingRecords;
    private List<PitchingRecord> pitchingRecords;
    private List<GameBattingRecord> gameBattingRecords;
    private List<GamePitchingRecord> gamePitchingRecords;
    
    protected Player() {
        super();
    }
    
    protected Player(Long id, Team team, String name, Integer backNumber, Position position,
                  LocalDate birthDate, Integer height, Integer weight,
                  List<BattingRecord> battingRecords, List<PitchingRecord> pitchingRecords,
                  List<GameBattingRecord> gameBattingRecords, List<GamePitchingRecord> gamePitchingRecords,
                  LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.team = team;
        this.name = Objects.requireNonNull(name);
        this.backNumber = Objects.requireNonNull(backNumber);
        this.position = Objects.requireNonNull(position);
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.battingRecords = battingRecords != null ? battingRecords : new ArrayList<>();
        this.pitchingRecords = pitchingRecords != null ? pitchingRecords : new ArrayList<>();
        this.gameBattingRecords = gameBattingRecords != null ? gameBattingRecords : new ArrayList<>();
        this.gamePitchingRecords = gamePitchingRecords != null ? gamePitchingRecords : new ArrayList<>();
    }
    
    public static Player create(Team team, String name, Integer backNumber, Position position) {
        return new Player(null, team, name, backNumber, position,
                         null, null, null,
                         new ArrayList<>(), new ArrayList<>(),
                         new ArrayList<>(), new ArrayList<>(),
                         null, null, null);
    }
    
    public static Player fromPersistence(Long id, Team team, String name, Integer backNumber, Position position,
                                        LocalDate birthDate, Integer height, Integer weight,
                                        List<BattingRecord> battingRecords, List<PitchingRecord> pitchingRecords,
                                        List<GameBattingRecord> gameBattingRecords, List<GamePitchingRecord> gamePitchingRecords,
                                        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new Player(id, team, name, backNumber, position,
                         birthDate, height, weight,
                         battingRecords, pitchingRecords,
                         gameBattingRecords, gamePitchingRecords,
                         createdAt, updatedAt, deletedAt);
    }
    
    public Player withId(Long id) {
        return new Player(id, this.team, this.name, this.backNumber, this.position,
                         this.birthDate, this.height, this.weight,
                         this.battingRecords, this.pitchingRecords,
                         this.gameBattingRecords, this.gamePitchingRecords,
                         this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
    }
    
    public Team getTeam() {
        return team;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getBackNumber() {
        return backNumber;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public Integer getWeight() {
        return weight;
    }
    
    public void updatePhysicalInfo(LocalDate birthDate, Integer height, Integer weight) {
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
    }
    
    public void changeTeam(Team team) {
        this.team = team;
    }
    
    public void changeBackNumber(Integer backNumber) {
        this.backNumber = backNumber;
    }
    
    public List<BattingRecord> getBattingRecords() {
        return battingRecords != null ? battingRecords : new ArrayList<>();
    }
    
    public List<PitchingRecord> getPitchingRecords() {
        return pitchingRecords != null ? pitchingRecords : new ArrayList<>();
    }
    
    public List<GameBattingRecord> getGameBattingRecords() {
        return gameBattingRecords != null ? gameBattingRecords : new ArrayList<>();
    }
    
    public List<GamePitchingRecord> getGamePitchingRecords() {
        return gamePitchingRecords != null ? gamePitchingRecords : new ArrayList<>();
    }
}
