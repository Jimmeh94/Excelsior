package excelsior.game.match.field;

import ecore.services.ByteColors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A cell is a # x # area of blocks within a Grid
 */
public class Cell {

    private List<Vector> locations;
    private String world;
    private Material material;
    private byte data;
    private boolean isAvailable = true;

    public Cell(Vector startingPos, int xDem, int zDem, String world, Material material, byte data) {
        this.world = world;
        this.material = material;
        this.data = data;

        locations = new CopyOnWriteArrayList<>();

        Vector use = startingPos.clone();
        int x = startingPos.getBlockX();
        int z = startingPos.getBlockZ();
        for(int i = 0; i < xDem; i++, x++){
            use.setX(startingPos.getBlockX() + i);
            for(int j = 0; j < zDem; j++, z++){
                use.setZ(z);
                locations.add(use.clone());
            }
            z = startingPos.getBlockZ();
        }
    }

    public boolean isWithinCell(Vector check){
        for(Vector v: locations){
            if(v.getBlockX() == check.getBlockX() && v.getBlockY() == check.getBlockY() && v.getBlockZ() == check.getBlockZ()){
                return true;
            }
        }
        return false;
    }

    public void drawCell(){
        for(Vector v: locations){
            Block block = Bukkit.getWorld(world).getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ());
            block.setType(material);
            block.setData(data);
        }
    }

    public void destroyCell(){
        for(Vector v: locations){
            Bukkit.getWorld(world).getBlockAt(v.getBlockX(), v.getBlockY(), v.getBlockZ()).setType(Material.AIR);
        }
    }

    public void drawAimForPlayer(Player player){
        for(Vector v: getVectors()){
            player.sendBlockChange(new Location(Bukkit.getWorld(world), v.getX(), v.getY(), v.getZ()),
                    Material.STAINED_GLASS, ByteColors.RED);
        }
    }

    public void clearAimForPlayer(Player player){
        for (Vector v : getVectors()) {
            player.sendBlockChange(new Location(Bukkit.getWorld(world), v.getX(), v.getY(), v.getZ()),
                    getMaterial(), getData());
        }
    }

    public List<Vector> getVectors() {
        return locations;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    public Vector getCenter() {
        //Assuming that the cell size is odd
        int size = locations.size();
        return locations.get((size / 2) + 1);
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean b) {
        this.isAvailable = b;
    }
}
