package excelsior.commands;

import ecore.ECore;
import ecore.services.messages.ServiceMessager;
import excelsior.Excelsior;
import excelsior.game.match.Arena;
import excelsior.game.match.Team;
import excelsior.game.match.field.GridNormal;
import excelsior.game.match.gamemodes.Gamemode;
import excelsior.game.match.gamemodes.GamemodeDuel;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ArenaCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {

        if(commandSender instanceof Player){
            Player player = (Player) commandSender;

            if(label.equalsIgnoreCase("arena")){
                if(args.length > 0){
                    if(args[0].equalsIgnoreCase("add") && args.length == 5){

                        String world = player.getLocation().getWorld().getName();
                        Location location = player.getLocation();
                        location.subtract(0, 1, 0);
                        Excelsior.INSTANCE.getArenaManager().add(new Arena(new GamemodeDuel(world),
                                new GridNormal(location.toVector(), world, Integer.valueOf(args[1]),
                                        Integer.valueOf(args[2]), Integer.valueOf(args[3]), Integer.valueOf(args[4]), false),
                                world));

                    } else if(args[0].equalsIgnoreCase("start")){

                        Optional<Arena> arena = Excelsior.INSTANCE.getArenaManager().getAvailableArena();
                        if(!arena.isPresent()){
                            ECore.INSTANCE.getMessager().sendMessage(player, ChatColor.RED + "No arena available! Tell staff", Optional.of(ServiceMessager.Prefix.ERROR));
                            return true;
                        }

                        Gamemode gamemode = arena.get().getGamemode();
                        gamemode.addTeam(new Team(new CombatantProfilePlayer(player.getUniqueId())));
                        arena.get().start();
                    }
                }
            }
        }

        return true;
    }
}
