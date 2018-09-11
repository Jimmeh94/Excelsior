package excelsior.game.cards;

import excelsior.Excelsior;
import excelsior.game.match.field.Cell;
import excelsior.game.match.field.Grid;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Normal movement is x cells forward, back, left and right
 */
public class CardMovementNormal extends CardMovement {

    public CardMovementNormal(int distanceInCells) {
        super(distanceInCells);
    }

    @Override
    public List<Cell> getAvailableSpaces() {
        List<Cell> cells = new ArrayList<>();

        Grid grid = Excelsior.INSTANCE.getArenaManager().findArenaWithCombatant(owner.getOwner()).get().getGrid();
        Cell current = owner.getCurrentCell();

        if(current != null){
            Optional<Cell> target;
            target = grid.getCellInDirection(current, new Vector(distanceInCells, 0, 0));
            if(target.isPresent()){
                cells.add(target.get());
            }

            target = grid.getCellInDirection(current, new Vector(-distanceInCells, 0, 0));
            if(target.isPresent()){
                cells.add(target.get());
            }

            target = grid.getCellInDirection(current, new Vector(0, 0, distanceInCells));
            if(target.isPresent()){
                cells.add(target.get());
            }

            target = grid.getCellInDirection(current, new Vector(0, 0, -distanceInCells));
            if(target.isPresent()){
                cells.add(target.get());
            }
        }

        return cells;
    }
}
