package excelsior;

import ecore.ECore;
import excelsior.commands.TestCommands;
import excelsior.events.PlayerEvents;
import excelsior.game.chatchannels.ChatChannelAuction;
import excelsior.game.chatchannels.ChatChannelGlobal;
import excelsior.game.chatchannels.ChatChannelStaff;
import org.bukkit.plugin.java.JavaPlugin;

public class Excelsior extends JavaPlugin {

    //TODO Make all function calls in PlayerQuitEvent as one function in ECore

    public static Excelsior INSTANCE;

    @Override
    public void onEnable(){
        INSTANCE = this;

        registerListeners();
        registerCommands();
        registerRunnables();

        ECore.INSTANCE.getChannels().add(new ChatChannelGlobal());
        ECore.INSTANCE.getChannels().add(new ChatChannelAuction());
        ECore.INSTANCE.getChannels().add(new ChatChannelStaff());

        getLogger().info(">> " + getDescription().getName() + " v" + getDescription().getVersion() + " enabled! <<");
    }

    @Override
    public void onDisable(){

        getLogger().info(">> " + getDescription().getName() + " v" + getDescription().getVersion() + " disabled! <<");
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
    }

    private void registerCommands(){
        getCommand("test").setExecutor(new TestCommands());

    }

    private void registerRunnables(){

    }

}
