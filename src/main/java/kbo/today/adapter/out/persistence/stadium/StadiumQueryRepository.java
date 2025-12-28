package kbo.today.adapter.out.persistence.stadium;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kbo.today.adapter.out.persistence.stadium.QStadiumJpaEntity;
import kbo.today.adapter.out.persistence.team.QTeamJpaEntity;
import kbo.today.domain.stadium.Stadium;
import org.springframework.stereotype.Repository;

@Repository
public class StadiumQueryRepository {

    private final JPAQueryFactory queryFactory;

    public StadiumQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<Stadium> findByIdWithDetails(Long id) {
        QStadiumJpaEntity stadium = QStadiumJpaEntity.stadiumJpaEntity;
        QTeamJpaEntity team = QTeamJpaEntity.teamJpaEntity;

        StadiumJpaEntity result = queryFactory
            .selectFrom(stadium)
            .leftJoin(stadium.team, team).fetchJoin()
            .leftJoin(stadium.foods).fetchJoin()
            .leftJoin(stadium.seats).fetchJoin()
            .leftJoin(stadium.transports).fetchJoin()
            .where(stadium.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(result).map(this::toDomainWithDetails);
    }

    public List<Stadium> findAllWithTeam() {
        QStadiumJpaEntity stadium = QStadiumJpaEntity.stadiumJpaEntity;
        QTeamJpaEntity team = QTeamJpaEntity.teamJpaEntity;

        List<StadiumJpaEntity> results = queryFactory
            .selectFrom(stadium)
            .distinct()
            .leftJoin(stadium.team, team).fetchJoin()
            .fetch();

        return results.stream()
            .map(entity -> entity.toDomain())
            .collect(Collectors.toList());
    }

    public Optional<Stadium> findByIdForWeather(Long id) {
        QStadiumJpaEntity stadium = QStadiumJpaEntity.stadiumJpaEntity;
        QTeamJpaEntity team = QTeamJpaEntity.teamJpaEntity;

        StadiumJpaEntity result = queryFactory
            .selectFrom(stadium)
            .leftJoin(stadium.team, team).fetchJoin()
            .where(stadium.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(result).map(entity -> entity.toDomain());
    }
    
    private Stadium toDomainWithDetails(StadiumJpaEntity entity) {
        Stadium stadium = entity.toDomain();
        
        // 관련 엔티티들을 도메인 객체로 변환
        List<kbo.today.domain.stadium.StadiumFood> foods = entity.getFoods().stream()
            .map(food -> food.toDomain(stadium))
            .collect(Collectors.toList());
        
        List<kbo.today.domain.stadium.StadiumSeat> seats = entity.getSeats().stream()
            .map(seat -> seat.toDomain(stadium))
            .collect(Collectors.toList());
        
        List<kbo.today.domain.stadium.StadiumTransport> transports = entity.getTransports().stream()
            .map(transport -> transport.toDomain(stadium))
            .collect(Collectors.toList());
        
        // Stadium의 foods, seats, transports를 설정하기 위해 새로운 Stadium 인스턴스 생성
        return Stadium.fromPersistence(
            stadium.getId(),
            stadium.getTeam(),
            stadium.getName(),
            stadium.getAddress(),
            stadium.getCapacity(),
            stadium.getLatitude(),
            stadium.getLongitude(),
            foods,
            seats,
            transports,
            stadium.getCreatedAt(),
            stadium.getUpdatedAt(),
            stadium.getDeletedAt()
        );
    }
}

