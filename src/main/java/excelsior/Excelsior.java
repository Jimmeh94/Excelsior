package excelsior;

import ecore.ECore;
import excelsior.commands.ArenaCommands;
import excelsior.commands.TestCommands;
import excelsior.events.*;
import excelsior.game.chatchannels.ChatChannelAuction;
import excelsior.game.chatchannels.ChatChannelGlobal;
import excelsior.game.chatchannels.ChatChannelStaff;
import excelsior.managers.ManagerArena;
import excelsior.runnables.TimerArena;
import excelsior.runnables.TimerDirectionalAimArenas;
import excelsior.utils.database.MongoUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public class Excelsior extends JavaPlugin {

    //TODO Make sure title sending works in ECore, look in the Team class at broacastingTurnMessage
    //TODO implement permissions system in ECore, be sure to add to staff chat channel and arena commands
    //TODO store all amorstands in manager and delete on server shutdown and arena end game
    //TODO make despawn method for NMSUtils rather than in CardBase and HotbarCardDescription
    //TODO look at remaking scoreboard with Teams rather than other way. Look at bookmark
    //TODO Look at using Mongo with Morphia. Look at bookmark

    public static Excelsior INSTANCE;

    private ManagerArena arenaManager;

    @Override
    public void onEnable(){
        INSTANCE = this;

        registerListeners();
        registerCommands();
        registerRunnables();

        ECore.INSTANCE.getChannels().add(new ChatChannelGlobal());
        ECore.INSTANCE.getChannels().add(new ChatChannelAuction());
        ECore.INSTANCE.getChannels().add(new ChatChannelStaff());

        arenaManager = new ManagerArena();

        MongoUtils.readArenas();

        getLogger().info(">> " + getDescription().getName() + " v" + getDescription().getVersion() + " enabled! <<");
    }

    @Override
    public void onDisable(){

        for(World w: Bukkit.getWorlds()){
            for(Entity e: w.getEntities()){
                if(e instanceof ArmorStand){
                    e.remove();
                }
            }
        }

        MongoUtils.writeArenas();

        getLogger().info(">> " + getDescription().getName() + " v" + getDescription().getVersion() + " disabled! <<");
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new ErrorStackEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerModeEvents(), this);
        getServer().getPluginManager().registerEvents(new WorldEvents(), this);
        getServer().getPluginManager().registerEvents(new EntityEvents(), this);
    }

    private void registerCommands(){
        getCommand("test").setExecutor(new TestCommands());
        getCommand("arena").setExecutor(new ArenaCommands());
    }

    private void registerRunnables(){
        (new TimerArena()).start();
        (new TimerDirectionalAimArenas()).start();
    }

    public ManagerArena getArenaManager() {
        return arenaManager;
    }
}
