package excelsior.game.match;

import excelsior.game.match.field.Grid;
import excelsior.game.match.gamemodes.Gamemode;

import java.util.Optional;
import java.util.UUID;

public class Arena {

    private Gamemode gamemode;
    private Grid grid;
    private UUID id;
    private String world;
    private boolean inUse = false;

    public Arena(Grid grid, String world) {
        this(grid, world, UUID.randomUUID());
    }

    public Arena(Grid grid, String world, UUID id){
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

    public Optional<Integer> getGamemodeID() {
        if(gamemode == null){
            return Optional.empty();
        }
        return Optional.of(gamemode.getID());
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

        gamemode = null;
    }

    public void start(Gamemode gamemode) {
        inUse = true;
        this.gamemode = gamemode;
        gamemode.start(grid.getStartPos());
    }
}
