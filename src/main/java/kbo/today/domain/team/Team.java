package kbo.today.domain.team;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;

public class Team extends BaseEntity {
    
    private String name;
    private String city;
    private String logoUrl;
    private TeamStatus status;
    
    protected Team() {
        super();
    }
    
    protected Team(Long id, String name, String city, String logoUrl, TeamStatus status,
                 LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = Objects.requireNonNull(name);
        this.city = Objects.requireNonNull(city);
        this.logoUrl = logoUrl;
        this.status = status != null ? status : TeamStatus.ACTIVE;
    }
    
    public static Team create(String name, String city, String logoUrl) {
        return new Team(null, name, city, logoUrl, TeamStatus.ACTIVE, null, null, null);
    }
    
    public static Team fromPersistence(Long id, String name, String city, String logoUrl, TeamStatus status,
                                       LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new Team(id, name, city, logoUrl, status, createdAt, updatedAt, deletedAt);
    }
    
    public Team withId(Long id) {
        return new Team(id, this.name, this.city, this.logoUrl, this.status,
                       this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
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
}