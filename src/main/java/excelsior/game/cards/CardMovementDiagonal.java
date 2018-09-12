package excelsior.game.cards;

import excelsior.Excelsior;
import excelsior.game.match.field.Cell;
import excelsior.game.match.field.Grid;
import org.bukkit.util.Vector;

import java.util.List;

public class CardMovementDiagonal extends CardMovementNormal {

    public CardMovementDiagonal(int distanceInCells) {
        super(distanceInCells);
    }

    @Override
    public List<Cell> getAvailableSpaces() {
        Vector start = owner.getCurrentCell().getCenter();
        Grid grid = Excelsior.INSTANCE.getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        return grid.getSquareGroupofCells(start, distanceInCells);
    }
}
