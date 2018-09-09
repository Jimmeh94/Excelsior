package excelsior.game.match.gamemodes;

import excelsior.game.match.Team;
import excelsior.game.match.profiles.CombatantProfile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.UUID;

public class GamemodeDuel extends Gamemode {


    public GamemodeDuel(String world) {
        //Time limit in seconds, each turn's time limit in seconds
        super(60 * 15, 60 * 1, world);
    }

    @Override
    protected void tick() {

    }

    @Override
    protected void endGame() {

    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void start(Vector start) {
        for(Team team: teams){
            for(CombatantProfile p: team.getCombatants()){
                if(p.isPlayer()){
                    Bukkit.getPlayer(p.getUUID()).teleport(new Location(Bukkit.getWorld(world), start.getX(), start.getY(), start.getZ()));
                }
            }
        }
    }
}
