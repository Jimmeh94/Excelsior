package excelsior.game.cards.cards;

import excelsior.game.cards.CardBase;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardDummy extends CardBase {

    public CardDummy(UUID owner) {
        super(owner, 1, "Dummy Card", Material.EMPTY_MAP, (short)1.0);
    }

    @Override
    protected List<String> generateLore() {
        List<String> give = new ArrayList<>();
        give.add(ChatColor.GRAY + "Rarity: Basic");
        give.add(ChatColor.GRAY + "Level: 1");
        give.add(" ");
        give.add(ChatColor.GRAY + ChatColor.ITALIC.toString() + "This card is a place holder for testing purposes.");
        return give;
    }
}
