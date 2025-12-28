package kbo.today.domain.stadium;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;

public class StadiumFood extends BaseEntity {
    
    private Stadium stadium;
    private String name;
    private Integer price;
    private String location;
    private Double rating;
    
    protected StadiumFood() {
        super();
    }
    
    protected StadiumFood(Long id, Stadium stadium, String name, Integer price, String location, Double rating,
                       LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.stadium = stadium;
        this.name = Objects.requireNonNull(name);
        this.price = Objects.requireNonNull(price);
        this.location = location;
        this.rating = rating;
    }
    
    public static StadiumFood create(Stadium stadium, String name, Integer price, String location, Double rating) {
        return new StadiumFood(null, stadium, name, price, location, rating, null, null, null);
    }
    
    public static StadiumFood fromPersistence(Long id, Stadium stadium, String name, Integer price, String location, Double rating,
                                              LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new StadiumFood(id, stadium, name, price, location, rating, createdAt, updatedAt, deletedAt);
    }
    
    public StadiumFood withId(Long id) {
        return new StadiumFood(id, this.stadium, this.name, this.price, this.location, this.rating,
                              this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
    }
    
    public Stadium getStadium() {
        return stadium;
    }
    
    public String getName() {
        return name;
    }
    
    public Integer getPrice() {
        return price;
    }
    
    public String getLocation() {
        return location;
    }
    
    public Double getRating() {
        return rating;
    }
}