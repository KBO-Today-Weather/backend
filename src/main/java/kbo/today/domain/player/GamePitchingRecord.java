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
@Table(name = "game_pitching_records",
       uniqueConstraints = @UniqueConstraint(columnNames = {"player_id", "game_id"}))
public class GamePitchingRecord extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
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
    
    protected GamePitchingRecord() {}
    
    public GamePitchingRecord(Player player, Game game) {
        this.player = player;
        this.game = game;
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
    
    public Double getEra() {
        return inningsPitched > 0 ? (earnedRuns * 9.0) / inningsPitched : 0.0;
    }
    
    public Double getWhip() {
        return inningsPitched > 0 ? (double) (walks + hits) / inningsPitched : 0.0;
    }
    
    public void updateStats(Boolean isStarter, Integer wins, Integer losses,
                           Integer saves, Integer holds, Double inningsPitched, Integer hits,
                           Integer runs, Integer earnedRuns, Integer walks, Integer strikeouts,
                           Integer homeRuns) {
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
    }
}

