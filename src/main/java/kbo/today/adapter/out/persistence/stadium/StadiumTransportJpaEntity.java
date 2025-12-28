package kbo.today.adapter.out.persistence.stadium;

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
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.StadiumTransport;
import kbo.today.domain.stadium.TransportType;

@Entity
@Table(name = "stadium_transports")
@EntityListeners(AuditingEntityListener.class)
public class StadiumTransportJpaEntity {
    
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
    @JoinColumn(name = "stadium_id")
    private StadiumJpaEntity stadium;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransportType transportType;
    
    @Column(nullable = false)
    private String route;
    
    private String description;
    private String tip;
    
    protected StadiumTransportJpaEntity() {}
    
    public StadiumTransportJpaEntity(StadiumJpaEntity stadium, TransportType transportType, String route, String description, String tip) {
        this.stadium = stadium;
        this.transportType = transportType;
        this.route = route;
        this.description = description;
        this.tip = tip;
    }
    
    public static StadiumTransportJpaEntity from(StadiumTransport transport, StadiumJpaEntity stadiumEntity) {
        StadiumTransportJpaEntity entity = new StadiumTransportJpaEntity(
            stadiumEntity,
            transport.getTransportType(),
            transport.getRoute(),
            transport.getDescription(),
            transport.getTip()
        );
        if (transport.getId() != null) {
            entity.id = transport.getId();
        }
        if (transport.getCreatedAt() != null) {
            entity.createdAt = transport.getCreatedAt();
        }
        if (transport.getUpdatedAt() != null) {
            entity.updatedAt = transport.getUpdatedAt();
        }
        if (transport.getDeletedAt() != null) {
            entity.deletedAt = transport.getDeletedAt();
        }
        return entity;
    }
    
    public StadiumTransport toDomain(Stadium stadium) {
        return StadiumTransport.fromPersistence(
            id,
            stadium,
            transportType,
            route,
            description,
            tip,
            createdAt,
            updatedAt,
            deletedAt
        );
    }
    
    public Long getId() {
        return id;
    }
    
    public StadiumJpaEntity getStadium() {
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

