package kbo.today.domain.team;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;

public class Team extends BaseEntity {

    private String name;
    private String city;
    private String logoUrl;
    private TeamStatus status;

    private Integer rank;
    private Integer wins;
    private Integer losses;
    private Integer draws;
    private Double winningPercentage;
    private Integer gamesBack;

    protected Team() {
        super();
    }

    protected Team(Long id, String name, String city, String logoUrl, TeamStatus status,
                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = Objects.requireNonNull(name);
        this.city = Objects.requireNonNull(city);
        this.logoUrl = logoUrl;
        this.status = status != null ? status : TeamStatus.ACTIVE;
    }

    protected Team(Long id, String name, String city, String logoUrl, TeamStatus status,
                   Integer rank, Integer wins, Integer losses, Integer draws,
                   Double winningPercentage, Integer gamesBack,
                   LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.name = Objects.requireNonNull(name);
        this.city = Objects.requireNonNull(city);
        this.logoUrl = logoUrl;
        this.status = status != null ? status : TeamStatus.ACTIVE;
        this.rank = rank;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.winningPercentage = winningPercentage;
        this.gamesBack = gamesBack;
    }

    public static Team create(String name, String city, String logoUrl) {
        return new Team(null, name, city, logoUrl, TeamStatus.ACTIVE, null, null, null);
    }

    public static Team fromPersistence(Long id, String name, String city, String logoUrl, TeamStatus status,
                                      LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        return new Team(id, name, city, logoUrl, status, createdAt, updatedAt, deletedAt);
    }

    public static Team fromPersistenceWithStandings(
        Long id, String name, String city, String logoUrl, TeamStatus status,
        int rank, int wins, int losses, int draws, double winningPercentage, Integer gamesBack,
        LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt
    ) {
        return new Team(
            id, name, city, logoUrl, status,
            rank, wins, losses, draws, winningPercentage, gamesBack,
            createdAt, updatedAt, deletedAt
        );
    }

    public Team withId(Long id) {
        return new Team(id, this.name, this.city, this.logoUrl, this.status,
            this.rank, this.wins, this.losses, this.draws,
            this.winningPercentage, this.gamesBack,
            this.getCreatedAt(), this.getUpdatedAt(), this.getDeletedAt());
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public Integer getRank() {
        return rank;
    }

    public Integer getWins() {
        return wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public Integer getDraws() {
        return draws;
    }

    public Double getWinningPercentage() {
        return winningPercentage;
    }

    public Integer getGamesBack() {
        return gamesBack;
    }
}