package excelsior.managers;

import excelsior.game.match.Arena;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class ManagerArena extends Manager<Arena> {

    public void tick(){
        for(Arena a: objects){
            if(a.isInUse()) {
                a.tick();
            }
        }
    }

    public void updatePlayersAim() {
        for(Arena a: objects){
            if(a.isInUse()) {
                a.updatePlayersAim();
            }
        }
    }

    public Optional<Arena> getAvailableArena() {
        for(Arena a: objects){
            if(!a.isInUse()){
                return Optional.of(a);
            }
        }
        return Optional.empty();
    }

    public void checkPlayer(Player player) {
        for(Arena arena: objects){
            if(arena.isInUse()){
               if(arena.isPlayerCombatant(player)){
                    arena.playerQuit(player);
               }
            }
        }
    }

    public Optional<Arena> findArenaWithPlayer(Player player) {
        for(Arena arena: objects){
            if(arena.isPlayerCombatant(player)){
                return Optional.of(arena);
            }
        }
        return Optional.empty();
    }
}
