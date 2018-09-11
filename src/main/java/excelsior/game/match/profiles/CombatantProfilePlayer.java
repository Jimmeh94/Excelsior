package excelsior.game.match.profiles;

import ecore.services.NMSUtils;
import excelsior.game.cards.Deck;
import excelsior.game.hotbars.duel.HotbarHand;
import excelsior.game.match.field.Cell;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.UUID;

public class CombatantProfilePlayer extends CombatantProfile {

    private Cell currentAim;
    private ViewingClientArmorstand viewingClientArmorstand;

    public CombatantProfilePlayer(UUID owner, Deck deck) {
        super(owner, deck);
    }

    public Cell getCurrentAim() {
        return currentAim;
    }

    public void setCurrentAim(Cell currentAim) {
        this.currentAim = currentAim;
    }

    public boolean isViewingClientArmorstand(){
        return viewingClientArmorstand != null;
    }

    public void setViewingClientArmorstand(ViewingClientArmorstand v){
        if(isViewingClientArmorstand()){
            viewingClientArmorstand.destory(Bukkit.getPlayer(getUUID()));
        }

        viewingClientArmorstand = v;
    }

    public ViewingClientArmorstand getViewingClientArmorstand() {
        return viewingClientArmorstand;
    }

    public static class ViewingClientArmorstand {

        private EntityArmorStand armorStand;
        private UUID owner;

        public ViewingClientArmorstand(EntityArmorStand armorStand, UUID owner) {
            this.armorStand = armorStand;
            this.owner = owner;
        }

        public void check(){
            Player player = Bukkit.getPlayer(owner);
            if(player == null){
                return;
            }

            Vector stand = new Vector(armorStand.locX, armorStand.locY, armorStand.locZ);
            if(player.getLocation().toVector().distance(stand) > 5){
                destory(player);
            }
        }

        public void destory(Player player){
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armorStand.getId());
            NMSUtils.getPlayerConnection(player).sendPacket(packet);
        }
    }
}
