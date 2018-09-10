package excelsior.utils;

import ecore.ECore;
import ecore.services.user.PlayerInfo;
import excelsior.game.user.UserPlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class PlayerUtils {

    public static Optional<UserPlayer> getUserPlayer(Player player){
        return getUserPlayer(player.getUniqueId());
    }

    public static Optional<UserPlayer> getUserPlayer(UUID uuid){
        Optional<PlayerInfo> temp = ECore.INSTANCE.getUsers().findPlayerInfo(uuid);

        return temp.isPresent() ? Optional.of((UserPlayer)temp.get()) : Optional.empty();
    }

}
