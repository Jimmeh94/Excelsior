package excelsior.commands;

import ecore.services.ByteColors;
import excelsior.Excelsior;
import excelsior.game.match.Arena;
import excelsior.game.match.Team;
import excelsior.game.match.field.Grid;
import excelsior.game.match.field.GridNormal;
import excelsior.game.match.gamemodes.GamemodeDuel;
import excelsior.game.match.profiles.CombatantProfilePlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;

        if(label.equalsIgnoreCase("test")){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("grid")){

                    Location location = player.getLocation();
                    new GridNormal(location.toVector(), location.getWorld().getName(), 11, 11, 3, 3, true,
                            Material.STAINED_GLASS, ByteColors.BLACK, Material.BARRIER, ByteColors.BLACK);

                } else if(args[0].equalsIgnoreCase("arena")){
                    Location location = player.getLocation();

                    Grid grid = new GridNormal(location.toVector(), location.getWorld().getName(), 11, 11, 3, 3, true,
                            Material.STAINED_GLASS, ByteColors.BLACK, Material.BARRIER, ByteColors.BLACK);

                    Excelsior.INSTANCE.getArenaManager().add(new Arena(grid, player.getLocation().getWorld().getName()));
                }
            }
        }

        return true;
    }
}
