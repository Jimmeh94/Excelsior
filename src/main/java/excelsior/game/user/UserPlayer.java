package excelsior.game.user;

import ecore.services.economy.EconomyAccount;
import ecore.services.hotbar.Hotbar;
import ecore.services.particles.ServiceParticles;
import ecore.services.scoreboard.Scoreboard;
import ecore.services.scoreboard.presets.ScoreboardPreset;
import ecore.services.user.PlayerInfo;
import excelsior.game.cards.Deck;
import excelsior.game.cards.decks.DeckDummy;
import excelsior.game.user.scoreboard.DefaultPreset;
import org.bukkit.entity.Player;

public class UserPlayer extends PlayerInfo {

    private EconomyAccount account;
    private Scoreboard scoreboard;
    private Deck deck;
    private PlayerMode playerMode;
    private Hotbar currentHotbar;

    public UserPlayer(Player player){
        super(player.getUniqueId(), ServiceParticles.ParticleModifier.NORMAL);

        playerMode = PlayerMode.NORMAL;
        account = new EconomyAccount(getUUID(), 100.00);
        deck = new DeckDummy(getUUID());

        scoreboard = new Scoreboard(player, new DefaultPreset(player));
    }

    public void setCurrentHotbar(Hotbar currentHotbar) {
        this.currentHotbar = currentHotbar;
    }

    public Hotbar getCurrentHotbar() {
        return currentHotbar;
    }

    public void setPlayerMode(PlayerMode playerMode) {
        this.playerMode = playerMode;
    }

    public PlayerMode getPlayerMode() {
        return playerMode;
    }

    public Deck getDeck() {
        return deck;
    }

    public EconomyAccount getAccount() {
        return account;
    }

    public void changeScoreboardPreset(ScoreboardPreset preset){
        scoreboard.setPreset(preset);
    }

    public void updateScoreboard() {
        scoreboard.updateScoreboard();
    }

    /**
     * This just makes it easier for dividing up the interact events
     */
    public enum PlayerMode{
        ARENA_ADD,
        ARENA_DUEL,
        NORMAL
    }
}
