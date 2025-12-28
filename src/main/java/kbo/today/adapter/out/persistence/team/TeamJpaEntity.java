package kbo.today.adapter.out.persistence.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import kbo.today.domain.team.Team;
import kbo.today.domain.team.TeamStatus;

@Entity
@Table(name = "teams")
@EntityListeners(AuditingEntityListener.class)
public class TeamJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime deletedAt;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String city;
    
    private String logoUrl;
    
    @Enumerated(EnumType.STRING)
    private TeamStatus status;
    
    protected TeamJpaEntity() {}
    
    public TeamJpaEntity(String name, String city, String logoUrl, TeamStatus status) {
        this.name = name;
        this.city = city;
        this.logoUrl = logoUrl;
        this.status = status;
    }
    
    public static TeamJpaEntity from(Team team) {
        TeamJpaEntity entity = new TeamJpaEntity(
            team.getName(),
            team.getCity(),
            team.getLogoUrl(),
            team.getStatus()
        );
        if (team.getId() != null) {
            entity.id = team.getId();
        }
        if (team.getCreatedAt() != null) {
            entity.createdAt = team.getCreatedAt();
        }
        if (team.getUpdatedAt() != null) {
            entity.updatedAt = team.getUpdatedAt();
        }
        if (team.getDeletedAt() != null) {
            entity.deletedAt = team.getDeletedAt();
        }
        return entity;
    }
    
    public Team toDomain() {
        return Team.fromPersistence(
            id,
            name,
            city,
            logoUrl,
            status,
            createdAt,
            updatedAt,
            deletedAt
        );
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getCity() {
        return city;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public TeamStatus getStatus() {
        return status;
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

