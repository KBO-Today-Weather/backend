package kbo.today.adapter.out.persistence.stadium;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import kbo.today.adapter.out.persistence.team.TeamJpaEntity;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.team.Team;

@Entity
@Table(name = "stadiums")
@EntityListeners(AuditingEntityListener.class)
public class StadiumJpaEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    private LocalDateTime updatedAt;
    
    private LocalDateTime deletedAt;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private TeamJpaEntity team;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String address;
    
    private Integer capacity;
    
    private Double latitude;
    
    private Double longitude;
    
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StadiumFoodJpaEntity> foods = new ArrayList<>();
    
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StadiumSeatJpaEntity> seats = new ArrayList<>();
    
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StadiumTransportJpaEntity> transports = new ArrayList<>();
    
    protected StadiumJpaEntity() {}
    
    public StadiumJpaEntity(TeamJpaEntity team, String name, String address, Integer capacity) {
        this.team = team;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }
    
    public static StadiumJpaEntity from(Stadium stadium, TeamJpaEntity teamEntity) {
        StadiumJpaEntity entity = new StadiumJpaEntity(teamEntity, stadium.getName(), stadium.getAddress(), stadium.getCapacity());
        entity.latitude = stadium.getLatitude();
        entity.longitude = stadium.getLongitude();
        if (stadium.getId() != null) {
            entity.id = stadium.getId();
        }
        if (stadium.getCreatedAt() != null) {
            entity.createdAt = stadium.getCreatedAt();
        }
        if (stadium.getUpdatedAt() != null) {
            entity.updatedAt = stadium.getUpdatedAt();
        }
        if (stadium.getDeletedAt() != null) {
            entity.deletedAt = stadium.getDeletedAt();
        }
        return entity;
    }
    
    public Stadium toDomain() {
        Team teamDomain = team != null ? team.toDomain() : null;
        
        return Stadium.fromPersistence(
            id,
            teamDomain,
            name,
            address,
            capacity,
            latitude,
            longitude,
            null, // foods는 별도로 로드
            null, // seats는 별도로 로드
            null, // transports는 별도로 로드
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
    
    public String getName() {
        return name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public Integer getCapacity() {
        return capacity;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    
    public List<StadiumFoodJpaEntity> getFoods() {
        return foods;
    }
    
    public List<StadiumSeatJpaEntity> getSeats() {
        return seats;
    }
    
    public List<StadiumTransportJpaEntity> getTransports() {
        return transports;
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

