package excelsior.events;

import ecore.ECore;
import excelsior.game.chatchannels.ChatChannelKeys;
import excelsior.game.user.UserPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage(null);

        ECore.INSTANCE.getUsers().add(new UserPlayer(event.getPlayer()));
        ECore.INSTANCE.getChannels().findChannel(ChatChannelKeys.GlobalChannel).get().add(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        event.setQuitMessage(null);

        ECore.INSTANCE.playerQuit(event.getPlayer());
    }

}
