package excelsior.game.hotbars.duel;

import ecore.ECore;
import excelsior.Excelsior;
import excelsior.game.hotbars.Hotbar;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import excelsior.game.user.UserPlayer;
import excelsior.game.user.scoreboard.ArenaDefaultPreset;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class HotbarActiveTurn extends Hotbar {

    @Override
    public void setHotbar(Player player) {
        super.setHotbar(player);

        UserPlayer user = (UserPlayer) ECore.INSTANCE.getUsers().findPlayerInfo(player.getUniqueId()).get();
        user.changeScoreboardPreset(new ArenaDefaultPreset(player, Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(player).get()));
    }

    @Override
    protected void setupItems() {
        ActionItemStack action;
        ItemMeta meta;

        /**
         * Need a free-cam mode to look around field, look at hand, look at discard pile
         * 2 = free cam
         * 4 = look at hand
         * 6 = discard pile
         * 8 = options menu (skip turn, leave game)
         */

        action = new ActionItemStack(Material.COMPASS, new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                //TODO activate free-cam mode for player
                //TODO load free cam hotbar
            }
        });
        meta = action.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Free Cam Mode" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(2, action);

        action = new ActionItemStack(Material.EMPTY_MAP, new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                //TODO load deck hotbar
                //TODO TP player, stop from moving unless deactivated, load maps (cards)
                CombatantProfilePlayer temp = (CombatantProfilePlayer) Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(player).get().getCombatantProfile(player.getUniqueId()).get();
                temp.getHotbarHand().setHotbar(player);
            }
        });
        meta = action.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Look at Hand" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(4, action);

        action = new ActionItemStack(Material.BARRIER, new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                //TODO load discard hotbar
                //TODO load card display
            }
        });
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Look at Discard Pile" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(6, action);

        action = new ActionItemStack(Material.DARK_OAK_DOOR, new Consumer<Player>() {
            @Override
            public void accept(Player player) {
                //TODO load options hotbar
            }
        });
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "[" + ChatColor.GRAY + "Options Menu" + ChatColor.LIGHT_PURPLE + "]");
        action.setItemMeta(meta);
        addItemWithAction(8, action);
    }
}
