package excelsior.game.match.field;

import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * A GridNormal contains a playing field of 11 x 11 cells, each cell separated by a 1 block wide divider
 */
public class GridNormal extends Grid {

    public GridNormal(Vector startingPos, String world) {
        super(startingPos, world);

        drawGrid();
    }

    @Override
    protected void GenerateCells(Vector startingPos) {

        Vector use = startingPos.clone();
        Vector endPosition = use.clone();
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                cells.add(new Cell(use, 3, 3, world));
                use.setZ(use.getBlockZ() + 4);
                //Plus 4 with 3 wide cells will leave a 1 block open area between each cell
                if(j == 10){
                    endPosition = use.clone();
                    endPosition.setZ(endPosition.getBlockZ());
                }
            }
            use.setZ(startingPos.getBlockZ());

            if(i < 10) {
                use.setX(use.getBlockX() + 4);
            } else {
                endPosition.setX(use.getBlockX() + 4);
            }
        }

        startingPos.setX(startingPos.getBlockX() - 1);
        startingPos.setZ(startingPos.getBlockZ() - 1);
        startingPos.setY(startingPos.getBlockY() - 1);
        endPosition.setY(endPosition.getBlockY() - 1);

        use = startingPos.clone();
        int sx = startingPos.getBlockX(), ex = endPosition.getBlockX();
        int sz = startingPos.getBlockZ(), ez = endPosition.getBlockZ();

        for(int i = 0; i < Math.max(sx, ex) - Math.min(sx, ex); i++){
            for(int j = 0; j < Math.max(sz, ez) - Math.min(sz, ez); j++){
                Vector temp = use.clone();
                temp.setY(temp.getBlockY() + 1);

                boolean needToPaint = true;
                for(Cell cell: cells){
                    if(needToPaint == true && cell.isWithinCell(temp)){
                        needToPaint = false;
                    }
                }
                if(needToPaint){
                    //So we just made sure that the block above this one isn't in a cell, in other words,
                    //This is a border between some cells
                    border.add(use.clone());
                }

                use.setZ(use.getBlockZ() + 1);
            }

            use.setZ(startingPos.getBlockZ());
            use.setX(use.getBlockX() + 1);
        }

    }
}
