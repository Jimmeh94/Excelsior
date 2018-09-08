package excelsior.game.chatchannels;

import ecore.services.messages.channels.ChatChannel;
import org.bukkit.ChatColor;

public class ChatChannelStaff extends ChatChannel {

    public ChatChannelStaff() {
        super(ChatColor.GRAY + "You joined the staff chat channel", ChatColor.GRAY + "Staff", ChatChannelKeys.StaffChannel);
    }
}
