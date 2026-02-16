package kbo.today.adapter.out.persistence.team;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kbo.today.domain.team.Team;

@Entity
@Table(
    name = "team_standings",
    uniqueConstraints = @UniqueConstraint(columnNames = {"season", "team_id"})
)
public class TeamSeasonJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private TeamJpaEntity team;

    @Column(nullable = false)
    private int rank;

    @Column(nullable = false)
    private int wins;

    @Column(nullable = false)
    private int losses;

    @Column(nullable = false)
    private int draws;

    @Column(name = "winning_percentage", nullable = false)
    private double winningPercentage;

    @Column(name = "games_back")
    private Integer gamesBack;

    @Column(name = "batting_average")
    private Double battingAverage;

    @Column(name = "home_runs")
    private Integer homeRuns;

    @Column(name = "era")
    private Double era;

    @Column(name = "stolen_bases")
    private Integer stolenBases;

    @Column(name = "fielding_percentage")
    private Double fieldingPercentage;

    @Column(name = "postseason_result")
    private String postseasonResult;

    protected TeamSeasonJpaEntity() {
    }

    public TeamSeasonJpaEntity(
        int season,
        TeamJpaEntity team,
        int rank,
        int wins,
        int losses,
        int draws,
        double winningPercentage,
        Integer gamesBack
    ) {
        this.season = season;
        this.team = team;
        this.rank = rank;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.winningPercentage = winningPercentage;
        this.gamesBack = gamesBack;
    }

    public Team toDomain() {
        if (team == null) {
            throw new IllegalStateException("team is required");
        }
        return Team.fromPersistenceWithStandings(
            team.getId(),
            team.getName(),
            team.getCity(),
            team.getLogoUrl(),
            team.getStatus(),
            rank,
            wins,
            losses,
            draws,
            winningPercentage,
            gamesBack,
            team.getCreatedAt(),
            team.getUpdatedAt(),
            team.getDeletedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public int getSeason() {
        return season;
    }

    public TeamJpaEntity getTeam() {
        return team;
    }

    public int getRank() {
        return rank;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public double getWinningPercentage() {
        return winningPercentage;
    }

    public Integer getGamesBack() {
        return gamesBack;
    }

    public Double getBattingAverage() {
        return battingAverage;
    }

    public Integer getHomeRuns() {
        return homeRuns;
    }

    public Double getEra() {
        return era;
    }

    public Integer getStolenBases() {
        return stolenBases;
    }

    public Double getFieldingPercentage() {
        return fieldingPercentage;
    }

    public String getPostseasonResult() {
        return postseasonResult;
    }
}
