package kbo.today.adapter.out.persistence.stadium;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.QStadium;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.team.QTeam;
import org.springframework.stereotype.Repository;

@Repository
public class StadiumQueryRepository {

    private final JPAQueryFactory queryFactory;

    public StadiumQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public Optional<Stadium> findByIdWithDetails(Long id) {
        QStadium stadium = QStadium.stadium;
        QTeam team = QTeam.team;

        Stadium result = queryFactory
            .selectFrom(stadium)
            .leftJoin(stadium.team, team).fetchJoin()
            .leftJoin(stadium.foods).fetchJoin()
            .leftJoin(stadium.seats).fetchJoin()
            .leftJoin(stadium.transports).fetchJoin()
            .where(stadium.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(result);
    }

    public List<Stadium> findAllWithTeam() {
        QStadium stadium = QStadium.stadium;
        QTeam team = QTeam.team;

        return queryFactory
            .selectFrom(stadium)
            .distinct()
            .leftJoin(stadium.team, team).fetchJoin()
            .fetch();
    }

    public Optional<Stadium> findByIdForWeather(Long id) {
        QStadium stadium = QStadium.stadium;
        QTeam team = QTeam.team;

        Stadium result = queryFactory
            .selectFrom(stadium)
            .leftJoin(stadium.team, team).fetchJoin()
            .where(stadium.id.eq(id))
            .fetchOne();

        return Optional.ofNullable(result);
    }
}

