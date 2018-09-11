package excelsior.game.cards;

import excelsior.game.match.field.Cell;

import java.util.List;

public abstract class CardMovement {

    protected CardBase owner;
    protected int distanceInCells;

    public CardMovement(int distanceInCells) {
        this.distanceInCells = distanceInCells;
    }

    public void setOwner(CardBase owner) {
        this.owner = owner;
    }

    public abstract List<Cell> getAvailableSpaces();

}
