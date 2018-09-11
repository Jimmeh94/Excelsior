package excelsior.game.cards;

import excelsior.Excelsior;
import excelsior.game.match.field.Cell;
import excelsior.game.match.field.Grid;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;

public class CardMovementDiagonal extends CardMovementNormal {

    public CardMovementDiagonal(int distanceInCells) {
        super(distanceInCells);
    }

    @Override
    public List<Cell> getAvailableSpaces() {
        List<Cell> cells = super.getAvailableSpaces();

        Grid grid = Excelsior.INSTANCE.getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        Cell current = owner.getCurrentCell();

        if(current != null){
            Optional<Cell> target;
            target = grid.getCellInDirection(current, new Vector(distanceInCells, 0, distanceInCells));
            if(target.isPresent()){
                cells.add(target.get());
            }

            target = grid.getCellInDirection(current, new Vector(distanceInCells, 0, -distanceInCells));
            if(target.isPresent()){
                cells.add(target.get());
            }

            target = grid.getCellInDirection(current, new Vector(-distanceInCells, 0, distanceInCells));
            if(target.isPresent()){
                cells.add(target.get());
            }

            target = grid.getCellInDirection(current, new Vector(-distanceInCells, 0, -distanceInCells));
            if(target.isPresent()){
                cells.add(target.get());
            }
        }

        return cells;
    }
}
