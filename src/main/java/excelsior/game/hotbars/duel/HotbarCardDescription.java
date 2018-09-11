package excelsior.game.hotbars.duel;

import ecore.services.hotbar.Hotbar;
import ecore.services.messages.Message;
import ecore.services.messages.ServiceMessager;
import excelsior.Excelsior;
import excelsior.game.cards.CardBase;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.meta.ItemMeta;

public class HotbarCardDescription extends Hotbar {

    private CardBase cardBase;
    private Hotbar previous;

    public HotbarCardDescription(CardBase cardBase, Hotbar previous) {
        this.cardBase = cardBase;
        this.previous = previous;

        setupItems();
    }

    @Override
    protected void setupItems() {
        if(cardBase == null){
            return;
        }

        ActionItemStack card;
        ItemMeta meta;


        card = new ActionItemStack(Material.WRITTEN_BOOK, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                Message.MessageBuilder builder = Message.builder();
                builder.addRecipient(player.getUniqueId());
                builder.addMessage(" ");
                builder.addMessage(ChatColor.GRAY + "[===== " + cardBase.getName() + ChatColor.GRAY + " =====]");
                builder.addMessage(" ");

                for(String s: cardBase.getMesh().getItemMeta().getLore()){
                    builder.addMessageAsChild(ChatColor.GOLD, s);
                }
                builder.addMessage(" ");

                builder.addMessage(ChatColor.GRAY + "[=====================================]");
                ServiceMessager.sendMessage(builder.build());
            }
        });
        meta = card.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Right Click for Card Info" + ChatColor.LIGHT_PURPLE + "]");
        card.setItemMeta(meta);
        addItem(3, card);

        card = new ActionItemStack(Material.CLAY_BALL, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                previous.setHotbar(player);
                ((CombatantProfilePlayer) Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(player).get().getCombatantProfile(player.getUniqueId()).get())
                        .setViewingClientArmorstand(null);
            }
        });
        meta = card.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Return to Previous Hotbar" + ChatColor.LIGHT_PURPLE + "]");
        card.setItemMeta(meta);
        addItem(6, card);
    }
}
