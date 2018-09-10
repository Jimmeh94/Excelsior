package excelsior.game.match.profiles;

import excelsior.game.cards.Deck;
import excelsior.game.hotbars.duel.HotbarHand;
import excelsior.game.match.field.Cell;

import java.util.UUID;

public class CombatantProfilePlayer extends CombatantProfile {

    private Cell currentAim;
    private HotbarHand hotbarHand;

    public CombatantProfilePlayer(UUID owner, Deck deck) {
        super(owner, deck);
    }

    public HotbarHand getHotbarHand() {
        if(hotbarHand == null){
            hotbarHand = new HotbarHand(this);
        }
        return hotbarHand;
    }

    public Cell getCurrentAim() {
        return currentAim;
    }

    public void setCurrentAim(Cell currentAim) {
        this.currentAim = currentAim;
    }
}
