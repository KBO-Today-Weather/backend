package kbo.today.adapter.out.persistence.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TeamSeasonQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TeamSeasonQueryRepository(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<TeamSeasonJpaEntity> findBySeasonOrderByRankAsc(int season) {
        QTeamSeasonJpaEntity seasonEntity = QTeamSeasonJpaEntity.teamSeasonJpaEntity;
        QTeamJpaEntity team = QTeamJpaEntity.teamJpaEntity;

        return queryFactory
            .selectFrom(seasonEntity)
            .join(seasonEntity.team, team).fetchJoin()
            .where(seasonEntity.season.eq(season))
            .orderBy(seasonEntity.rank.asc())
            .fetch();
    }
}
