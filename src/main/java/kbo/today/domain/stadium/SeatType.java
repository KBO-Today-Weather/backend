package kbo.today.domain.stadium;

public enum SeatType {
    TABLE_SEAT("테이블석"),
    PREMIUM_SEAT("프리미엄석"),
    OUTFIELD_SEAT("외야석"),
    SKYBOX("스카이박스");
    
    private final String description;
    
    SeatType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}