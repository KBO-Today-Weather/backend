package kbo.today.domain.stadium;

import jakarta.persistence.*;
import kbo.today.common.domain.BaseEntity;

@Entity
@Table(name = "stadium_transports")
public class StadiumTransport extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportType transportType;
    
    @Column(nullable = false)
    private String route;
    
    private String description;
    private String tip;
    
    protected StadiumTransport() {}
    
    public StadiumTransport(Stadium stadium, TransportType transportType, String route, String description, String tip) {
        this.stadium = stadium;
        this.transportType = transportType;
        this.route = route;
        this.description = description;
        this.tip = tip;
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