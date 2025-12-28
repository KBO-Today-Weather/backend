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
import kbo.today.domain.stadium.SeatType;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.StadiumSeat;

@Entity
@Table(name = "stadium_seats")
@EntityListeners(AuditingEntityListener.class)
public class StadiumSeatJpaEntity {
    
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
    
    @Column(nullable = false)
    private String sectionName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;
    
    private String description;
    private String recommendation;
    
    protected StadiumSeatJpaEntity() {}
    
    public StadiumSeatJpaEntity(StadiumJpaEntity stadium, String sectionName, SeatType seatType, String description, String recommendation) {
        this.stadium = stadium;
        this.sectionName = sectionName;
        this.seatType = seatType;
        this.description = description;
        this.recommendation = recommendation;
    }
    
    public static StadiumSeatJpaEntity from(StadiumSeat seat, StadiumJpaEntity stadiumEntity) {
        StadiumSeatJpaEntity entity = new StadiumSeatJpaEntity(
            stadiumEntity,
            seat.getSectionName(),
            seat.getSeatType(),
            seat.getDescription(),
            seat.getRecommendation()
        );
        if (seat.getId() != null) {
            entity.id = seat.getId();
        }
        if (seat.getCreatedAt() != null) {
            entity.createdAt = seat.getCreatedAt();
        }
        if (seat.getUpdatedAt() != null) {
            entity.updatedAt = seat.getUpdatedAt();
        }
        if (seat.getDeletedAt() != null) {
            entity.deletedAt = seat.getDeletedAt();
        }
        return entity;
    }
    
    public StadiumSeat toDomain(Stadium stadium) {
        return StadiumSeat.fromPersistence(
            id,
            stadium,
            sectionName,
            seatType,
            description,
            recommendation,
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
    
    public String getSectionName() {
        return sectionName;
    }
    
    public SeatType getSeatType() {
        return seatType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getRecommendation() {
        return recommendation;
    }
}

