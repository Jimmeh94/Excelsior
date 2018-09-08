package excelsior.game.match.field;

import ecore.services.ByteColors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Grid is made up of Cells
 */
public abstract class Grid {

    protected List<Cell> cells;
    protected List<Vector> border;
    protected String world;

    public Grid(Vector startingPos, String world){
        this.world = world;
        cells = new CopyOnWriteArrayList<>();
        border = new CopyOnWriteArrayList<>();

        GenerateCells(startingPos);
    }

    public void drawGrid(){
        for(Cell cell: cells){
            cell.drawCell(Material.BARRIER);
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
}
