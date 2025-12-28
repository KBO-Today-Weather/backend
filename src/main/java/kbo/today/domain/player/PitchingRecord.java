package kbo.today.domain.player;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kbo.today.common.domain.BaseEntity;

@Entity
@Table(name = "pitching_records")
public class PitchingRecord extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @Column(nullable = false)
    private Integer season;
    
    private Integer games = 0;
    private Integer gamesStarted = 0;
    private Integer wins = 0;
    private Integer losses = 0;
    private Integer saves = 0;
    private Integer holds = 0;
    private Double inningsPitched = 0.0;
    private Integer hits = 0;
    private Integer runs = 0;
    private Integer earnedRuns = 0;
    private Integer walks = 0;
    private Integer strikeouts = 0;
    private Integer homeRuns = 0;
    private Integer atBats = 0;
    private Integer sacrificeFlies = 0;
    
    protected PitchingRecord() {}
    
    public PitchingRecord(Player player, Integer season) {
        this.player = player;
        this.season = season;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Integer getSeason() {
        return season;
    }
    
    public Integer getGames() {
        return games;
    }
    
    public Integer getGamesStarted() {
        return gamesStarted;
    }
    
    public Integer getWins() {
        return wins;
    }
    
    public Integer getLosses() {
        return losses;
    }
    
    public Integer getSaves() {
        return saves;
    }
    
    public Integer getHolds() {
        return holds;
    }
    
    public Double getInningsPitched() {
        return inningsPitched;
    }
    
    public Integer getHits() {
        return hits;
    }
    
    public Integer getRuns() {
        return runs;
    }
    
    public Integer getEarnedRuns() {
        return earnedRuns;
    }
    
    public Integer getWalks() {
        return walks;
    }
    
    public Integer getStrikeouts() {
        return strikeouts;
    }
    
    public Integer getHomeRuns() {
        return homeRuns;
    }
    
    public Integer getAtBats() {
        return atBats;
    }
    
    public Integer getSacrificeFlies() {
        return sacrificeFlies;
    }
    
    public Double getEra() {
        return inningsPitched > 0 ? (earnedRuns * 9.0) / inningsPitched : 0.0;
    }
    
    public Double getWhip() {
        return inningsPitched > 0 ? (double) (walks + hits) / inningsPitched : 0.0;
    }
    
    public Double getWinningPercentage() {
        int totalDecisions = wins + losses;
        return totalDecisions > 0 ? (double) wins / totalDecisions : 0.0;
    }
    
    public Double getBabip() {
        int denominator = atBats - strikeouts - homeRuns + sacrificeFlies;
        return denominator > 0 ? (double) (hits - homeRuns) / denominator : 0.0;
    }
    
    public Double getK9() {
        return inningsPitched > 0 ? (strikeouts * 9.0) / inningsPitched : 0.0;
    }
    
    public void updateStats(Integer games, Integer gamesStarted, Integer wins, Integer losses,
                           Integer saves, Integer holds, Double inningsPitched, Integer hits,
                           Integer runs, Integer earnedRuns, Integer walks, Integer strikeouts,
                           Integer homeRuns, Integer atBats, Integer sacrificeFlies) {
        this.games = games;
        this.gamesStarted = gamesStarted;
        this.wins = wins;
        this.losses = losses;
        this.saves = saves;
        this.holds = holds;
        this.inningsPitched = inningsPitched;
        this.hits = hits;
        this.runs = runs;
        this.earnedRuns = earnedRuns;
        this.walks = walks;
        this.strikeouts = strikeouts;
        this.homeRuns = homeRuns;
        this.atBats = atBats;
        this.sacrificeFlies = sacrificeFlies;
    }
}