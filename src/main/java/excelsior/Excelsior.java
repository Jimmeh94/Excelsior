package excelsior;

import ecore.ECore;
import org.bukkit.plugin.java.JavaPlugin;

public class Excelsior extends JavaPlugin {

    @Override
    public void onEnable(){
        new ECore(getConfig().getString("database-username"), getConfig().getString("database-password"),
                getConfig().getString("database-ip"), getConfig().getString("database-name"));
    }

    @Override
    public void onDisable(){

    }

}
