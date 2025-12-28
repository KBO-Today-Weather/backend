package kbo.today.adapter.out.persistence.stadium;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import kbo.today.domain.stadium.StadiumFood;

@Entity
@Table(name = "stadium_foods")
@EntityListeners(AuditingEntityListener.class)
public class StadiumFoodJpaEntity {
    
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
    private String name;
    
    @Column(nullable = false)
    private Integer price;
    
    private String location;
    private Double rating;
    
    protected StadiumFoodJpaEntity() {}
    
    public StadiumFoodJpaEntity(StadiumJpaEntity stadium, String name, Integer price, String location, Double rating) {
        this.stadium = stadium;
        this.name = name;
        this.price = price;
        this.location = location;
        this.rating = rating;
    }
    
    public static StadiumFoodJpaEntity from(StadiumFood food, StadiumJpaEntity stadiumEntity) {
        StadiumFoodJpaEntity entity = new StadiumFoodJpaEntity(
            stadiumEntity,
            food.getName(),
            food.getPrice(),
            food.getLocation(),
            food.getRating()
        );
        if (food.getId() != null) {
            entity.id = food.getId();
        }
        if (food.getCreatedAt() != null) {
            entity.createdAt = food.getCreatedAt();
        }
        if (food.getUpdatedAt() != null) {
            entity.updatedAt = food.getUpdatedAt();
        }
        if (food.getDeletedAt() != null) {
            entity.deletedAt = food.getDeletedAt();
        }
        return entity;
    }
    
    public StadiumFood toDomain(Stadium stadium) {
        return StadiumFood.fromPersistence(
            id,
            stadium,
            name,
            price,
            location,
            rating,
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

