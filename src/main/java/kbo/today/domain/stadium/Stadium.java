package kbo.today.domain.stadium;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.team.Team;

public class Stadium extends BaseEntity {
    
    private Team team;
    private String name;
    private String address;
    private Integer capacity;
    private Double latitude;
    private Double longitude;
    private List<StadiumFood> foods;
    private List<StadiumSeat> seats;
    private List<StadiumTransport> transports;
    
    protected Stadium() {
        super();
    }
    
    protected Stadium(Long id, Team team, String name, String address, Integer capacity,
                   Double latitude, Double longitude,
                   List<StadiumFood> foods, List<StadiumSeat> seats, List<StadiumTransport> transports,
                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.team = team;
        this.name = Objects.requireNonNull(name);
        this.address = Objects.requireNonNull(address);
        this.capacity = capacity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foods = foods != null ? foods : new ArrayList<>();
        this.seats = seats != null ? seats : new ArrayList<>();
        this.transports = transports != null ? transports : new ArrayList<>();
    }
    
    public static Stadium create(Team team, String name, String address, Integer capacity) {
        return new Stadium(null, team, name, address, capacity,
                          null, null,
                          new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                          null, null, null);
    }
    
    public static Stadium create(Team team, String name, String address, Integer capacity,
                                Double latitude, Double longitude) {
        return new Stadium(null, team, name, address, capacity,
                          latitude, longitude,
                          new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                          null, null, null);
    }
    
    public static Stadium fromPersistence(Long id, Team team, String name, String address, Integer capacity,
                                          Double latitude, Double longitude,
                                          List<StadiumFood> foods, List<StadiumSeat> seats, List<StadiumTransport> transports,
                                          LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new Stadium(id, team, name, address, capacity,
                          latitude, longitude,
                          foods, seats, transports,
                          createdAt, updatedAt, deletedAt);
    }
    
    public Stadium withId(Long id) {
        return new Stadium(id, this.team, this.name, this.address, this.capacity,
                          this.latitude, this.longitude,
                          this.foods, this.seats, this.transports,
                          this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
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
        return foods != null ? foods : new ArrayList<>();
    }
    
    public List<StadiumSeat> getSeats() {
        return seats != null ? seats : new ArrayList<>();
    }
    
    public List<StadiumTransport> getTransports() {
        return transports != null ? transports : new ArrayList<>();
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void updateLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}