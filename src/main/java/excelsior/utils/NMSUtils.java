package excelsior.utils;

import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.util.Optional;

public class NMSUtils {

    public static PlayerConnection getPlayerConnection(Player player){
        return ((CraftPlayer) player).getHandle().playerConnection;
    }

    public static ItemStack getNMSCopy(org.bukkit.inventory.ItemStack itemStack){
        return CraftItemStack.asNMSCopy(itemStack);
    }

}
