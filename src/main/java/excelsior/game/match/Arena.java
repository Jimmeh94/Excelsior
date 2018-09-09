package excelsior.game.match;

import excelsior.game.match.field.Grid;
import excelsior.game.match.gamemodes.Gamemode;

import java.util.UUID;

public class Arena {

    private Gamemode gamemode;
    private Grid grid;
    private UUID id;
    private String world;
    private boolean inUse = false;

    public Arena(Gamemode gamemode, Grid grid, String world) {
        this(gamemode, grid, world, UUID.randomUUID());
    }

    public Arena(Gamemode gamemode, Grid grid, String world, UUID id){
        this.gamemode = gamemode;
        this.grid = grid;
        this.world = world;
        this.id = id;
    }

    public void tick(){
        gamemode.baseTick();
    }

    public void updatePlayersAim() {
        gamemode.updatePlayersAim(grid);
    }

    public boolean isInUse() {
        return inUse;
    }

    public UUID getID() {
        return id;
    }

    public String getWorld() {
        return world;
    }

    public int getGamemodeID() {
        return gamemode.getID();
    }

    public Grid getGrid() {
        return grid;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public void end(){
        inUse = false;
        //TODO TP players out
    }

    public void start() {
        inUse = true;
        //TODO TP players to arena
        gamemode.start(grid.getStartPos());
    }
}
