package excelsior.game.cards;

import ecore.services.NMSUtils;
import ecore.services.Pair;
import excelsior.Excelsior;
import excelsior.game.match.field.Cell;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class CardBase {

    private double level;
    private String name;
    private UUID owner;
    private Pair<EntityArmorStand, List<UUID>> clientArmorStand;
    private EntityArmorStand description;
    private Material material;
    private short materialDamageValue;
    private ItemStack mesh;
    private Cell currentCell;
    private CardMovement cardMovement;

    public CardBase(UUID owner, double level, String name, Material material, short materialDamageValue, CardMovement cardMovement) {
        this.owner = owner;
        this.level = level;
        this.name = name;
        this.material = material;
        this.materialDamageValue = materialDamageValue;
        this.cardMovement = cardMovement;
        this.cardMovement.setOwner(this);

        generateItemStack();
    }

    protected abstract List<String> generateLore();

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void generateItemStack(){
        mesh = new ItemStack(material);
        mesh.setDurability(materialDamageValue);
        ItemMeta meta = mesh.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(generateLore());
        mesh.setItemMeta(meta);
    }

    public ItemStack getMesh() {
        return mesh;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwnerPlayer(){
        return Bukkit.getPlayer(owner) != null;
    }

    public double getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    /**
     * This is the clientArmorStand that is spawned when laid on the field.
     * This one should be used for when a card is face down, hidden, etc.
     * @param spawnLocation
     */
    public void spawn3DRepresentationClient(Location spawnLocation, List<UUID> viewers){
        if(clientArmorStand == null){
            EntityArmorStand e = new EntityArmorStand(((CraftWorld)spawnLocation.getWorld()).getHandle(), spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ());
            e.setInvisible(true);
            e.setBasePlate(false);
            e.setCustomName(name);
            e.setCustomNameVisible(true);

            clientArmorStand = new Pair<>(e, viewers);
        }

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(clientArmorStand.getFirst());
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(clientArmorStand.getFirst().getId(), EnumItemSlot.HEAD, NMSUtils.getNMSCopy(mesh));
        for(UUID c: clientArmorStand.getSecond()){
            Player player = Bukkit.getPlayer(c);
            if(player != null){
                PlayerConnection connection = NMSUtils.getPlayerConnection(player);
                connection.sendPacket(packet);
                connection.sendPacket(equipment);
            }
        }
    }

    public void add3DClientViewer(UUID uuid){
        if(!clientArmorStand.getSecond().contains(uuid)){
            clientArmorStand.getSecond().add(uuid);
        }
    }

    public void remove3DClientViewer(UUID uuid){
        clientArmorStand.getSecond().remove(uuid);

        PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(clientArmorStand.getFirst().getId());
        NMSUtils.getPlayerConnection(Bukkit.getPlayer(uuid)).sendPacket(packet);
    }

    public void move3DRepresentationClient(long deltaX, long deltaY, long deltaZ){
        PacketPlayOutEntity.PacketPlayOutRelEntityMove packet = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(clientArmorStand.getFirst().getId(),
                deltaX, deltaY, deltaZ, true);

        for(UUID uuid: clientArmorStand.getSecond()){
            NMSUtils.getPlayerConnection(Bukkit.getPlayer(uuid)).sendPacket(packet);
        }
    }

    /**
     * This will display the 3D view in front of the card owner with a book/chat with details of the description
     * @param location
     */
    public void displayCardDescription(Location location){
        Player player = Bukkit.getPlayer(owner);
        if(player == null){
            return;
        }

        if(clientArmorStand != null && clientArmorStand.getFirst() != null){
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(clientArmorStand.getFirst().getId());
            NMSUtils.getPlayerConnection(player).sendPacket(packet);
        }

        description = new EntityArmorStand(((CraftWorld)location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
        description.setInvisible(true);
        description.setBasePlate(false);
        description.setCustomName(name);
        description.setCustomNameVisible(true);

        clientArmorStand = new Pair<>(description, Arrays.asList(owner));

        PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(clientArmorStand.getFirst());
        PacketPlayOutEntityEquipment equipment = new PacketPlayOutEntityEquipment(clientArmorStand.getFirst().getId(), EnumItemSlot.HEAD, NMSUtils.getNMSCopy(mesh));

        PlayerConnection connection = NMSUtils.getPlayerConnection(player);
        connection.sendPacket(packet);
        connection.sendPacket(equipment);

        ((CombatantProfilePlayer)Excelsior.INSTANCE.getArenaManager().findArenaWithPlayer(player).get().getCombatantProfile(owner).get())
                .setViewingClientArmorstand(new CombatantProfilePlayer.ViewingClientArmorstand(clientArmorStand.getFirst(), owner));
    }

    public void spawn3DRepresentationServer(Location center) {
        ArmorStand stand = center.getWorld().spawn(center, ArmorStand.class);
        stand.setVisible(false);
        stand.setCustomName(name);
        stand.setCustomNameVisible(true);
        stand.setHelmet(new ItemStack(material));
        stand.setBasePlate(false);
        stand.setGravity(false);
    }
}
