package kbo.today.adapter.out.persistence.team;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
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

    public Optional<TeamSeasonJpaEntity> findByTeamIdAndSeason(Long teamId, int season) {
        QTeamSeasonJpaEntity seasonEntity = QTeamSeasonJpaEntity.teamSeasonJpaEntity;
        QTeamJpaEntity team = QTeamJpaEntity.teamJpaEntity;

        return Optional.ofNullable(
            queryFactory
                .selectFrom(seasonEntity)
                .join(seasonEntity.team, team).fetchJoin()
                .where(seasonEntity.team.id.eq(teamId), seasonEntity.season.eq(season))
                .fetchOne()
        );
    }

    public List<TeamSeasonJpaEntity> findByTeamIdOrderBySeasonDesc(Long teamId, Integer seasonFrom, Integer seasonTo) {
        QTeamSeasonJpaEntity seasonEntity = QTeamSeasonJpaEntity.teamSeasonJpaEntity;
        QTeamJpaEntity team = QTeamJpaEntity.teamJpaEntity;

        var predicate = seasonEntity.team.id.eq(teamId);
        if (seasonFrom != null) {
            predicate = predicate.and(seasonEntity.season.goe(seasonFrom));
        }
        if (seasonTo != null) {
            predicate = predicate.and(seasonEntity.season.loe(seasonTo));
        }
        return queryFactory
            .selectFrom(seasonEntity)
            .join(seasonEntity.team, team).fetchJoin()
            .where(predicate)
            .orderBy(seasonEntity.season.desc())
            .fetch();
    }
}
