package kbo.today.adapter.out.persistence.player;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import kbo.today.adapter.out.persistence.team.TeamJpaEntity;
import kbo.today.domain.player.Player;
import kbo.today.domain.player.Position;
import kbo.today.domain.team.Team;

@Entity
@Table(name = "players")
@EntityListeners(AuditingEntityListener.class)
public class PlayerJpaEntity {
    
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
    @JoinColumn(name = "team_id")
    private TeamJpaEntity team;
    
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
    
    protected PlayerJpaEntity() {}
    
    public PlayerJpaEntity(TeamJpaEntity team, String name, Integer backNumber, Position position) {
        this.team = team;
        this.name = name;
        this.backNumber = backNumber;
        this.position = position;
    }
    
    public static PlayerJpaEntity from(Player player, TeamJpaEntity teamEntity) {
        PlayerJpaEntity entity = new PlayerJpaEntity(teamEntity, player.getName(), player.getBackNumber(), player.getPosition());
        entity.birthDate = player.getBirthDate();
        entity.height = player.getHeight();
        entity.weight = player.getWeight();
        if (player.getId() != null) {
            entity.id = player.getId();
        }
        if (player.getCreatedAt() != null) {
            entity.createdAt = player.getCreatedAt();
        }
        if (player.getUpdatedAt() != null) {
            entity.updatedAt = player.getUpdatedAt();
        }
        if (player.getDeletedAt() != null) {
            entity.deletedAt = player.getDeletedAt();
        }
        return entity;
    }
    
    public Player toDomain() {
        Team teamDomain = team != null ? team.toDomain() : null;
        
        return Player.fromPersistence(
            id,
            teamDomain,
            name,
            backNumber,
            position,
            birthDate,
            height,
            weight,
            null, // battingRecords는 별도로 로드
            null, // pitchingRecords는 별도로 로드
            null, // gameBattingRecords는 별도로 로드
            null, // gamePitchingRecords는 별도로 로드
            createdAt,
            updatedAt,
            deletedAt
        );
    }
    
    public Long getId() {
        return id;
    }
    
    public TeamJpaEntity getTeam() {
        return team;
    }
    
    public void setTeam(TeamJpaEntity team) {
        this.team = team;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getBackNumber() {
        return backNumber;
    }
    
    public void setBackNumber(Integer backNumber) {
        this.backNumber = backNumber;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public LocalDate getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    
    public Integer getHeight() {
        return height;
    }
    
    public void setHeight(Integer height) {
        this.height = height;
    }
    
    public Integer getWeight() {
        return weight;
    }
    
    public void setWeight(Integer weight) {
        this.weight = weight;
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

