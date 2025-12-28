package kbo.today.domain.player;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.game.Game;

@Entity
@Table(name = "game_batting_records", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "game_id"}))
public class GameBattingRecord extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
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
    
    protected GameBattingRecord() {}
    
    public GameBattingRecord(Player player, Game game) {
        this.player = player;
        this.game = game;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Game getGame() {
        return game;
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
    
    public void updateStats(Integer plateAppearances, Integer atBats, 
                           Integer hits, Integer doubles, Integer triples, Integer homeRuns,
                           Integer runs, Integer rbis, Integer walks, Integer strikeouts,
                           Integer stolenBases, Integer caughtStealing) {
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

