package excelsior.managers;

import excelsior.game.match.Arena;

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
}
