package kbo.today.domain.stadium;

public enum TransportType {
    SUBWAY("지하철"),
    BUS("버스"),
    CAR("자차"),
    WALKING("도보");
    
    private final String description;
    
    TransportType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}