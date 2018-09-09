package excelsior.game.match.profiles;

import excelsior.game.match.field.Cell;

import java.util.UUID;

public class CombatantProfilePlayer extends CombatantProfile {

    private Cell currentAim;

    public CombatantProfilePlayer(UUID owner) {
        super(owner);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public Cell getCurrentAim() {
        return currentAim;
    }

    public void setCurrentAim(Cell currentAim) {
        this.currentAim = currentAim;
    }
}
