package excelsior.game.hotbars.duel;

import excelsior.game.hotbars.Hotbar;
import excelsior.game.match.profiles.CombatantProfile;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class HotbarHand extends Hotbar {

    private CombatantProfilePlayer profile;

    public HotbarHand(CombatantProfilePlayer profile) {
        this.profile = profile;
    }

    @Override
    public void setHotbar(Player player) {
        setupItems();

        super.setHotbar(player);
    }

    @Override
    protected void setupItems() {
        if(profile == null){
            return;
        }

        ActionItemStack card;
        ItemMeta meta;

        for(int i = 0; i < profile.getHand().getSize(); i++){
            meta = profile.getHand().getCard(i).getMesh().getItemMeta();
            card = new ActionItemStack(profile.getHand().viewCard(i).getMesh().getType(), new Consumer<Player>() {
                @Override
                public void accept(Player player) {

                }
            });
            card.getItemMeta().setLore(meta.getLore());
            card.setItemMeta(meta);
            addItemWithAction(i, card);
        }
    }
}
