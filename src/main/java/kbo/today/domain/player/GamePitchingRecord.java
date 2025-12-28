package kbo.today.domain.player;

import java.time.LocalDateTime;
import java.util.Objects;
import kbo.today.common.domain.BaseEntity;
import kbo.today.domain.game.Game;

public class GamePitchingRecord extends BaseEntity {
    
    private Player player;
    private Game game;
    private Boolean isStarter = false;
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
    
    protected GamePitchingRecord() {
        super();
    }
    
    protected GamePitchingRecord(Long id, Player player, Game game, Boolean isStarter,
                              Integer wins, Integer losses, Integer saves, Integer holds, Double inningsPitched,
                              Integer hits, Integer runs, Integer earnedRuns, Integer walks, Integer strikeouts,
                              Integer homeRuns, Integer atBats, Integer sacrificeFlies,
                              LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        super(id, createdAt, updatedAt, deletedAt);
        this.player = Objects.requireNonNull(player);
        this.game = Objects.requireNonNull(game);
        this.isStarter = isStarter != null ? isStarter : false;
        this.wins = wins != null ? wins : 0;
        this.losses = losses != null ? losses : 0;
        this.saves = saves != null ? saves : 0;
        this.holds = holds != null ? holds : 0;
        this.inningsPitched = inningsPitched != null ? inningsPitched : 0.0;
        this.hits = hits != null ? hits : 0;
        this.runs = runs != null ? runs : 0;
        this.earnedRuns = earnedRuns != null ? earnedRuns : 0;
        this.walks = walks != null ? walks : 0;
        this.strikeouts = strikeouts != null ? strikeouts : 0;
        this.homeRuns = homeRuns != null ? homeRuns : 0;
        this.atBats = atBats != null ? atBats : 0;
        this.sacrificeFlies = sacrificeFlies != null ? sacrificeFlies : 0;
    }
    
    public static GamePitchingRecord create(Player player, Game game) {
        return new GamePitchingRecord(null, player, game, false, 0, 0, 0, 0, 0.0,
                                    0, 0, 0, 0, 0, 0, 0, 0,
                                    null, null, null);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Game getGame() {
        return game;
    }
    
    public Boolean getIsStarter() {
        return isStarter;
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
    
    public Double getBabip() {
        int denominator = atBats - strikeouts - homeRuns + sacrificeFlies;
        return denominator > 0 ? (double) (hits - homeRuns) / denominator : 0.0;
    }
    
    public Double getK9() {
        return inningsPitched > 0 ? (strikeouts * 9.0) / inningsPitched : 0.0;
    }
    
    public void updateStats(Boolean isStarter, Integer wins, Integer losses,
                           Integer saves, Integer holds, Double inningsPitched, Integer hits,
                           Integer runs, Integer earnedRuns, Integer walks, Integer strikeouts,
                           Integer homeRuns, Integer atBats, Integer sacrificeFlies) {
        this.isStarter = isStarter;
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

