package kbo.today.domain.stadium;

import jakarta.persistence.*;
import kbo.today.common.domain.BaseEntity;

@Entity
@Table(name = "stadium_seats")
public class StadiumSeat extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;
    
    @Column(nullable = false)
    private String sectionName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatType seatType;
    
    private String description;
    private String recommendation;
    
    protected StadiumSeat() {}
    
    public StadiumSeat(Stadium stadium, String sectionName, SeatType seatType, String description, String recommendation) {
        this.stadium = stadium;
        this.sectionName = sectionName;
        this.seatType = seatType;
        this.description = description;
        this.recommendation = recommendation;
    }
    
    public Stadium getStadium() {
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