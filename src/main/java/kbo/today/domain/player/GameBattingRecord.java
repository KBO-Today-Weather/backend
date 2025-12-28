package kbo.today.domain.player;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.game.Game;

public class GameBattingRecord extends BaseEntity {
    
    private Player player;
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
    private Integer sacrificeFlies = 0;
    private Integer sacrificeBunts = 0;
    
    protected GameBattingRecord() {
        super();
    }
    
    protected GameBattingRecord(Long id, Player player, Game game, Integer plateAppearances, Integer atBats,
                             Integer hits, Integer doubles, Integer triples, Integer homeRuns,
                             Integer runs, Integer rbis, Integer walks, Integer strikeouts,
                             Integer stolenBases, Integer caughtStealing, Integer sacrificeFlies, Integer sacrificeBunts,
                             LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.player = Objects.requireNonNull(player);
        this.game = Objects.requireNonNull(game);
        this.plateAppearances = plateAppearances != null ? plateAppearances : 0;
        this.atBats = atBats != null ? atBats : 0;
        this.hits = hits != null ? hits : 0;
        this.doubles = doubles != null ? doubles : 0;
        this.triples = triples != null ? triples : 0;
        this.homeRuns = homeRuns != null ? homeRuns : 0;
        this.runs = runs != null ? runs : 0;
        this.rbis = rbis != null ? rbis : 0;
        this.walks = walks != null ? walks : 0;
        this.strikeouts = strikeouts != null ? strikeouts : 0;
        this.stolenBases = stolenBases != null ? stolenBases : 0;
        this.caughtStealing = caughtStealing != null ? caughtStealing : 0;
        this.sacrificeFlies = sacrificeFlies != null ? sacrificeFlies : 0;
        this.sacrificeBunts = sacrificeBunts != null ? sacrificeBunts : 0;
    }
    
    public static GameBattingRecord create(Player player, Game game) {
        return new GameBattingRecord(null, player, game, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                    null, null, null);
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
    
    public Integer getSacrificeFlies() {
        return sacrificeFlies;
    }
    
    public Integer getSacrificeBunts() {
        return sacrificeBunts;
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
    
    public Double getOps() {
        return getOnBasePercentage() + getSluggingPercentage();
    }
    
    public Double getWalkRate() {
        return plateAppearances > 0 ? (double) walks / plateAppearances : 0.0;
    }
    
    public void updateStats(Integer plateAppearances, Integer atBats, 
                           Integer hits, Integer doubles, Integer triples, Integer homeRuns,
                           Integer runs, Integer rbis, Integer walks, Integer strikeouts,
                           Integer stolenBases, Integer caughtStealing, Integer sacrificeFlies,
                           Integer sacrificeBunts) {
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
        this.sacrificeFlies = sacrificeFlies;
        this.sacrificeBunts = sacrificeBunts;
    }
}

