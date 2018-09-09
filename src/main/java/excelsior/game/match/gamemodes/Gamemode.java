package excelsior.game.match.gamemodes;

import ecore.services.ByteColors;
import excelsior.game.match.Arena;
import excelsior.game.match.Team;
import excelsior.game.match.field.Cell;
import excelsior.game.match.field.Grid;
import excelsior.game.match.profiles.CombatantProfile;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class Gamemode {

    protected List<Team> teams;
    private int timeLimit; //in seconds
    private TurnManager turnManager;
    protected String world;
    protected Arena arena;

    public Gamemode(int timeLimit, int timeLimitForEachTurn, String world){
        teams = new ArrayList<>();

        this.timeLimit = timeLimit;
        turnManager = new TurnManager(timeLimitForEachTurn);
        this.world = world;
    }

    protected abstract void tick();
    protected abstract void endingGame();
    protected abstract void startingGame();

    /**
     * ID of the gamemode.
     * Duel = 0
     * Not sure if this will ever be used or not
     * @return
     */
    public abstract int getID();

    public void start(Vector start){
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    Bukkit.getPlayer(p.getUUID()).teleport(new Location(Bukkit.getWorld(world), start.getX(), start.getY(), start.getZ()));
                }
            }
        }
        startingGame();
    }

    public void endGame(){
        arena.getGrid().resetCells();
        endingGame();
        arena = null;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void baseTick(){
        timeLimit--;
        if(timeLimit == 0){
            endGame();
        } else {
            for(Team team: teams){
                if(team.isEmptyOfPlayers()){
                    //TODO if not a PlayerVsAI game or if players don't want them to be repalced by bots
                    //TODO game should end and the other team should win
                }
            }

            if(turnManager.needToStartNextTurn()){
                turnManager.startNextTurn(teams);
            }
        }
        tick();
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public void updatePlayersAim(Grid grid) {
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    CombatantProfilePlayer cpp = (CombatantProfilePlayer)p;
                    Player player = Bukkit.getPlayer(p.getUUID());
                    BlockIterator it = new BlockIterator(player.getEyeLocation(), 0, 100);
                    while(it.hasNext()){
                        Block block = it.next();
                        if(block.getType() != Material.AIR){
                            if(grid.isCell(block.getLocation().toVector())){
                                Cell cell = grid.getCell(block.getLocation().toVector()).get();
                                if(cpp.getCurrentAim() != null && cpp.getCurrentAim() == cell){
                                    continue;
                                }
                                if(cpp.getCurrentAim() != null) {
                                    for (Vector v : cpp.getCurrentAim().getVectors()) {
                                        player.sendBlockChange(new Location(Bukkit.getWorld(world), v.getX(), v.getY(), v.getZ()),
                                                cpp.getCurrentAim().getMaterial(), cpp.getCurrentAim().getData());
                                    }
                                }
                                cpp.setCurrentAim(cell);
                                for(Vector v: cpp.getCurrentAim().getVectors()){
                                    player.sendBlockChange(new Location(Bukkit.getWorld(world), v.getX(), v.getY(), v.getZ()),
                                            Material.STAINED_GLASS, ByteColors.RED);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isPlayerCombatant(Player player) {
        for(Team team: teams){
            if(team.isPlayerCombatant(player)){
                return true;
            }
        }
        return false;
    }

    public void playerQuit(Player player) {
        for(Team team: teams){
            if(team.isPlayerCombatant(player)){
                team.playerQuit(player);
            }
        }
        //TODO do something here. Give option to other players to continue, continue with bot, or leave
    }


    protected class TurnManager {

        private Team currentTurn;
        private int timeLimitForEachTurn;
        private int elapsedTime;

        public TurnManager(int timeLimitForEachTurn) {
            this.timeLimitForEachTurn = timeLimitForEachTurn;
        }

        public void startNextTurn(List<Team> teams){
            if(currentTurn == null){
                currentTurn = teams.get(0);
            } else {
                int index = teams.indexOf(currentTurn);
                if(index == teams.size() - 1){
                    currentTurn = teams.get(0);
                } else {
                    currentTurn = teams.get(index + 1);
                }
            }
            elapsedTime = 0;

            currentTurn.broadcastStartTurnMessage();

            for(Team team: teams){
                if(team == currentTurn){
                    continue;
                }
                team.broadcastEndTurnMessage(ChatColor.GRAY + "Other team's turn now");
            }
        }

        public boolean needToStartNextTurn(){
            elapsedTime++;
            if(elapsedTime >= timeLimitForEachTurn || currentTurn == null){
                return true;
            }
            return false;
        }
    }


}
