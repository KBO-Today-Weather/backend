package kbo.today.domain.player;

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
import kbo.today.domain.team.Team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "players")
public class Player extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer backNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;
    
    private LocalDate birthDate;
    private Integer height;
    private Integer weight;
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BattingRecord> battingRecords;
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PitchingRecord> pitchingRecords;
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameBattingRecord> gameBattingRecords;
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GamePitchingRecord> gamePitchingRecords;
    
    protected Player() {}
    
    public Player(Team team, String name, Integer backNumber, Position position) {
        this.team = team;
        this.name = name;
        this.backNumber = backNumber;
        this.position = position;
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
