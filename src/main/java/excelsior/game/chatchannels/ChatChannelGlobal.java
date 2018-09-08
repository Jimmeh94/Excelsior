package excelsior.game.chatchannels;

import ecore.services.messages.channels.ChatChannel;
import org.bukkit.ChatColor;

public class ChatChannelGlobal extends ChatChannel {

    public ChatChannelGlobal() {
        super(ChatColor.GRAY + "You joined the global chat channel", ChatColor.GRAY + "Global", ChatChannelKeys.GlobalChannel);
    }
}
