package excelsior.game.match.field;

import ecore.services.ByteColors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Grid is made up of Cells
 */
public abstract class Grid {

    protected List<Cell> cells;
    protected List<Vector> border;
    protected String world;
    protected int gridDemX, gridDemZ, cellDemX, cellDemZ;
    protected Vector startPos;

    public Grid(Vector startingPos, String world, int gridDemX, int gridDemZ, int cellDemX, int cellDemZ, boolean drawGrid){
        this.startPos = startingPos;
        this.world = world;
        this.gridDemX = gridDemX;
        this.gridDemZ = gridDemZ;
        this.cellDemX = cellDemX;
        this.cellDemZ = cellDemZ;
        cells = new CopyOnWriteArrayList<>();
        border = new CopyOnWriteArrayList<>();

        GenerateCells(startingPos);

        if(drawGrid){
            drawGrid();
        }
    }

    public void drawGrid(){
        for(Cell cell: cells){
            cell.drawCell();
        }
        for(Vector v: border){
            Block block = Bukkit.getWorld(world).getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ());
            block.setType(Material.STAINED_GLASS);
            block.setData(ByteColors.BLACK);
        }
    }

    public String getWorld() {
        return world;
    }

    protected abstract void GenerateCells(Vector startingPos);

    public List<Cell> getCells() {
        return cells;
    }

    public boolean isCell(Vector toVector) {
        for(Cell c: cells){
            if(c.isWithinCell(toVector)){
                return true;
            }
        }
        return false;
    }

    public Optional<Cell> getCell(Vector toVector) {
        for(Cell c: cells){
            if(c.isWithinCell(toVector)){
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public Vector getStartPos() {
        return startPos;
    }

    public int getGridX() {
        return gridDemX;
    }

    public int getGridZ(){
        return gridDemZ;
    }

    public int getCellX(){
        return cellDemX;
    }

    public int getCellZ(){
        return cellDemZ;
    }
}
