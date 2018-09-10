package excelsior.game.cards.cards;

import excelsior.game.cards.CardBase;
import org.bukkit.Material;

import java.util.UUID;

public class CardDummy extends CardBase {

    public CardDummy(UUID owner) {
        super(owner, 1, "Dummy Card", Material.EMPTY_MAP, (short)1.0);
    }
}
