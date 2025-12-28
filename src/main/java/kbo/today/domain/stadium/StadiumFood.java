package kbo.today.domain.stadium;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kbo.today.common.domain.BaseEntity;

@Entity
@Table(name = "stadium_foods")
public class StadiumFood extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stadium_id")
    private Stadium stadium;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer price;
    
    private String location;
    private Double rating;
    
    protected StadiumFood() {}
    
    public StadiumFood(Stadium stadium, String name, Integer price, String location, Double rating) {
        this.stadium = stadium;
        this.name = name;
        this.price = price;
        this.location = location;
        this.rating = rating;
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