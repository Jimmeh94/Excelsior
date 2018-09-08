package excelsior.game.user;

import ecore.services.economy.EconomyAccount;
import ecore.services.particles.ServiceParticles;
import ecore.services.scoreboard.Scoreboard;
import ecore.services.user.PlayerInfo;
import excelsior.game.user.scoreboard.DefaultPreset;
import org.bukkit.entity.Player;

public class UserPlayer extends PlayerInfo {

    private EconomyAccount account;
    private Scoreboard scoreboard;

    public UserPlayer(Player player){
        super(player.getUniqueId(), ServiceParticles.ParticleModifier.NORMAL);

        account = new EconomyAccount(getUUID(), 100.00);

        scoreboard = new Scoreboard(player, new DefaultPreset(player));
    }

    public EconomyAccount getAccount() {
        return account;
    }
}
