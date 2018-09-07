package excelsior.game.cards;

import java.util.UUID;

public abstract class CardBase {

    private double level;
    private String name;
    private UUID owner;

    public CardBase(UUID owner, double level, String name) {
        this.owner = owner;
        this.level = level;
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public double getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
