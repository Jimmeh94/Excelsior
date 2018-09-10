package excelsior.game.hotbars.duel;

import ecore.services.hotbar.Hotbar;
import excelsior.Excelsior;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import excelsior.game.user.UserPlayer;
import excelsior.game.user.scoreboard.ArenaDefaultPreset;
import excelsior.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class HotbarActiveTurn extends Hotbar {

    @Override
    public void setHotbar(Player player) {
        super.setHotbar(player);

        UserPlayer user = PlayerUtils.getUserPlayer(Bukkit.getPlayer(player.getUniqueId())).get();
        user.changeScoreboardPreset(new ArenaDefaultPreset(player, Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(player).get()));
    }

    @Override
    protected void setupItems() {
        ActionItemStack action;
        ItemMeta meta;

        /**
         * Need a free-cam mode to look around field, look at hand, look at discard pile
         * 2 = use passive
         * 4 = look at hand
         * 6 = discard pile
         * 8 = options menu (skip turn, leave game)
         */

        action = new ActionItemStack(Material.BLAZE_POWDER, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                //bring up ability hotbar
            }
        });
        meta = action.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Use Ability" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(2, action);

        action = new ActionItemStack(Material.EMPTY_MAP, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                //TODO TP player, stop from moving unless deactivated, load maps (cards)
                CombatantProfilePlayer temp = (CombatantProfilePlayer) Excelsior.INSTANCE.getArenaManager()
                        .findArenaWithPlayer(player).get().getCombatantProfile(player.getUniqueId()).get();
                UserPlayer userPlayer = PlayerUtils.getUserPlayer(Bukkit.getPlayer(player.getUniqueId())).get();
                userPlayer.setCurrentHotbar(new HotbarHand(temp));
                userPlayer.getCurrentHotbar().setHotbar(player);
            }
        });
        meta = action.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Look at Hand" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(4, action);

        action = new ActionItemStack(Material.BARRIER, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                //TODO load dyanmic inventory
            }
        });
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Look at Discard Pile" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(6, action);

        action = new ActionItemStack(Material.CLAY_BALL, new ActionItemStack.Callback() {
            @Override
            public void action(Player player, Action action) {
                //TODO load options hotbar
            }
        });
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Options Menu" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(8, action);
    }
}
