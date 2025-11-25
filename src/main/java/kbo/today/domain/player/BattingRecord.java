package kbo.today.domain.player;

import jakarta.persistence.*;
import kbo.today.domain.common.BaseEntity;

@Entity
@Table(name = "batting_records")
public class BattingRecord extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @Column(nullable = false)
    private Integer season;
    
    private Integer games = 0;
    private Integer plateAppearances = 0;
    private Integer atBats = 0;
    private Integer hits = 0;
    private Integer doubles = 0;
    private Integer triples = 0;
    private Integer homeRuns = 0;
    private Integer runs = 0;
    private Integer rbis = 0;
    private Integer walks = 0;
    private Integer strikeouts = 0;
    private Integer stolenBases = 0;
    private Integer caughtStealing = 0;
    
    protected BattingRecord() {}
    
    public BattingRecord(Player player, Integer season) {
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
    
    public Integer getPlateAppearances() {
        return plateAppearances;
    }
    
    public Integer getAtBats() {
        return atBats;
    }
    
    public Integer getHits() {
        return hits;
    }
    
    public Integer getDoubles() {
        return doubles;
    }
    
    public Integer getTriples() {
        return triples;
    }
    
    public Integer getHomeRuns() {
        return homeRuns;
    }
    
    public Integer getRuns() {
        return runs;
    }
    
    public Integer getRbis() {
        return rbis;
    }
    
    public Integer getWalks() {
        return walks;
    }
    
    public Integer getStrikeouts() {
        return strikeouts;
    }
    
    public Integer getStolenBases() {
        return stolenBases;
    }
    
    public Integer getCaughtStealing() {
        return caughtStealing;
    }
    
    public Double getBattingAverage() {
        return atBats > 0 ? (double) hits / atBats : 0.0;
    }
    
    public Double getOnBasePercentage() {
        int totalPA = plateAppearances;
        return totalPA > 0 ? (double) (hits + walks) / totalPA : 0.0;
    }
    
    public Double getSluggingPercentage() {
        if (atBats == 0) return 0.0;
        int totalBases = hits + doubles + (triples * 2) + (homeRuns * 3);
        return (double) totalBases / atBats;
    }
    
    public void updateStats(Integer games, Integer plateAppearances, Integer atBats, 
                           Integer hits, Integer doubles, Integer triples, Integer homeRuns,
                           Integer runs, Integer rbis, Integer walks, Integer strikeouts,
                           Integer stolenBases, Integer caughtStealing) {
        this.games = games;
        this.plateAppearances = plateAppearances;
        this.atBats = atBats;
        this.hits = hits;
        this.doubles = doubles;
        this.triples = triples;
        this.homeRuns = homeRuns;
        this.runs = runs;
        this.rbis = rbis;
        this.walks = walks;
        this.strikeouts = strikeouts;
        this.stolenBases = stolenBases;
        this.caughtStealing = caughtStealing;
    }
}