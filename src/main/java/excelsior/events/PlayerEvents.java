package excelsior.events;

import ecore.ECore;
import ecore.services.hotbar.Hotbar;
import excelsior.Excelsior;
import excelsior.game.chatchannels.ChatChannelKeys;
import excelsior.game.hotbars.HotbarArenaAdd;
import excelsior.game.hotbars.Hotbars;
import excelsior.game.hotbars.duel.HotbarActiveTurn;
import excelsior.game.hotbars.duel.HotbarCardDescription;
import excelsior.game.hotbars.duel.HotbarHand;
import excelsior.game.hotbars.duel.HotbarWaitingTurn;
import excelsior.game.user.UserPlayer;
import excelsior.utils.PlayerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);

        ECore.INSTANCE.getUsers().add(new UserPlayer(event.getPlayer()));
        ECore.INSTANCE.getChannels().findChannel(ChatChannelKeys.GlobalChannel).get().add(event.getPlayer().getUniqueId());
        Hotbars.HOTBAR_DEFAULT.setHotbar(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        event.setQuitMessage(null);

        Excelsior.INSTANCE.getArenaManager().checkPlayer(event.getPlayer());

        ECore.INSTANCE.playerQuit(event.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        event.setCancelled(true);

        UserPlayer userPlayer = PlayerUtils.getUserPlayer(event.getPlayer()).get();
        int index = event.getPlayer().getInventory().getHeldItemSlot();
        Hotbar current = userPlayer.getCurrentHotbar();

        if(userPlayer.getPlayerMode() == UserPlayer.PlayerMode.ARENA_ADD){
            if(current instanceof HotbarArenaAdd){
                current.handle(index, event.getPlayer(), event.getAction());
            }
        }
        else if(userPlayer.getPlayerMode() == UserPlayer.PlayerMode.ARENA_DUEL){
            if(current instanceof HotbarActiveTurn || current instanceof HotbarWaitingTurn || current instanceof HotbarHand ||
                current instanceof HotbarCardDescription){
                current.handle(index, event.getPlayer(), event.getAction());
            }
        } else if(userPlayer.getPlayerMode() == UserPlayer.PlayerMode.NORMAL){

        }
    }
}
