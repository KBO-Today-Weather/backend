package kbo.today.domain.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kbo.today.common.domain.BaseEntity;

@Entity
@Table(name = "teams")
public class Team extends BaseEntity {
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String city;
    
    private String logoUrl;
    
    @Enumerated(EnumType.STRING)
    private TeamStatus status;
    
    protected Team() {}
    
    public Team(String name, String city, String logoUrl) {
        this.name = name;
        this.city = city;
        this.logoUrl = logoUrl;
        this.status = TeamStatus.ACTIVE;
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