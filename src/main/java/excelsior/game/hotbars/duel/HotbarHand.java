package excelsior.game.hotbars.duel;

import ecore.services.hotbar.Hotbar;
import ecore.services.messages.Message;
import ecore.services.messages.ServiceMessager;
import excelsior.Excelsior;
import excelsior.game.hotbars.Hotbars;
import excelsior.game.match.field.Cell;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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
            meta = profile.getHand().viewCard(i).getMesh().getItemMeta();
            card = new ActionItemStack(profile.getHand().viewCard(i).getMesh().getType(), new ActionItemStack.Callback() {
                @Override
                public void action(Player player, Action action) {
                    if(action == Action.LEFT_CLICK_AIR) {
                        int index = player.getInventory().getHeldItemSlot();
                        Cell currentAim = ((CombatantProfilePlayer) Excelsior.INSTANCE.getArenaManager()
                                .findArenaWithPlayer(player).get().getCombatantProfile(player.getUniqueId()).get()).getCurrentAim();

                        if (currentAim != null && currentAim.isAvailable() && profile.getHand().hasCardAt(index)) {
                            currentAim.placeCard(profile.getHand().getCard(index));
                            player.getInventory().clear(index);
                        }
                    } else if(action == Action.RIGHT_CLICK_AIR){

                    }
                }
            });
            card.getItemMeta().setLore(meta.getLore());
            card.setItemMeta(meta);
            addItemWithAction(i, card);
        }

        card = new ActionItemStack(Material.BOOK_AND_QUILL, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                Message.MessageBuilder builder = Message.builder();
                builder.addRecipient(player.getUniqueId());
                builder.addMessage(" ");
                builder.addMessage(ChatColor.GRAY + "=====================================");
                builder.addMessage(" ");
                builder.addMessageAsChild(ChatColor.GOLD, "Left Click with a card in hand to place it on the field");
                builder.addMessageAsChild(ChatColor.GOLD, "Right Click with a card in hand to see the details");
                builder.addMessage(" ");
                builder.addMessageAsChild(ChatColor.GOLD, "Left/Right Click towards the field to get info about the target area");
                builder.addMessage(" ");
                builder.addMessage(ChatColor.GRAY + "=====================================");
                ServiceMessager.sendMessage(builder.build());
            }
        });
        meta = card.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Right Click for Help" + ChatColor.LIGHT_PURPLE + "]");
        card.setItemMeta(meta);
        addItemWithAction(7, card);

        card = new ActionItemStack(Material.CLAY_BALL, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                if(Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(player).get().isPlayersTurn(player)){
                    Hotbars.HOTBAR_ACTIVE_TURN.setHotbar(player);
                } else {
                    Hotbars.HOTBAR_WAITING_TURN.setHotbar(player);
                }
            }
        });
        meta = card.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Game Menu" + ChatColor.LIGHT_PURPLE + "]");
        card.setItemMeta(meta);
        addItemWithAction(8, card);
    }
}
