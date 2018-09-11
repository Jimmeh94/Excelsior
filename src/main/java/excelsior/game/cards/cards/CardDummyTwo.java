package excelsior.game.cards.cards;

import excelsior.game.cards.CardBase;
import excelsior.game.cards.CardMovementNormal;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummyTwo extends CardBase {

    public CardDummyTwo(UUID owner) {
        super(owner, 1, "Dummy 2 Card", Material.STONE, (short)1.0, new CardMovementNormal(1));
    }

    @Override
    protected List<String> generateLore() {
        List<String> give = new ArrayList<>();
        give.add(ChatColor.GRAY + "Rarity: Rare");
        give.add(ChatColor.GRAY + "Level: 2");
        give.add(" ");
        give.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "This card is a place holder for testing purposes.");
        return give;
    }
}
