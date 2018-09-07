package excelsior;

import ecore.ECore;
import ecore.events.PlayerEvents;
import org.bukkit.plugin.java.JavaPlugin;

public class Excelsior extends JavaPlugin {

    @Override
    public void onEnable(){
        new ECore(getConfig().getString("database-username"), getConfig().getString("database-password"),
                getConfig().getString("database-ip"), getConfig().getString("database-name"));

        registerListeners();
        registerCommands();
        registerRunnables();
    }

    @Override
    public void onDisable(){
        ECore.INSTANCE.shutdown();
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    }

    private void registerCommands(){

    }

    private void registerRunnables(){

    }

}
