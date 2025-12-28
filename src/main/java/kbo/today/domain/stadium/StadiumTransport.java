package kbo.today.domain.stadium;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;

public class StadiumTransport extends BaseEntity {
    
    private Stadium stadium;
    private TransportType transportType;
    private String route;
    private String description;
    private String tip;
    
    protected StadiumTransport() {
        super();
    }
    
    protected StadiumTransport(Long id, Stadium stadium, TransportType transportType, String route,
                           String description, String tip,
                           LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.stadium = stadium;
        this.transportType = Objects.requireNonNull(transportType);
        this.route = Objects.requireNonNull(route);
        this.description = description;
        this.tip = tip;
    }
    
    public static StadiumTransport create(Stadium stadium, TransportType transportType, String route,
                                         String description, String tip) {
        return new StadiumTransport(null, stadium, transportType, route, description, tip,
                                   null, null, null);
    }
    
    public static StadiumTransport fromPersistence(Long id, Stadium stadium, TransportType transportType, String route,
                                                   String description, String tip,
                                                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new StadiumTransport(id, stadium, transportType, route, description, tip,
                                   createdAt, updatedAt, deletedAt);
    }
    
    public StadiumTransport withId(Long id) {
        return new StadiumTransport(id, this.stadium, this.transportType, this.route,
                                   this.description, this.tip,
                                   this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
    }
    
    public Stadium getStadium() {
        return stadium;
    }
    
    public TransportType getTransportType() {
        return transportType;
    }
    
    public String getRoute() {
        return route;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getTip() {
        return tip;
    }
}