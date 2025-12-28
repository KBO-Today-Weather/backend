package kbo.today.adapter.out.persistence.stadium;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kbo.today.adapter.out.persistence.stadium.QStadiumJpaEntity;
import kbo.today.adapter.out.persistence.stadium.QStadiumFoodJpaEntity;
import kbo.today.adapter.out.persistence.stadium.QStadiumSeatJpaEntity;
import kbo.today.adapter.out.persistence.stadium.QStadiumTransportJpaEntity;
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

        // MultipleBagFetchException 방지: 
        // 한 번에 하나의 @OneToMany만 fetch join하고, 나머지는 별도 쿼리로 로드
        StadiumJpaEntity result = queryFactory
            .selectFrom(stadium)
            .leftJoin(stadium.team, team).fetchJoin()
            .leftJoin(stadium.foods).fetchJoin()  // 첫 번째 @OneToMany만 fetch join
            .where(stadium.id.eq(id))
            .fetchOne();

        if (result == null) {
            return Optional.empty();
        }

        // 나머지 @OneToMany 컬렉션들을 별도 QueryDSL 쿼리로 로드
        loadSeats(result);
        loadTransports(result);

        return Optional.of(result).map(this::toDomainWithDetails);
    }

    private void loadSeats(StadiumJpaEntity stadium) {
        QStadiumSeatJpaEntity seat = QStadiumSeatJpaEntity.stadiumSeatJpaEntity;
        
        // 별도 쿼리로 seats 로드 (MultipleBagFetchException 방지)
        List<StadiumSeatJpaEntity> seats = queryFactory
            .selectFrom(seat)
            .where(seat.stadium.id.eq(stadium.getId()))
            .fetch();
        
        // lazy 컬렉션을 초기화하기 위해 getter 호출 후 컬렉션에 추가
        // 영속성 컨텍스트에 이미 로드된 엔티티들이므로 안전하게 추가 가능
        if (!stadium.getSeats().isEmpty()) {
            stadium.getSeats().clear();
        }
        stadium.getSeats().addAll(seats);
    }

    private void loadTransports(StadiumJpaEntity stadium) {
        QStadiumTransportJpaEntity transport = QStadiumTransportJpaEntity.stadiumTransportJpaEntity;
        
        // 별도 쿼리로 transports 로드 (MultipleBagFetchException 방지)
        List<StadiumTransportJpaEntity> transports = queryFactory
            .selectFrom(transport)
            .where(transport.stadium.id.eq(stadium.getId()))
            .fetch();
        
        // lazy 컬렉션을 초기화하기 위해 getter 호출 후 컬렉션에 추가
        // 영속성 컨텍스트에 이미 로드된 엔티티들이므로 안전하게 추가 가능
        if (!stadium.getTransports().isEmpty()) {
            stadium.getTransports().clear();
        }
        stadium.getTransports().addAll(transports);
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

