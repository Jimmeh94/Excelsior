package excelsior.game.chatchannels;

import ecore.services.messages.channels.ChatChannel;
import org.bukkit.ChatColor;

public class ChatChannelAuction extends ChatChannel {

    public ChatChannelAuction() {
        super(ChatColor.GRAY + "You joined the auction chat channel", ChatColor.GRAY + "Auction", ChatChannelKeys.AuctionChannel);
    }
}
