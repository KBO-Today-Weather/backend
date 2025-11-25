package kbo.today.domain.player;

import jakarta.persistence.*;
import kbo.today.domain.common.BaseEntity;
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
    private List<BattingRecord> battingRecords = new ArrayList<>();
    
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PitchingRecord> pitchingRecords = new ArrayList<>();
    
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
        return battingRecords;
    }
    
    public List<PitchingRecord> getPitchingRecords() {
        return pitchingRecords;
    }
}
