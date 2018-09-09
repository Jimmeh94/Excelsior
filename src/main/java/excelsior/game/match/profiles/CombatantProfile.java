package excelsior.game.match.profiles;

import excelsior.game.cards.CardBase;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class CombatantProfile {

    private UUID owner;
    private List<CardBase> deck;

    private double health, energy;

    public CombatantProfile(UUID owner) {
        this.owner = owner;

        deck = new CopyOnWriteArrayList<>();

        health = 800;
        energy = 100;
    }

    public abstract boolean isPlayer();

    public UUID getUUID() {
        return owner;
    }
}
