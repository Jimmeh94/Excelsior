package excelsior.game.hotbars.duel;

import ecore.services.hotbar.Hotbar;
import ecore.services.messages.Message;
import ecore.services.messages.ServiceMessager;
import excelsior.Excelsior;
import excelsior.game.cards.CardBase;
import excelsior.game.hotbars.Hotbars;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import excelsior.game.user.UserPlayer;
import excelsior.utils.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.meta.ItemMeta;

public class HotbarCardManipulate extends Hotbar {

    private CardBase cardBase;

    public HotbarCardManipulate(CardBase cardBase) {
        this.cardBase = cardBase;

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
                //Activate card passive ability
            }
        });
        meta = card.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Right Click to use Card Ability" + ChatColor.LIGHT_PURPLE + "]");
        card.setItemMeta(meta);
        addItem(1, card);


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
        addItem(6, card);

        card = new ActionItemStack(Material.CLAY_BALL, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                PlayerUtils.getUserPlayer(player.getUniqueId()).get().setPlayerMode(UserPlayer.PlayerMode.ARENA_DUEL_DEFAULT);
            }
        });
        meta = card.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Return to Duel Menu" + ChatColor.LIGHT_PURPLE + "]");
        card.setItemMeta(meta);
        addItem(8, card);
    }
}
