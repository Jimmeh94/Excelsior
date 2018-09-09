package excelsior.game.match.gamemodes;

import ecore.services.ByteColors;
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
import java.util.UUID;

public abstract class Gamemode {

    //TODO Make sure players are still connected. If one leaves, remove from team replace with bot
    //TODO If whole team leaves, end game or option to replace with bot

    protected List<Team> teams;
    private int timeLimit; //in seconds
    private TurnManager turnManager;
    protected String world;

    public Gamemode(int timeLimit, int timeLimitForEachTurn, String world){
        teams = new ArrayList<>();

        this.timeLimit = timeLimit;
        turnManager = new TurnManager(timeLimitForEachTurn);
        this.world = world;
    }

    protected abstract void tick();
    protected abstract void endGame();
    public abstract int getID();
    public abstract void start(Vector start);

    public void baseTick(){

        timeLimit--;
        if(timeLimit == 0){
            endGame();
        } else {
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
