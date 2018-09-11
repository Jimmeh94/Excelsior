package excelsior.utils;

import ecore.ECore;
import ecore.services.user.PlayerInfo;
import excelsior.Excelsior;
import excelsior.game.match.profiles.CombatantProfile;
import excelsior.game.match.profiles.CombatantProfilePlayer;
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

    public static Optional<CombatantProfilePlayer> getCombatProfilePlayer(UUID uuid){
        Optional<CombatantProfile> c = Excelsior.INSTANCE.getArenaManager().findArenaWithCombatant(uuid).get().getCombatantProfile(uuid);

        return c.isPresent() && c.get().isPlayer() ? Optional.of((CombatantProfilePlayer)c.get()) : Optional.empty();
    }

}
