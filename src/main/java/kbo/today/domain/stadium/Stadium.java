package kbo.today.domain.stadium;

import jakarta.persistence.*;
import kbo.today.domain.common.BaseEntity;
import kbo.today.domain.team.Team;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stadiums")
public class Stadium extends BaseEntity {
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String address;
    
    private Integer capacity;
    
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StadiumFood> foods = new ArrayList<>();
    
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StadiumSeat> seats = new ArrayList<>();
    
    @OneToMany(mappedBy = "stadium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StadiumTransport> transports = new ArrayList<>();
    
    protected Stadium() {}
    
    public Stadium(Team team, String name, String address, Integer capacity) {
        this.team = team;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
    }
    
    public Team getTeam() {
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
    
    public List<StadiumFood> getFoods() {
        return foods;
    }
    
    public List<StadiumSeat> getSeats() {
        return seats;
    }
    
    public List<StadiumTransport> getTransports() {
        return transports;
    }
}