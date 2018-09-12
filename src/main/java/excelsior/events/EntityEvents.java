package excelsior.events;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntityEvents implements Listener {

    @EventHandler
    public void onSpawn(EntitySpawnEvent event){
        if(event.getEntityType() != EntityType.ARMOR_STAND){
            event.setCancelled(true);
        } else {
            System.out.println("SPAWNING");
        }
    }

}
