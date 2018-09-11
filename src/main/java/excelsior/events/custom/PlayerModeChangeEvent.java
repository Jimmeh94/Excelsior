package excelsior.events.custom;

import ecore.events.CustomEvent;
import excelsior.game.user.UserPlayer;
import org.bukkit.entity.Player;

public class PlayerModeChangeEvent extends CustomEvent {

    private UserPlayer.PlayerMode changeFrom, changeTo;
    private Player player;

    public PlayerModeChangeEvent(String cause, UserPlayer.PlayerMode changeFrom, UserPlayer.PlayerMode changeTo, Player player) {
        super(cause);

        this.changeFrom = changeFrom;
        this.changeTo = changeTo;
        this.player = player;
    }

    public UserPlayer.PlayerMode getChangeFrom() {
        return changeFrom;
    }

    public UserPlayer.PlayerMode getChangeTo() {
        return changeTo;
    }

    public Player getPlayer() {
        return player;
    }
}
