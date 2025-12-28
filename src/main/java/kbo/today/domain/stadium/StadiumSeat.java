package kbo.today.domain.stadium;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;

public class StadiumSeat extends BaseEntity {
    
    private Stadium stadium;
    private String sectionName;
    private SeatType seatType;
    private String description;
    private String recommendation;
    
    protected StadiumSeat() {
        super();
    }
    
    protected StadiumSeat(Long id, Stadium stadium, String sectionName, SeatType seatType,
                       String description, String recommendation,
                       LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.stadium = stadium;
        this.sectionName = Objects.requireNonNull(sectionName);
        this.seatType = Objects.requireNonNull(seatType);
        this.description = description;
        this.recommendation = recommendation;
    }
    
    public static StadiumSeat create(Stadium stadium, String sectionName, SeatType seatType,
                                    String description, String recommendation) {
        return new StadiumSeat(null, stadium, sectionName, seatType, description, recommendation,
                              null, null, null);
    }
    
    public static StadiumSeat fromPersistence(Long id, Stadium stadium, String sectionName, SeatType seatType,
                                             String description, String recommendation,
                                             LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new StadiumSeat(id, stadium, sectionName, seatType, description, recommendation,
                              createdAt, updatedAt, deletedAt);
    }
    
    public StadiumSeat withId(Long id) {
        return new StadiumSeat(id, this.stadium, this.sectionName, this.seatType,
                               this.description, this.recommendation,
                               this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
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