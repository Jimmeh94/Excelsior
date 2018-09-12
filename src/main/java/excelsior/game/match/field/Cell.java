package excelsior.game.match.field;

import ecore.services.ByteColors;
import ecore.services.location.ServiceLocationUtils;
import excelsior.game.cards.CardBase;
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
    private CardBase occupyingCard;
    private Vector center;

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

        center = ServiceLocationUtils.getMiddleLocation(locations.get(0), locations.get(locations.size() - 1));
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
            //block.setData(data);
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
                    Material.RED_STAINED_GLASS, ByteColors.RED);
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
        return center.clone();
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean b) {
        this.isAvailable = b;
        occupyingCard = null;
    }

    public CardBase getOccupyingCard() {
        return occupyingCard;
    }

    public void placeCard(CardBase card) {
        setAvailable(false);
        occupyingCard = card;
        occupyingCard.spawn3DRepresentationServer(new Location(Bukkit.getWorld(world), getCenter().getX(),
                getCenter().getY(), getCenter().getZ()));
        occupyingCard.setCurrentCell(this);
    }

    public void drawAvailableSpaceForPlayer(Player player) {
        for(Vector v: getVectors()){
            player.sendBlockChange(new Location(Bukkit.getWorld(world), v.getX(), v.getY(), v.getZ()),
                    Material.BLUE_STAINED_GLASS, ByteColors.BLUE);
        }
    }

    public String getWorld() {
        return world;
    }
}
