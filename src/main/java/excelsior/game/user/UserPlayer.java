package excelsior.game.user;

import ecore.services.economy.EconomyAccount;
import ecore.services.particles.ServiceParticles;
import ecore.services.user.PlayerInfo;
import org.bukkit.entity.Player;

public class UserPlayer extends PlayerInfo {

    private EconomyAccount account;

    public UserPlayer(Player player){
        super(player.getUniqueId(), ServiceParticles.ParticleModifier.NORMAL);

        account = new EconomyAccount(getUUID(), 100.00);
    }

    public EconomyAccount getAccount() {
        return account;
    }
}
