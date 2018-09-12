package excelsior.game.match.field;

import ecore.services.ByteColors;
import excelsior.game.cards.CardBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.material.Directional;
import org.bukkit.util.Vector;

import java.util.ArrayList;
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
    protected Material gridBorder, cellMat;


    public Grid(Vector startingPos, String world, int gridDemX, int gridDemZ, int cellDemX, int cellDemZ, boolean drawGrid,
                Material gridBorder, Material cellMat){
        this.startPos = startingPos;
        this.world = world;
        this.gridDemX = gridDemX;
        this.gridDemZ = gridDemZ;
        this.cellDemX = cellDemX;
        this.cellDemZ = cellDemZ;
        this.gridBorder = gridBorder;
        this.cellMat = cellMat;
        cells = new CopyOnWriteArrayList<>();
        border = new CopyOnWriteArrayList<>();

        GenerateCells(startingPos);

        if(drawGrid){
            drawGrid();
        }
    }

    public Optional<Cell> getCellInDirection(Cell current, Vector direction){
        double xDistance = cellDemX * direction.getX();
        xDistance += 1 * direction.getX();

        double zDistance = cellDemZ * direction.getZ();
        zDistance += 1 * direction.getZ();

        //direction.setX(direction.getX() * ((cellDemX * direction.getX()) + (1 * direction.getX())));
        //direction.setZ(direction.getZ() * ((cellDemZ * direction.getZ()) + (1 * direction.getZ())));

        Vector target = current.getCenter().clone().add(new Vector(xDistance, 0, zDistance));
        if(isCell(target)){
            Cell cell = getCell(target).get();
            if(cell.isAvailable()){
                return Optional.of(cell);
            }
        }
        return Optional.empty();
    }

    public List<Cell> getSquareGroupofCells(Vector start, int distanceInCells) {
        List<Cell> cells = new ArrayList<>();

        double tempDistance = cellDemX + 1;
        start.setX(start.getX() - (tempDistance * distanceInCells));
        start.setZ(start.getZ() - (tempDistance * distanceInCells));

        Vector use = start.clone();

        //Now we have bottom right corner of area
        int limit = distanceInCells * 2;
        limit += 1;

        for(int i = 0; i < limit; i++){
            //this first loop will increase z by tempDistance each time
            for(int j = 0; j < limit; j++){
                //this second loop will incrase x by tempDistance each time
                if(isCell(use)){
                    Cell cell = getCell(use).get();
                    if(cell.isAvailable()){
                        cells.add(cell);
                    }
                }
                use.setX(use.getX() + tempDistance);
            }
            use.setX(start.getX());
            use.setZ(use.getZ() + tempDistance);
        }

        return cells;
    }

    public void drawGrid(){
        for(Cell cell: cells){
            cell.drawCell();
        }
        for(Vector v: border){
            Block block = Bukkit.getWorld(world).getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ());
            block.setType(gridBorder);
            //block.setData(gridBorderData);
        }
    }

    public List<Cell> getAdjacentAvailableCells(Cell current){
        Vector center = current.getCenter();
        List<Cell> give = new ArrayList<>();

        //get cell in +x, -x, +z, -z
        Vector use = center.clone();
        use.setX(use.getBlockX() + cellDemX + 1);
        if(isCell(use)){
            Cell target = getCell(use).get();
            if(target.isAvailable()){
                give.add(target);
            }
        }

        return give;
    }

    public Material getGridBorder() {
        return gridBorder;
    }

    public Material getCellMat() {
        return cellMat;
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

    public void resetCells() {
        for(Cell cell: cells){
            cell.setAvailable(true);
        }
    }

    public void displayAvailableCellsToMoveTo(CardBase cardBase) {
        List<Cell> cells = cardBase.getMovement().getAvailableSpaces();
        cardBase.getMovement().setCurrentlyHighlighed(cells);
        for(Cell cell: cells){
            cell.drawAvailableSpaceForPlayer(Bukkit.getPlayer(cardBase.getOwner()));
        }
    }

    public void eraseAvailableCellsToMoveTo(CardBase cardBase){
        for(Cell cell: cells){
            cell.clearAimForPlayer(Bukkit.getPlayer(cardBase.getOwner()));
        }
    }
}
